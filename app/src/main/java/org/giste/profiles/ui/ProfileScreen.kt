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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import org.giste.profiles.R
import org.giste.profiles.domain.IntSetting
import org.giste.profiles.domain.Profile
import org.giste.profiles.domain.RingModeSetting
import org.giste.profiles.ui.theme.ProfilesTheme
import kotlin.math.roundToInt

@Preview
@Composable
fun ProfilePreview() {
    ProfilesTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            ProfileScreen(
                uiState = ProfileViewModel.UiState(
                    profile = Profile(
                        name = "Profile name",
                        mediaVolume = IntSetting(true, 15),
                        ringVolume = IntSetting(false, 5),
                        brightness = IntSetting(false, 100),
                    ),
                ),
                onValueChange = {}
            )
        }
    }
}

@Composable
fun ProfileScreen(
    uiState: ProfileViewModel.UiState,
    onValueChange: (profile: Profile) -> Unit,
) {
    val profile = uiState.profile
    val systemProperties = uiState.systemProperties

    ProvideTopBarTitle {
        Text(profile.name)
    }

    ProvideFab { }

    Column(modifier = Modifier.padding(ProfilesTheme.dimensions.padding)) {
        // Volume
        Category(category = stringResource(id = R.string.profile_screen_category_volume_label))
        // Media
        with (profile.mediaVolume) {
            SliderPreference(
                label = stringResource(id = R.string.profile_screen_setting_volume_media_label),
                iconResource = ImageVector.vectorResource(R.drawable.volume_media),
                apply = apply,
                value = value,
                min = systemProperties.streamMediaMinValue,
                max = systemProperties.streamMediaMaxValue,
                onApplyClick = { apply ->
                    onValueChange(profile.copy(mediaVolume = copy(apply = apply)))
                },
                onSliderChange = { value ->
                    onValueChange(profile.copy(mediaVolume = copy(value = value)))
                }
            )
        }
        // Ring
        with (profile.ringVolume) {
            SliderPreference(
                label = stringResource(id = R.string.profile_screen_setting_volume_ring_label),
                iconResource = ImageVector.vectorResource(R.drawable.volume_ring),
                apply = apply,
                value = value,
                min = systemProperties.streamRingMinValue,
                max = systemProperties.streamRingMaxValue,
                onApplyClick = { apply ->
                    onValueChange(profile.copy(ringVolume = copy(apply = apply)))
                },
                onSliderChange = { value ->
                    onValueChange(profile.copy(ringVolume = copy(value = value)))
                }
            )
        }
        // Notification
        with (profile.notificationVolume) {
            SliderPreference(
                label = stringResource(id = R.string.profile_screen_setting_volume_notification_label),
                iconResource = ImageVector.vectorResource(R.drawable.volume_notification),
                apply = apply,
                value = value,
                min = systemProperties.streamNotificationMinValue,
                max = systemProperties.streamNotificationMaxValue,
                onApplyClick = { apply ->
                    onValueChange(profile.copy(notificationVolume = copy(apply = apply)))
                },
                onSliderChange = { value ->
                    onValueChange(profile.copy(notificationVolume = copy(value = value)))
                }
            )
        }
        // Alarm
        with (profile.alarmVolume) {
            SliderPreference(
                label = stringResource(id = R.string.profile_screen_setting_volume_alarm_label),
                iconResource = ImageVector.vectorResource(R.drawable.volume_alarm),
                apply = apply,
                value = value,
                min = systemProperties.streamAlarmMinValue,
                max = systemProperties.streamAlarmMaxValue,
                onApplyClick = { apply ->
                    onValueChange(profile.copy(alarmVolume = copy(apply = apply)))
                },
                onSliderChange = { value ->
                    onValueChange(profile.copy(alarmVolume = copy(value = value)))
                }
            )
        }
        // Ring Mode
        with (profile.ringMode) {
            RingModePreference(
                label = stringResource(id = R.string.profile_screen_setting_volume_ring_mode_label),
                iconResource = ImageVector.vectorResource(R.drawable.ring_mode),
                apply = apply,
                value = value,
                onApplyClick = { apply ->
                    onValueChange(profile.copy(ringMode = copy(apply = apply)))
                },
                onSelectionChange = { value ->
                    onValueChange(profile.copy(ringMode = copy(value = value)))
                }
            )
        }

        // Brightness
        Category(category = stringResource(id = R.string.profile_screen_category_brightness_label))
        // Auto
        with (profile.autoBrightness) {
            BooleanPreference(
                label = stringResource(id = R.string.profile_screen_setting_brightness_auto_label),
                iconResource = ImageVector.vectorResource(R.drawable.brightness_auto),
                apply = apply,
                value = value,
                onApplyClick = { apply ->
                    onValueChange(profile.copy(autoBrightness = copy(apply = apply)))
                },
                onSelectionChange = { value ->
                    onValueChange(profile.copy(autoBrightness = copy(value = value)))
                }
            )
        }
        // Brightness level
        with (profile.brightness) {
            SliderPreference(
                label = stringResource(id = R.string.profile_screen_setting_brightness_level_label),
                iconResource = ImageVector.vectorResource(R.drawable.brightness_level),
                apply = apply,
                value = value,
                min = 0,
                max = 255,
                onApplyClick = { apply ->
                    onValueChange(profile.copy(brightness = copy(apply = apply)))
                },
                onSliderChange = { value ->
                    onValueChange(profile.copy(brightness = copy(value = value)))
                }
            )
        }
    }
}

