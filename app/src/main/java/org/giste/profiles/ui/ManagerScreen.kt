package org.giste.profiles.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.giste.profiles.R
import org.giste.profiles.domain.Profile
import org.giste.profiles.ui.components.FabSettings
import org.giste.profiles.ui.components.TopBarSettings
import org.giste.profiles.ui.destinations.ProfileBodyDestination
import org.giste.profiles.ui.destinations.ProfileNameBodyDestination

@Preview(showBackground = true)
@Composable
fun ManagerPreview() {
    ManagerScreen(
        profileList = listOf(
            Profile(id = 1L, name = "Profile 1"),
            Profile(id = 2L, name = "Profile 2"),
            Profile(id = 3L, name = "Profile 3")
        ),
        selectedId = 0L,
        onProfileSelect = {},
        onProfileClick = {},
        onProfileDelete = {}
    )
}

@RootNavGraph(start = true)
@Destination
@Composable
fun ManagerBody(
    navigator: DestinationsNavigator,
    topBarSettings: TopBarSettings,
    fabSettings: FabSettings
) {
    val managerViewModel: ManagerViewModel = hiltViewModel()
    val title = stringResource(id = R.string.manager_screen_title)

    LaunchedEffect("Manager") {
        topBarSettings.config(
            upVisible = false,
            title = title
        )
        fabSettings.config(
            visible = true,
            icon = Icons.Default.Add,
            onClick = { navigator.navigate(ProfileNameBodyDestination) }
        )
    }

    ManagerScreen(
        profileList = managerViewModel.profileList,
        selectedId = managerViewModel.selectedProfileId,
        onProfileSelect = {}, // managerViewModel::onProfileSelected,
        onProfileClick = { profile -> navigator.navigate(ProfileBodyDestination(profile.id), true) },
        onProfileDelete = managerViewModel::deleteProfile
    )
}

@Composable
fun ManagerScreen(
    profileList: List<Profile>,
    selectedId: Long,
    onProfileSelect: (Profile) -> Unit,
    onProfileClick: (Profile) -> Unit,
    onProfileDelete: (Profile) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(all = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(
            items = profileList,
            key = { profile -> profile.id }
        ) { profile ->
            ProfileRow(
                isSelected = profile.id == selectedId,
                name = profile.name,
                onRadioSelect = { onProfileSelect(profile) },
                onTextClick = { onProfileClick(profile) },
                onDelete = { onProfileDelete(profile) }
            )
        }
    }
}

@Composable
fun ProfileRow(
    isSelected: Boolean,
    name: String,
    onRadioSelect: () -> Unit,
    onTextClick: () -> Unit,
    onDelete: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = isSelected, onClick = { onRadioSelect() })
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = name, modifier = Modifier
            .weight(1f)
            .clickable { onTextClick() }
            .padding(vertical = 8.dp)
        )
        IconButton(onClick = { onDelete() }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(id = R.string.profile_screen_delete_profile_content_description)
            )
        }
    }
}
