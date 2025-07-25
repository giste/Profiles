package org.giste.profiles.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.isGranted
import org.giste.profiles.ui.theme.ProfilesTheme

@Preview
@Composable
fun ProfilesPermissionsPreview() {
    ProfilesTheme {
        ProfilesPermissionContent(
            permissions = listOf(
                Permission("Write settings", "Used to store profiles"),
                Permission("Notify", "Used to control volumes"),
            ),
            onDismiss = { },
            onConfirm = { },
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ProfilesPermissions(
    permissionsState: MultiplePermissionsState,
) {
    for (permission in permissionsState.permissions) {
        Log.d(
            "ProfilesPermissions",
            "Permission '${permission.permission} granted: ${permission.status.isGranted}"
        )
    }

    if (!permissionsState.allPermissionsGranted) {
        val permissions = mutableListOf<Permission>()
        for (permission in permissionsState.revokedPermissions) {
            permissions.add(
                Permission(
                    permission = permission.permission,
                    rationale = "Rationale for ${permission.permission}"
                )
            )
        }

        ProfilesPermissionContent(
            permissions = permissions,
            onDismiss = { },
            onConfirm = { permissionsState.launchMultiplePermissionRequest() }
        )
    }
}

@Composable
private fun ProfilesPermissionContent(
    permissions: List<Permission>,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                // Consume this insets so that it's not applied again
                // when using safeDrawing in the hierarchy below
                .consumeWindowInsets(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.CenterStart,
        ) {
            Column(
                modifier = Modifier.padding(ProfilesTheme.dimensions.padding)
            ) {
                Text(
                    text = "Permissions",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(text = "Profiles needs these permissions to work")
                Spacer(modifier = Modifier.height(ProfilesTheme.dimensions.spacing))
                for (permission in permissions) {
                    Text(
                        text = permission.permission,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(text = permission.rationale)
                    Spacer(modifier = Modifier.height(ProfilesTheme.dimensions.spacing))
                }
//                Button(
//                    onClick = { onConfirm() }
//                ) { Text(text = "OK") }
            }

            LaunchedEffect(permissions) {
                Log.d("ProfilesPermissionContent", "Launching permission request...")
                onConfirm()
            }
        }
    }
}

private data class Permission(
    val permission: String,
    val rationale: String,
)