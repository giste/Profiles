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
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import org.giste.profiles.ui.Profiles
import org.giste.profiles.ui.ProfilesPermissions
import org.giste.profiles.ui.theme.ProfilesTheme
import androidx.core.net.toUri

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        checkWriteSettingsPermission()

        setContent {
            ProfilesTheme {
//                val multiplePermissionState = rememberMultiplePermissionsState(
//                    permissions = listOf(
//                        //Manifest.permission.ACCESS_NOTIFICATION_POLICY,
//                        Manifest.permission.WRITE_SETTINGS,
//                        //Manifest.permission.MODIFY_AUDIO_SETTINGS,
//                    )
//                )
//
//                ProfilesPermissions(multiplePermissionState)
//
//                if (multiplePermissionState.allPermissionsGranted) {
                    Profiles()
//                }
            }
        }
    }

    private fun checkWriteSettingsPermission() {
        if (!Settings.System.canWrite(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = "package:$packageName".toUri()
            startActivity(intent)
        }
    }
}
