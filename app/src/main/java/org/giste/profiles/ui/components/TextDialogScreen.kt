package org.giste.profiles.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.giste.profiles.R
import java.lang.Integer.min

@Preview
@Composable
fun TextDialogPreview() {
    TextDialogScreen(
        title = "Title",
        cancelLabel = "Cancel",
        acceptLabel = "Accept",
        onDismiss = { },
        onAccept = { },
        label = "Label",
        text = "Initial text",
        error = "Error text",
        onTextChange = {},
        maxLength = 20
    )
}

@Composable
fun TextDialogScreen(
    title: String,
    cancelLabel: String = stringResource(R.string.dialog_cancel_label),
    acceptLabel: String = stringResource(R.string.dialog_accept_label),
    onDismiss: () -> Unit = {},
    onAccept: (String) -> Unit,
    label: String,
    text: String,
    error: String,
    onTextChange: (String) -> Unit,
    maxLength: Int = 0
) {
    DialogScreen(
        title = title,
        cancelLabel = cancelLabel,
        acceptLabel = acceptLabel,
        onDismiss = onDismiss,
        onAccept = { onAccept(text) },
        acceptEnabled = { error.isBlank() }
    ) {
        TextDialogContent(
            label = label,
            text = text,
            onTextChange = onTextChange,
            error = error,
            maxLength = min(maxLength, 0),
            onAccept = { onAccept(text) }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun TextDialogContent(
    label: String,
    text: String,
    onTextChange: (String) -> Unit,
    error: String,
    maxLength: Int,
    onAccept: () -> Unit
) {
    val textFieldContentDescription =
        stringResource(R.string.dialog_text_field_content_description, label)
    val isError = error != ""
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    Column {
        TextField(
            value = text,
            onValueChange = onTextChange,
            label = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = textFieldContentDescription
                    testTag = "TextField"
                }
                .focusRequester(focusRequester),
            trailingIcon = {
                if (maxLength > 0) {
                    Text(
                        text = "${text.length}/$maxLength",
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp)
                            .semantics { testTag = "LengthText" },
                        color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
                        style = MaterialTheme.typography.caption
                    )
                }
            },
            isError = isError,
            keyboardOptions = KeyboardOptions.Default.copy(
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onAccept() }
            ),
            singleLine = true
        )
        if (isError) {
            Text(
                text = error,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .semantics { testTag = "ErrorText" },
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption
            )
        }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
            delay(100)
            keyboard?.show()
        }
    }
}