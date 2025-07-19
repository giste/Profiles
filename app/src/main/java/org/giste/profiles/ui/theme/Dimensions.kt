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

package org.giste.profiles.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass.Companion.COMPACT
import androidx.window.core.layout.WindowWidthSizeClass.Companion.EXPANDED
import androidx.window.core.layout.WindowWidthSizeClass.Companion.MEDIUM

@Immutable
data class Dimensions(
    val padding: Dp,
    val spacing: Dp,
)

@Composable
fun dimensions(windowSizeClass: WindowSizeClass) = when (windowSizeClass.windowWidthSizeClass) {
    EXPANDED -> expandedDimensions
    MEDIUM -> mediumDimensions
    COMPACT -> compactDimensions
    else -> defaultDimensions()
}

fun defaultDimensions() = compactDimensions

private val compactDimensions = Dimensions(
    padding = 4.dp,
    spacing = 8.dp,
)

private val mediumDimensions = Dimensions(
    padding = 6.dp,
    spacing = 12.dp,
)

private val expandedDimensions = Dimensions(
    padding = 8.dp,
    spacing = 16.dp,
)