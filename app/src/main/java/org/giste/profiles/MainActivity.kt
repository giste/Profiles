package org.giste.profiles

import android.app.NotificationManager
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import dagger.hilt.android.AndroidEntryPoint
import org.giste.profiles.ui.NavGraphs
import org.giste.profiles.ui.components.FabSettings
import org.giste.profiles.ui.components.TopBarSettings
import org.giste.profiles.ui.theme.ProfilesTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkWriteSettingsPermission()
        checkAccessNotificationPolicy()

        setContent {
            ProfileApp()
        }
    }

    private fun checkWriteSettingsPermission() {
        if (!Settings.System.canWrite(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        }
    }

    private fun checkAccessNotificationPolicy() {
        val nm = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (nm.isNotificationPolicyAccessGranted) {
            val audioManager = applicationContext.getSystemService(AUDIO_SERVICE) as AudioManager
            audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
        } else {
            // Ask the user to grant access
            val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            startActivity(intent)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ProfileApp()
}

@Composable
fun ProfileApp() {
    ProfilesTheme {
        val navController = rememberNavController()

        val (tbUpVisible, setTbUpVisible) = remember { mutableStateOf(false) }
        val (tbTitle, setTbTitle) = remember { mutableStateOf("") }
        val topBarSettings = object : TopBarSettings {
            override fun config(upVisible: Boolean, title: String) {
                setTbUpVisible(upVisible)
                setTbTitle(title)
            }
        }

        val (fabVisible, setFabVisible) = remember { mutableStateOf(false) }
        val (fabIcon, setFabIcon) = remember { mutableStateOf(Icons.Default.Add) }
        val (contentDescription, setContentDescription) = remember { mutableStateOf("") }
        val (fabOnClick, setFabOnClick) = remember { mutableStateOf({ }) }
        val fabSettings = object : FabSettings {
            override fun config(
                visible: Boolean,
                icon: ImageVector,
                contentDescription: String,
                onClick: () -> Unit
            ) {
                setFabVisible(visible)
                setFabIcon(icon)
                setContentDescription(contentDescription)
                setFabOnClick(onClick)
            }
        }

        Scaffold(
            topBar = {
                TopBar(
                    upVisible = tbUpVisible,
                    title = tbTitle,
                    navController = navController
                )
            },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                FloatingButton(
                    visible = fabVisible,
                    icon = fabIcon,
                    contentDescription = contentDescription,
                    onClick = fabOnClick
                )
            }
        ) { innerPadding ->
            ProfilesNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
                topBarSettings = topBarSettings,
                fabSettings = fabSettings
            )
        }
    }
}

@Composable
fun ProfilesNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    topBarSettings: TopBarSettings,
    fabSettings: FabSettings
) {
    DestinationsNavHost(
        navGraph = NavGraphs.root,
        navController = navController,
        modifier = modifier,
        dependenciesContainerBuilder = {
            dependency(topBarSettings)
            dependency(fabSettings)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(upVisible: Boolean, title: String, navController: NavHostController) {
    val backContentDescription = stringResource(id = R.string.app_back_content_description)

    TopAppBar(
        navigationIcon = {
            if (upVisible) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = backContentDescription
                    )
                }
            }
        },
        title = { Text(title) }
    )
}

@Composable
fun FloatingButton(
    visible: Boolean,
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    if (visible) {
        FloatingActionButton(
            onClick = onClick
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription
            )
        }
    }
}
