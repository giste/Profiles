package org.giste.profiles.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoNotDisturbOnTotalSilence
import androidx.compose.material.icons.filled.RingVolume
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import org.giste.profiles.R
import org.giste.profiles.domain.ProfileDetail
import org.giste.profiles.domain.RingModeSetting
import org.giste.profiles.domain.SettingType
import org.giste.profiles.domain.SystemProperties
import org.giste.profiles.ui.components.FabSettings
import org.giste.profiles.ui.components.TopBarSettings
import kotlin.math.roundToInt

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    ProfileContent(
        profile = ProfileDetail(name = "Profile Name"),
        onOverrideClick = { _, _ -> },
        onValueChange = { _, _ -> },
        systemProperties = object : SystemProperties {
            override val streamMediaMinValue: Int = 0
            override val streamMediaMaxValue: Int = 15
            override val streamRingMinValue: Int = 0
            override val streamRingMaxValue: Int = 7
            override val streamNotificationMinValue: Int = 0
            override val streamNotificationMaxValue: Int = 7
            override val streamAlarmMinValue: Int = 1
            override val streamAlarmMaxValue: Int = 7
        }
    )
}

@Destination(navArgsDelegate = ProfileScreenNavArgs::class)
@Composable
fun ProfileScreen(
    topBarSettings: TopBarSettings,
    fabSettings: FabSettings
) {
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val profileName = profileViewModel.profile.name

    LaunchedEffect(profileName) {
        topBarSettings.config(upVisible = true, title = profileName)
        fabSettings.config(visible = false)
    }

    ProfileContent(
        profileViewModel.profile,
        profileViewModel::onOverrideChange,
        profileViewModel::onValueChange,
        profileViewModel.systemProperties
    )
}

@Composable
private fun ProfileContent(
    profile: ProfileDetail,
    onOverrideClick: (SettingType, Boolean) -> Unit,
    onValueChange: (SettingType, Any) -> Unit,
    systemProperties: SystemProperties
) {
    Column(modifier = Modifier.padding(8.dp)) {
        ProfileName(
            name = profile.name
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Category(category = stringResource(id = R.string.profile_screen_category_volume_label))
            profile.settings[SettingType.VOLUME_MEDIA]?.let {
                SliderPreference(
                    label = stringResource(id = R.string.profile_screen_setting_volume_media_label),
                    override = it.override,
                    value = it.value as Int,
                    min = systemProperties.streamMediaMinValue,
                    max = systemProperties.streamMediaMaxValue,
                    onOverrideClick = { override -> onOverrideClick(it.type, override) },
                    onSliderChange = { value -> onValueChange(it.type, value) }
                )
            }
            profile.settings[SettingType.VOLUME_RING]?.let {
                SliderPreference(
                    label = stringResource(id = R.string.profile_screen_setting_volume_ring_label),
                    override = it.override,
                    value = it.value as Int,
                    min = systemProperties.streamRingMinValue,
                    max = systemProperties.streamRingMaxValue,
                    onOverrideClick = { override -> onOverrideClick(it.type, override) },
                    onSliderChange = { value -> onValueChange(it.type, value) }
                )
            }
            profile.settings[SettingType.VOLUME_NOTIFICATION]?.let {
                SliderPreference(
                    label = stringResource(id = R.string.profile_screen_setting_volume_notification_label),
                    override = it.override,
                    value = it.value as Int,
                    min = systemProperties.streamNotificationMinValue,
                    max = systemProperties.streamNotificationMaxValue,
                    onOverrideClick = { override -> onOverrideClick(it.type, override) },
                    onSliderChange = { value -> onValueChange(it.type, value) }
                )
            }
            profile.settings[SettingType.VOLUME_ALARM]?.let {
                SliderPreference(
                    label = stringResource(id = R.string.profile_screen_setting_volume_alarm_label),
                    override = it.override,
                    value = it.value as Int,
                    min = systemProperties.streamAlarmMinValue,
                    max = systemProperties.streamAlarmMaxValue,
                    onOverrideClick = { override -> onOverrideClick(it.type, override) },
                    onSliderChange = { value -> onValueChange(it.type, value) }
                )
            }
            profile.settings[SettingType.RING_MODE]?.let {
                RingModePreference(
                    label = stringResource(id = R.string.profile_screen_setting_volume_ring_mode_label),
                    override = it.override,
                    value = it.value as RingModeSetting.Companion.RingMode,
                    onOverrideClick = { override -> onOverrideClick(it.type, override) },
                    onSelectionChange = { value -> onValueChange(it.type, value) }
                )
            }

            Category(category = stringResource(id = R.string.profile_screen_category_connection_label))
            profile.settings[SettingType.CONNECTION_WIFI]?.let {
                BooleanPreference(
                    label = stringResource(id = R.string.profile_screen_setting_connection_wifi_label),
                    override = it.override,
                    value = it.value as Boolean,
                    onOverrideClick = { override -> onOverrideClick(it.type, override) },
                    onSelectionChange = { value -> onValueChange(it.type, value) }
                )
            }
            profile.settings[SettingType.CONNECTION_DATA]?.let {
                BooleanPreference(
                    label = stringResource(id = R.string.profile_screen_setting_connection_data_label),
                    override = it.override,
                    value = it.value as Boolean,
                    onOverrideClick = { override -> onOverrideClick(it.type, override) },
                    onSelectionChange = { value -> onValueChange(it.type, value) }
                )
            }
            profile.settings[SettingType.CONNECTION_BLUETOOTH]?.let {
                BooleanPreference(
                    label = stringResource(id = R.string.profile_screen_setting_connection_bluetooth_label),
                    override = it.override,
                    value = it.value as Boolean,
                    onOverrideClick = { override -> onOverrideClick(it.type, override) },
                    onSelectionChange = { value -> onValueChange(it.type, value) }
                )
            }
            profile.settings[SettingType.CONNECTION_NFC]?.let {
                BooleanPreference(
                    label = stringResource(id = R.string.profile_screen_setting_connection_nfc_label),
                    override = it.override,
                    value = it.value as Boolean,
                    onOverrideClick = { override -> onOverrideClick(it.type, override) },
                    onSelectionChange = { value -> onValueChange(it.type, value) }
                )
            }
            profile.settings[SettingType.CONNECTION_AIRPLANE]?.let {
                BooleanPreference(
                    label = stringResource(id = R.string.profile_screen_setting_connection_airplane_label),
                    override = it.override,
                    value = it.value as Boolean,
                    onOverrideClick = { override -> onOverrideClick(it.type, override) },
                    onSelectionChange = { value -> onValueChange(it.type, value) }
                )
            }
        }
    }

}

