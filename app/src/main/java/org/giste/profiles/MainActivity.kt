/*
 * Copyright 2025 Giste Trappiste
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.giste.profiles

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import org.giste.profiles.ui.Permission
import org.giste.profiles.ui.PermissionsScreen
import org.giste.profiles.ui.Profiles
import org.giste.profiles.ui.theme.ProfilesTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ProfilesTheme {
                val multiplePermissionState = rememberMultiplePermissionsState(
                    permissions = listOf(
                        Manifest.permission.ACCESS_NOTIFICATION_POLICY,
                        Manifest.permission.MODIFY_AUDIO_SETTINGS,
                    )
                )
                multiplePermissionState.permissions.forEach {
                    Log.d("MainActivity", "Permission: ${it.permission} granted: ${it.status.isGranted}")
                }

                var isWriteSettingsGranted by rememberSaveable {
                    mutableStateOf(isWriteSettingsPermissionGranted())
                }
                Log.d("MainActivity", "Permission: WRITE_SETTINGS granted: $isWriteSettingsGranted")

                LifecycleResumeEffect(Unit) {
                    isWriteSettingsGranted = isWriteSettingsPermissionGranted()

                    onPauseOrDispose {  }
                }

                if (multiplePermissionState.allPermissionsGranted && isWriteSettingsGranted) {
                    Profiles()
                } else {
                    PermissionsScreen(
                        permissions = multiplePermissionState.revokedPermissions
                            .mapNotNull {
                                when (val permissionName = it.permission) {
                                    Manifest.permission.ACCESS_NOTIFICATION_POLICY -> Permission(
                                        name = permissionName.substringAfterLast('.'),
                                        rationale = stringResource(R.string.permissions_rationale_access_notification_policy),
                                        request = { requestAccessNotificationPolicy() }
                                    )

                                    Manifest.permission.MODIFY_AUDIO_SETTINGS -> Permission(
                                        name = permissionName.substringAfterLast('.'),
                                        rationale = stringResource(R.string.permissions_rationale_modify_audio_settings),
                                        request = { it.launchPermissionRequest() }
                                    )

                                    else -> null
                                }
                            }
                            .toMutableList()
                            .apply {
                                if (!isWriteSettingsGranted) {
                                    Log.d("MainActivity", "Adding WRITE_SETTINGS permission")
                                    this.add(
                                        Permission(
                                            name = "WRITE_SETTINGS",
                                            rationale = stringResource(R.string.permissions_rationale_write_settings),
                                            request = { requestWriteSettingsPermission() }
                                        )
                                    )
                                }
                            }
                            .toList()
                    )
                }
            }
        }
    }

    private fun isWriteSettingsPermissionGranted(): Boolean {
        val isGranted = Settings.System.canWrite(this)
        Log.d("MainActivity", "isWriteSettingsPermissionGranted: $isGranted")
        return isGranted
    }

    private fun requestWriteSettingsPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        intent.data = "package:$packageName".toUri()
        startActivity(intent)
    }

    private fun requestAccessNotificationPolicy() {
        val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
        startActivity(intent)
    }
}
