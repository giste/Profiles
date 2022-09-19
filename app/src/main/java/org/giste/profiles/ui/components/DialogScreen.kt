package org.giste.profiles.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.giste.profiles.R

@Preview(showBackground = true)
@Composable
fun DialogPreview() {
    DialogContent(
        title = "Title",
        cancelLabel = "Cancel",
        acceptLabel = "Accept",
        onDismiss = {},
        onAccept = {},
        acceptEnabled = true
    ) {
        Text(text = "Content")
    }
}

@Composable
fun DialogScreen(
    title: String,
    cancelLabel: String = stringResource(R.string.dialog_cancel_label),
    acceptLabel: String = stringResource(R.string.dialog_accept_label),
    onDismiss: () -> Unit = {},
    onAccept: () -> Unit,
    acceptEnabled: () -> Boolean = { true },
    content: @Composable () -> Unit
) {
    DialogContent(
        title = title,
        cancelLabel = cancelLabel,
        acceptLabel = acceptLabel,
        onDismiss = onDismiss,
        onAccept = onAccept,
        acceptEnabled = acceptEnabled()
    ) {
        content()
    }
}

@Composable
private fun DialogContent(
    title: String,
    cancelLabel: String,
    acceptLabel: String,
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
    acceptEnabled: Boolean,
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .semantics { testTag = "Root" }
            ) {
                DialogTitle(title = title)
                Divider(thickness = 1.dp)
                Spacer(modifier = Modifier.height(8.dp))
                DialogContent(content)
                Spacer(modifier = Modifier.height(8.dp))
                DialogButtons(
                    cancelLabel = cancelLabel,
                    acceptLabel = acceptLabel,
                    onDismiss = onDismiss,
                    onAccept = onAccept,
                    acceptEnabled = acceptEnabled
                )
            }
        }
    }
}

@Composable
private fun DialogTitle(title: String) {
    val titleContentDescription = stringResource(R.string.dialog_title_content_description)

    Row {
        Text(
            text = title,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.semantics {
                contentDescription = titleContentDescription
                testTag = "Title"
            }
        )
    }
}

@Composable
private fun DialogContent(content: @Composable () -> Unit) {
    Row {
        content()
    }
}

@Composable
private fun DialogButtons(
    cancelLabel: String,
    acceptLabel: String,
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
    acceptEnabled: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(1f),
        horizontalArrangement = Arrangement.Center
    ) {
        val cancelContentDescription =
            stringResource(R.string.dialog_cancel_button_content_description)
        val acceptContentDescription =
            stringResource(R.string.dialog_accept_button_content_description)

        Button(
            onClick = onDismiss,
            modifier = Modifier.semantics {
                contentDescription = cancelContentDescription
                testTag = "CancelButton"
            }
        ) {
            Text(text = cancelLabel)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = onAccept,
            modifier = Modifier.semantics {
                contentDescription = acceptContentDescription
                testTag = "AcceptButton"
            },
            enabled = acceptEnabled
        ) {
            Text(text = acceptLabel)
        }
    }
}

