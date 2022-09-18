package org.giste.profiles

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import dagger.hilt.android.AndroidEntryPoint
import org.giste.profiles.ui.*
import org.giste.profiles.ui.components.FabSettings
import org.giste.profiles.ui.destinations.ManagerBodyDestination
import org.giste.profiles.ui.destinations.ProfileBodyDestination
import org.giste.profiles.ui.theme.ProfilesTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileApp()
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
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        val (fabVisible, setFabVisible) = remember { mutableStateOf(false) }
        val (fabIcon, setFabIcon) = remember { mutableStateOf(Icons.Default.Add) }
        val (fabOnClick, setFabOnClick) = remember { mutableStateOf({ }) }

        val fabSettings = object: FabSettings {
            override fun config(visible: Boolean, icon: ImageVector, onClick: () -> Unit) {
                setFabVisible(visible)
                setFabIcon(icon)
                setFabOnClick(onClick)
            }
        }

        Scaffold(
            topBar = { TopBar(destination = currentDestination) },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                FloatingButton(
                    visible = fabVisible,
                    icon = fabIcon,
                    onClick = fabOnClick
                )
            }
        ) { innerPadding ->
            ProfilesNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
                fabSettings = fabSettings
            )
        }
    }
}

@Composable
fun ProfilesNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    fabSettings: FabSettings
) {
    DestinationsNavHost(
        navGraph = NavGraphs.root,
        navController = navController,
        modifier = modifier
    ) {
        composable(ManagerBodyDestination){
            ManagerBody(navigator = destinationsNavigator, fabSettings = fabSettings)
        }
        composable(ProfileBodyDestination){
            ProfileBody(fabSettings = fabSettings)
        }
    }
}

@Composable
fun TopBar(destination: NavDestination?) {
    Log.d("TopBar", "destination: $destination")

    when (destination?.route) {
        ManagerBodyDestination.route -> {
            ManagerToolBar()
        }
        ProfileBodyDestination.route -> {
            ProfileToolBar()
        }
    }
}

@Composable
fun FloatingButton(
    visible: Boolean,
    icon: ImageVector,
    onClick: () -> Unit
) {
    if(visible) {
        FloatingActionButton(
            onClick = onClick
        ) {
            Icon(
                //imageVector = Icons.Default.Add,
                imageVector = icon,
                contentDescription = stringResource(id = R.string.manager_screen_add_profile_content_description)
            )
        }
    }
}
