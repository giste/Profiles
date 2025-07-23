package org.giste.profiles.ui

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import org.giste.profiles.MainActivity
import org.giste.profiles.R
import kotlin.system.exitProcess

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionsScreen(
    multiplePermissionState: MultiplePermissionsState,
) {
    val context = LocalContext.current
    var showRationalDialog by remember { mutableStateOf(false) }

    for (permission in multiplePermissionState.permissions) {
        Log.d("PermissionsScreen", "${permission.permission}: ${permission.status}")
    }


    if (!multiplePermissionState.allPermissionsGranted) {
        if (multiplePermissionState.shouldShowRationale) {
            // Show a rationale if needed (optional)
            showRationalDialog = true
        } else {
            LaunchedEffect(showRationalDialog) {
                // Request the permission
                multiplePermissionState.launchMultiplePermissionRequest()
            }
        }
    }

    Log.d("PermissionsScreen", "Show rationale: $showRationalDialog")

    if (showRationalDialog) {
        AlertDialog(
            onDismissRequest = {
                showRationalDialog = false
            },
            title = { Text(text = stringResource(R.string.permissions_title)) },
            text = { Text(text = stringResource(R.string.permissions_request)) },
            confirmButton = {
                IconButton(
                    onClick = {
                        showRationalDialog = false
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        )
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent, null)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = stringResource(R.string.dialog_confirm_content_description),
                    )
                }
            },
            dismissButton = {
                IconButton(
                    onClick = {
                        showRationalDialog = false
                        // Permissions not granted, close application
                        MainActivity().finish()
                        exitProcess(0)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.dialog_dismiss_content_description),
                    )
                }
            },
        )
    }
}