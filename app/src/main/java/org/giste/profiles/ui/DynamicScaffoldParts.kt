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

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.referentialEqualityPolicy
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DynamicTopBar(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    backStackEntry?.let { entry ->
        val viewModel: DynamicScaffoldViewModel = viewModel(
            viewModelStoreOwner = entry,
            initializer = { DynamicScaffoldViewModel() }
        )

        TopAppBar(
            title = viewModel.title,
            modifier = modifier,
            navigationIcon = { },
            colors = topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
        )
    }
}

@Composable
internal fun DynamicFab(
    navController: NavController,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    backStackEntry?.let { entry ->
        val viewModel: DynamicScaffoldViewModel = viewModel(
            viewModelStoreOwner = entry,
            initializer = { DynamicScaffoldViewModel() }
        )

        viewModel.fab()
    }
}

@Composable
internal fun ProvideTopBarTitle(
    title: @Composable () -> Unit,
) {
    getCurrentScaffoldViewModel()?.let { viewModel ->
        LaunchedEffect(title) {
            viewModel.title = title
        }
    }
}

@Composable
internal fun ProvideFab(
    fab: @Composable () -> Unit,
) {
    getCurrentScaffoldViewModel()?.let { viewModel ->
        LaunchedEffect(fab) {
            viewModel.fab = fab
        }
    }
}

@Composable
private fun getCurrentScaffoldViewModel(): DynamicScaffoldViewModel? {
    return (LocalViewModelStoreOwner.current as? NavBackStackEntry)?.let { owner ->
        viewModel(
            viewModelStoreOwner = owner,
            initializer = { DynamicScaffoldViewModel() },
        )
    }
}

private class DynamicScaffoldViewModel : ViewModel() {
    var title by mutableStateOf<@Composable () -> Unit>({}, referentialEqualityPolicy())
    var fab by mutableStateOf<@Composable () -> Unit>({}, referentialEqualityPolicy())
}