@Composable
private fun Category(category: String) {
    Row(
        modifier = Modifier.padding(top = ProfilesTheme.dimensions.spacing)
    ) {
        Text(
            text = category,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.headlineSmall
        )
    }
    Row {
        HorizontalDivider()
    }
}

@Composable
fun SliderPreference(
    label: String,
    iconResource: ImageVector,
    apply: Boolean,
    value: Int,
    min: Int,
    max: Int,
    onApplyClick: (Boolean) -> Unit,
    onSliderChange: (Int) -> Unit
) {
    var lastValue by remember { mutableIntStateOf(0) }
    var selection by remember { mutableIntStateOf(0) }

    if (value != lastValue) {
        lastValue = value
        selection = lastValue
    }

    Preference(
        iconResource = iconResource,
        label = label,
        apply = apply,
        onApplyChange = onApplyClick,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Slider(
                value = selection.toFloat(),
                onValueChange = { selection = it.roundToInt() },
                modifier = Modifier.weight(8f),
                enabled = apply,
                valueRange = min.toFloat().rangeTo(max.toFloat()),
                onValueChangeFinished = { onSliderChange(selection) }
            )
            Spacer(Modifier.width(ProfilesTheme.dimensions.spacing))
            Text(
                text = "$selection/$max",
                modifier = Modifier
                    .alpha(if (apply) 1f else ProfilesTheme.ALPHA_DISABLED)
                    .weight(2f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun BooleanPreference(
    label: String,
    iconResource: ImageVector,
    apply: Boolean,
    value: Boolean,
    onApplyClick: (Boolean) -> Unit,
    onSelectionChange: (Boolean) -> Unit
) {
    Preference(
        iconResource = iconResource,
        label = label,
        apply = apply,
        onApplyChange = onApplyClick,
        {
            Switch(
                checked = value,
                onCheckedChange = { onSelectionChange(it) },
                enabled = apply
            )
        }
    )
}

@Composable
private fun RingModePreference(
    label: String,
    iconResource: ImageVector,
    apply: Boolean,
    value: RingModeSetting.Companion.RingMode,
    onApplyClick: (Boolean) -> Unit,
    onSelectionChange: (RingModeSetting.Companion.RingMode) -> Unit
) {
    Preference(
        iconResource = iconResource,
        label = label,
        apply = apply,
        onApplyChange = onApplyClick,
        {
            Row(
                modifier = Modifier
                    .selectableGroup()
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                val tint = if (apply) {
                    LocalContentColor.current
                } else {
                    LocalContentColor.current.copy(alpha = ProfilesTheme.ALPHA_DISABLED)
                }

                Column(horizontalAlignment = Alignment.Start) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = value == RingModeSetting.Companion.RingMode.NORMAL,
                            onClick = { onSelectionChange(RingModeSetting.Companion.RingMode.NORMAL) },
                            enabled = apply
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ring_mode_normal),
                            contentDescription = "",
                            tint = tint
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = value == RingModeSetting.Companion.RingMode.VIBRATE,
                            onClick = { onSelectionChange(RingModeSetting.Companion.RingMode.VIBRATE) },
                            enabled = apply
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ring_mode_vibrate),
                            contentDescription = "",
                            tint = tint
                        )
                    }
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = value == RingModeSetting.Companion.RingMode.SILENT,
                            onClick = { onSelectionChange(RingModeSetting.Companion.RingMode.SILENT) },
                            enabled = apply
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ring_mode_silence),
                            contentDescription = "",
                            tint = tint
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun Preference(
    iconResource: ImageVector,
    label: String,
    apply: Boolean,
    onApplyChange: (Boolean) -> Unit,
    content: (@Composable () -> Unit),
) {
    Spacer(Modifier.height(ProfilesTheme.dimensions.spacing))
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = iconResource,
                contentDescription = "",
                modifier = Modifier.padding(ProfilesTheme.dimensions.padding)
            )
            Spacer(Modifier.width(ProfilesTheme.dimensions.spacing))
            Text(text = label)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(checked = apply, onCheckedChange = { onApplyChange(it) })
            Spacer(Modifier.width(ProfilesTheme.dimensions.spacing))
            content()
        }
    }
}
