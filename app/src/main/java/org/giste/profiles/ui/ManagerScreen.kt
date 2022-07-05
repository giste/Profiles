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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.giste.profiles.R
import org.giste.profiles.domain.Profile

@Preview(showBackground = true)
@Composable
fun ManagerPreview() {
    ProfileManagerScreen(
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

@Composable
fun ProfileManagerBody(
    managerViewModel: ManagerViewModel,
    onProfileClick: (Profile) -> Unit
) {
    ProfileManagerScreen(
        profileList = managerViewModel.profileList,
        selectedId = managerViewModel.selectedProfileId,
        onProfileSelect = {}, // managerViewModel::onProfileSelected,
        onProfileClick = onProfileClick,
        onProfileDelete = managerViewModel::deleteProfile
    )
}

@Composable
fun ProfileManagerScreen(
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
                onDelete = {onProfileDelete(profile)}
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

@Composable
fun ManagerToolBar() {
    TopAppBar { Text(stringResource(id = R.string.manager_screen_title)) }
}

@Composable
fun ManagerFloatingButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(id = R.string.manager_screen_add_profile_content_description)
        )
    }
}