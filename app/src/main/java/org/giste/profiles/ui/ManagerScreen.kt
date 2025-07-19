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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.giste.profiles.R
import org.giste.profiles.domain.Profile
import org.giste.profiles.ui.theme.ProfilesTheme

@Composable
@Preview
fun ManagerPreview() {
    ProfilesTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ManagerScreen(
                state = ManagerViewModel.UiState(
                    profiles = listOf(
                        Profile(1L, "Profile 1"),
                        Profile(2L, "Profile 2"),
                        Profile(3L, "Profile 3"),
                    ),
                    selectedProfile = 1L,
                ),
                onProfileAdd = {},
                onProfileSelect = {},
                onProfileClick = {},
                onProfileDelete = {},
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ManagerScreen(
    state: ManagerViewModel.UiState,
    onProfileAdd: () -> Unit,
    onProfileSelect: (Profile) -> Unit,
    onProfileClick: (Profile) -> Unit,
    onProfileDelete: (Profile) -> Unit,
) {
    ProvideTopBarTitle {
        Text(stringResource(R.string.manager_screen_title))
    }

    ProvideFab {
        FloatingActionButton(
            onClick = { onProfileAdd() },
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(R.string.manager_screen_add_profile_content_description)
            )
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(ProfilesTheme.dimensions.spacing)
    ) {
        items(
            items = state.profiles,
            key = { it.id }
        ) { profile ->
            ProfileRow(
                isSelected = profile.id == state.selectedProfile,
                name = profile.name,
                onRadioSelect = { onProfileSelect(profile) },
                onTextClick = { onProfileClick(profile) },
                onDelete = { onProfileDelete(profile) }
            )
        }
    }
}

@Composable
private fun ProfileRow(
    isSelected: Boolean,
    name: String,
    onRadioSelect: () -> Unit,
    onTextClick: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = isSelected, onClick = { onRadioSelect() })
        Text(
            text = name,
            modifier = Modifier
                .weight(1f)
                .clickable { onTextClick() },
        )
        IconButton(onClick = { onDelete() }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.manager_screen_delete_profile_content_description)
            )
        }
    }
}
