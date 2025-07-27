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

package org.giste.profiles.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import org.giste.profiles.R
import org.giste.profiles.ui.theme.ProfilesTheme

@Preview
@Composable
fun PermissionsScreenPreview() {
    ProfilesTheme {
        Surface {
            PermissionsScreen(
                permissions = listOf(
                    Permission(
                        name = "WRITE_SETTINGS",
                        rationale = "Needed to modify brightness.",
                        request = { }
                    ),
                    Permission(
                        name = "ACCESS_NOTIFICATION_POLICY",
                        rationale = "Needed to set different ringer modes.",
                        request = { }
                    ),
                )
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PermissionsScreen(
    permissions: List<Permission>,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.permissions_title)) },
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
        ) {
            Column(modifier = Modifier.padding(ProfilesTheme.dimensions.padding)
            ) {
                Text(text = stringResource(R.string.permissions_rationale))
                Spacer(modifier = Modifier.height(ProfilesTheme.dimensions.spacing))
                permissions.forEach {
                    Spacer(modifier = Modifier.height(ProfilesTheme.dimensions.spacing))
                    PermissionRow(
                        permission = it.name,
                        rationale = it.rationale,
                        request = it.request,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRow(
    permission: String,
    rationale: String,
    request: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(ProfilesTheme.dimensions.padding),
        verticalAlignment = Alignment.Top,
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = permission,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(text = rationale)
        }
        Spacer(modifier = Modifier.width(ProfilesTheme.dimensions.spacing))
        Button(onClick = { request() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowForward,
                contentDescription = "",
            )
        }
    }
}

data class Permission(
    val name: String,
    val rationale: String,
    val request: () -> Unit,
)