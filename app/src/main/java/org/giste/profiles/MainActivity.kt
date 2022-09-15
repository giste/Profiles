package org.giste.profiles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import org.giste.profiles.ui.*
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
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        val managerViewModel = hiltViewModel<ManagerViewModel>()

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        Scaffold(
            topBar = { TopBar(destination = currentDestination) },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                FloatingButton(
                    destination = currentDestination,
                    navController = navController,
                    managerViewModel = managerViewModel
                )
            }
        ) { innerPadding ->
            ProfilesNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
                managerViewModel
            )
        }
    }
}

@Composable
fun ProfilesNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    managerViewModel: ManagerViewModel
) {
    NavHost(
        navController = navController,
        startDestination = ProfileScreens.Manager.name,
        modifier = modifier
    ) {
        composable(ProfileScreens.Manager.name) {
            ProfileManagerBody(
                managerViewModel = managerViewModel,
                onProfileClick = { profile ->
                    navController.navigate("${ProfileScreens.Profile}/${profile.id}")
                },
                navController
            )
        }
        composable(
            route = "${ProfileScreens.Profile.name}/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) {
            ProfileBody(
                profileViewModel = hiltViewModel(),
                navigate = {
                    navController.navigate("${ProfileScreens.Manager}") {
                        popUpTo("${ProfileScreens.Manager}") { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
fun TopBar(destination: NavDestination?) {
    if (destination?.route == ProfileScreens.Manager.name) {
        ManagerToolBar()
    } else {
        ProfileToolBar()
    }
}

@Composable
fun FloatingButton(
    destination: NavDestination?,
    navController: NavHostController,
    managerViewModel: ManagerViewModel
) {
    if (destination?.route == ProfileScreens.Manager.name) {
        ManagerFloatingButton(
            onClick = {
                managerViewModel.addProfile(it)
            }
        )
    }
}

enum class ProfileScreens { Manager, Profile }