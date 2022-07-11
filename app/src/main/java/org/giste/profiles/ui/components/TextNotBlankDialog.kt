package org.giste.profiles.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.giste.profiles.R

@Preview
@Composable
fun TextNotBlankDialogPreview() {
    TextNotBlankDialogScreen(
        title = "Title",
        cancelLabel = "Cancel",
        acceptLabel = "Accept",
        onDismiss = { },
        onAccept = { },
        label = "Label",
        initialText = ""
    )
}

@Composable
fun TextNotBlankDialogBody(
    title: String,
    cancelLabel: String = stringResource(R.string.dialog_cancel_label),
    acceptLabel: String = stringResource(R.string.dialog_accept_label),
    onDismiss: () -> Unit = {},
    onAccept: (String) -> Unit,
    label: String,
    text: String = ""
) {
    TextNotBlankDialogScreen(
        title = title,
        cancelLabel = cancelLabel,
        acceptLabel = acceptLabel,
        onDismiss = onDismiss,
        onAccept = onAccept,
        label = label,
        initialText = text
    )
}

@Composable
private fun TextNotBlankDialogScreen(
    title: String,
    cancelLabel: String,
    acceptLabel: String,
    onDismiss: () -> Unit,
    onAccept: (String) -> Unit,
    label: String,
    initialText: String
) {
    var text by remember { mutableStateOf(initialText) }

    DialogBody(
        title = title,
        cancelLabel = cancelLabel,
        acceptLabel = acceptLabel,
        onDismiss = onDismiss,
        onAccept = { onAccept(text) },
        acceptEnabled = { text != "" }
    ) {
        Content(label = label, text = text, onTextChange = { text = it })
    }
}

@Composable
private fun Content(
    label: String,
    text: String,
    onTextChange: (String) -> Unit
) {
    val textFieldContentDescription =
        stringResource(R.string.dialog_text_not_blank_text_field_content_description, label)
    val isError = text.isBlank()

    Column {
        TextField(
            value = text,
            onValueChange = onTextChange,
            label = { Text(label) },
            modifier = Modifier.semantics {
                contentDescription = textFieldContentDescription
                testTag = "TextField"
            },
            isError = isError
        )
        if(isError) {
            Text(
                text = stringResource(R.string.dialog_text_not_blank_error),
                modifier = Modifier.padding(start = 16.dp).semantics { testTag = "ErrorText" },
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption
            )
        }
    }
}