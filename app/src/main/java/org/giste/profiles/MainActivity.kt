package org.giste.profiles

import android.os.Bundle
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import dagger.hilt.android.AndroidEntryPoint
import org.giste.profiles.ui.ManagerToolBar
import org.giste.profiles.ui.ManagerViewModel
import org.giste.profiles.ui.NavGraphs
import org.giste.profiles.ui.ProfileToolBar
import org.giste.profiles.ui.destinations.ManagerBodyDestination
import org.giste.profiles.ui.destinations.ProfileBodyDestination
import org.giste.profiles.ui.destinations.ProfileNameBodyDestination
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

        Scaffold(
            topBar = { TopBar(destination = currentDestination) },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                FloatingButton(
                    destination = currentDestination,
                    onClick = { navController.navigate(ProfileNameBodyDestination.route) }
                )
            }
        ) { innerPadding ->
            ProfilesNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun ProfilesNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    DestinationsNavHost(
        navGraph = NavGraphs.root,
        navController = navController,
        modifier = modifier,
        dependenciesContainerBuilder = {
            dependency(ManagerBodyDestination) { hiltViewModel<ManagerViewModel>() }
        }
    )
}

@Composable
fun TopBar(destination: NavDestination?) {
    when(destination?.route) {
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
    destination: NavDestination?,
    onClick: () -> Unit
) {
    if (destination?.route == ManagerBodyDestination.route) {
        FloatingActionButton(
            onClick = onClick
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.manager_screen_add_profile_content_description)
            )
        }
    }
}
