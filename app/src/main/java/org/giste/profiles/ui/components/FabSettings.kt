package org.giste.profiles.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.graphics.vector.ImageVector

interface FabSettings {
    fun config(
        visible: Boolean = false,
        icon: ImageVector = Icons.Default.Add,
        contentDescription: String = "",
        onClick: () -> Unit = {}
    )
}