@Composable
private fun ProfileName(
    name: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = name,
            style = MaterialTheme.typography.h6
        )
    }
}

@Composable
private fun Category(category: String) {
    Row {
        Text(
            text = category,
            fontWeight = FontWeight.Bold
        )
    }
    Row {
        Divider()
    }
}

@Composable
fun Preference(
    override: Boolean,
    label: String,
    onOverrideClick: (Boolean) -> Unit,
    valueSetting: (@Composable () -> Unit),
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(
            modifier = Modifier
                .weight(2f)
                .clickable { onOverrideClick(!override) }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = override,
                    onCheckedChange = { onOverrideClick(it) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = label)
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
        Column(modifier = Modifier.weight(4f)) {
            valueSetting()
        }
    }
}

@Composable
fun SliderPreference(
    label: String,
    override: Boolean,
    value: Int,
    min: Int,
    max: Int,
    onOverrideClick: (Boolean) -> Unit,
    onSliderChange: (Int) -> Unit
) {
    var lastValue by remember { mutableStateOf(value) }
    var selection by remember { mutableStateOf(0) }

    if (value != lastValue) {
        lastValue = value
        selection = lastValue
    }

    Preference(
        override = override,
        label = label,
        onOverrideClick = onOverrideClick,
        valueSetting = {
            Slider(
                value = selection.toFloat(),
                onValueChange = { selection = it.roundToInt() },
                enabled = override,
                valueRange = min.toFloat().rangeTo(max.toFloat()),
                onValueChangeFinished = { onSliderChange(selection) }
            )
        }
    )
}

@Composable
private fun RingModePreference(
    label: String,
    override: Boolean,
    value: RingModeSetting.Companion.RingMode,
    onOverrideClick: (Boolean) -> Unit,
    onSelectionChange: (RingModeSetting.Companion.RingMode) -> Unit
) {
    Preference(
        override = override,
        label = label,
        onOverrideClick = onOverrideClick
    ) {
        Row(
            modifier = Modifier
                .selectableGroup()
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val tint = if (override) {
                LocalContentColor.current
            } else {
                LocalContentColor.current.copy(alpha = ContentAlpha.disabled)
            }

            RadioButton(
                selected = value == RingModeSetting.Companion.RingMode.NORMAL,
                onClick = { onSelectionChange(RingModeSetting.Companion.RingMode.NORMAL) },
                enabled = override
            )
            Icon(
                imageVector = Icons.Default.RingVolume,
                contentDescription = "",
                tint = tint
            )

            Spacer(modifier = Modifier.width(16.dp))

            RadioButton(
                selected = value == RingModeSetting.Companion.RingMode.VIBRATE,
                onClick = { onSelectionChange(RingModeSetting.Companion.RingMode.VIBRATE) },
                enabled = override
            )
            Icon(
                imageVector = Icons.Default.Vibration,
                contentDescription = "",
                tint = tint
            )

            Spacer(modifier = Modifier.width(16.dp))

            RadioButton(
                selected = value == RingModeSetting.Companion.RingMode.SILENT,
                onClick = { onSelectionChange(RingModeSetting.Companion.RingMode.SILENT) },
                enabled = override
            )
            Icon(
                imageVector = Icons.Default.DoNotDisturbOnTotalSilence,
                contentDescription = "",
                tint = tint
            )

        }
    }
}

@Composable
private fun BooleanPreference(
    label: String,
    override: Boolean,
    value: Boolean,
    onOverrideClick: (Boolean) -> Unit,
    onSelectionChange: (Boolean) -> Unit
) {
    Preference(
        override = override,
        label = label,
        onOverrideClick = onOverrideClick
    ) {
        Switch(
            checked = value,
            onCheckedChange = { onSelectionChange(it) },
            enabled = override
        )
    }
}
