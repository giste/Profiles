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

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import org.giste.profiles.R

private const val NAME_MAX_LENGTH = 20

@Preview
@Composable
fun NewProfilePreview() {
    NewProfileDialog(
        uiState = NewProfileViewModel.UiState(
            name = "Profile name",
            error = NewProfileViewModel.NameError.NoError,
        ),
        onNameChange = {},
        onAccept = {},
        onDismiss = {},
        onNewProfile = {},
    )
}

@Preview
@Composable
fun NewProfileErrorPreview() {
    NewProfileDialog(
        uiState = NewProfileViewModel.UiState(error = NewProfileViewModel.NameError.BlankNameError),
        onNameChange = {},
        onAccept = {},
        onDismiss = {},
        onNewProfile = {},
    )
}

@OptIn(FlowPreview::class)
@Composable
fun NewProfileDialog(
    uiState: NewProfileViewModel.UiState,
    onNameChange: (String) -> Unit,
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
    onNewProfile: (Long) -> Unit,
) {
    if (uiState.id > 0) {
        onNewProfile(uiState.id)
    }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = stringResource(R.string.profile_name_dialog_title)) },
        confirmButton = {
            IconButton(
                onClick = { onAccept() },
                enabled = uiState.error is NewProfileViewModel.NameError.NoError,
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = stringResource(R.string.dialog_confirm_content_description)
                )
            }
        },
        dismissButton = {
            IconButton(
                onClick = { onDismiss() },
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.dialog_dismiss_content_description)
                )
            }
        },
        text = {
            var name by rememberSaveable { mutableStateOf("") }
            LaunchedEffect(name) {
                snapshotFlow { name }
                    .debounce(500L)
                    .collectLatest { onNameChange(it) }
            }
            val focusRequester = remember { FocusRequester() }
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            TextField(
                value = name,
                onValueChange = { name = it.take(NAME_MAX_LENGTH) },
                modifier = Modifier
                    .focusRequester(focusRequester),
                label = { Text(text = stringResource(R.string.profile_name_dialog_label)) },
                trailingIcon = {
                    if (uiState.error !is NewProfileViewModel.NameError.NoError) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = stringResource(
                                id = R.string.profile_name_dialog_error_content_description
                            ),
                            tint = MaterialTheme.colorScheme.error,
                        )
                    }
                },
                supportingText = {
                    Text(
                        text = when (uiState.error) {
                            NewProfileViewModel.NameError.NoError ->
                                "${uiState.name.length}/$NAME_MAX_LENGTH"

                            NewProfileViewModel.NameError.BlankNameError ->
                                stringResource(R.string.validation_not_blank)

                            NewProfileViewModel.NameError.NameExistsError ->
                                stringResource(R.string.validation_already_exists)
                        },
                        color = if (uiState.error is NewProfileViewModel.NameError.NoError) {
                            Color.Unspecified
                        } else {
                            MaterialTheme.colorScheme.error
                        },
                    )
                },
                isError = uiState.error !is NewProfileViewModel.NameError.NoError,
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onAccept() }
                ),
                singleLine = true,
            )
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    )
}