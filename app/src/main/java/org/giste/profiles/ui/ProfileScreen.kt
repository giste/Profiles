package org.giste.profiles.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.giste.profiles.R

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    ProfileScreen(
        name = "Profile 1",
        onNameChange = {},
        onDelete = {}
    )
}

@Composable
fun ProfileBody(
    profileViewModel: ProfileViewModel,
    navigate: () -> Unit
) {
    ProfileScreen(
        name = profileViewModel.profile.name,
        onNameChange = profileViewModel::onNameChange,
        onDelete = {
            //profileViewModel.deleteProfile()
            navigate()
        }
    )
}

@Composable
fun ProfileScreen(
    name: String,
    onNameChange: (String) -> Unit,
    onDelete: () -> Unit
) {
    Column(modifier = Modifier.padding(8.dp)) {
        ProfileName(
            name = name,
            onChange = onNameChange,
            onDelete = onDelete
        )
    }
}

@Composable
fun ProfileName(
    name: String,
    onChange: (String) -> Unit,
    onDelete: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        TextField(
            value = name,
            onValueChange = onChange,
            singleLine = true,
            placeholder = { Text(stringResource(id = R.string.profile_screen_name_label)) },
            modifier = Modifier.weight(1F)
        )
        Spacer(modifier = Modifier.width(16.dp))
        IconButton(onClick = { onDelete() }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(id = R.string.profile_screen_delete_profile_content_description)
            )
        }
    }
}

@Composable
fun ProfileToolBar() {
    TopAppBar { Text(stringResource(id = R.string.profile_screen_title)) }
}