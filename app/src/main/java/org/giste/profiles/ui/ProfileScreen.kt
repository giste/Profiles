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
import org.giste.profiles.domain.RingMode
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
        onOverrideChange = { _, _ -> },
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
        },
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
        profile = profileViewModel.profile,
        onOverrideChange = profileViewModel::onOverrideChange,
        onValueChange = profileViewModel::onValueChange,
        profileViewModel.systemProperties,
    )
}

@Composable
private fun ProfileContent(
    profile: ProfileDetail,
    onOverrideChange: (SettingType, Boolean) -> Unit,
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
            with(profile.mediaVolume) {
                SliderSetting(
                    label = stringResource(id = R.string.profile_screen_setting_volume_media_label),
                    override = override,
                    value = value,
                    min = systemProperties.streamMediaMinValue,
                    max = systemProperties.streamMediaMaxValue,
                    onOverrideChange = { onOverrideChange(type, it) },
                    onSliderChange = { onValueChange(type, it) }
                )
            }
            with(profile.ringVolume) {
                SliderSetting(
                    label = stringResource(id = R.string.profile_screen_setting_volume_ring_label),
                    override = override,
                    value = value,
                    min = systemProperties.streamRingMinValue,
                    max = systemProperties.streamRingMaxValue,
                    onOverrideChange = { onOverrideChange(type, it) },
                    onSliderChange = { onValueChange(type, it) }
                )
            }
            with(profile.notificationVolume) {
                SliderSetting(
                    label = stringResource(id = R.string.profile_screen_setting_volume_notification_label),
                    override = override,
                    value = value,
                    min = systemProperties.streamNotificationMinValue,
                    max = systemProperties.streamNotificationMaxValue,
                    onOverrideChange = { onOverrideChange(type, it) },
                    onSliderChange = { onValueChange(type, it) }
                )
            }
            with(profile.alarmVolume) {
                SliderSetting(
                    label = stringResource(id = R.string.profile_screen_setting_volume_alarm_label),
                    override = override,
                    value = value,
                    min = systemProperties.streamAlarmMinValue,
                    max = systemProperties.streamAlarmMaxValue,
                    onOverrideChange = { onOverrideChange(type, it) },
                    onSliderChange = { onValueChange(type, it) }
                )
            }
            with(profile.ringMode) {
                RingModeSetting(
                    label = stringResource(id = R.string.profile_screen_setting_volume_ring_mode_label),
                    override = override,
                    value = value,
                    onOverrideChange = { onOverrideChange(type, it) },
                    onSelectionChange = { onValueChange(type, it.ringMode) }
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
fun Setting(
    override: Boolean,
    label: String,
    onOverrideChange: (Boolean) -> Unit,
    valueContent: (@Composable () -> Unit)
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(
            modifier = Modifier
                .weight(2f)
                .clickable { onOverrideChange(!override) }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = override,
                    onCheckedChange = { onOverrideChange(it) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = label)
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
        Column(modifier = Modifier.weight(4f)) {
            valueContent()
        }
    }
}

@Composable
fun SliderSetting(
    label: String,
    override: Boolean,
    value: Int,
    min: Int,
    max: Int,
    onOverrideChange: (Boolean) -> Unit,
    onSliderChange: (Int) -> Unit
) {
    Setting(
        override = override,
        label = label,
        onOverrideChange = onOverrideChange
    ) {
        var lastValue by remember { mutableStateOf(value) }
        var selection by remember { mutableStateOf(0) }

        if (value != lastValue) {
            lastValue = value
            selection = lastValue
        }

        Slider(
            value = selection.toFloat(),
            onValueChange = { selection = it.roundToInt() },
            enabled = override,
            valueRange = min.toFloat().rangeTo(max.toFloat()),
            onValueChangeFinished = { onSliderChange(selection) }
        )
    }
}

@Composable
private fun RingModeSetting(
    label: String,
    override: Boolean,
    value: RingMode,
    onOverrideChange: (Boolean) -> Unit,
    onSelectionChange: (RingMode) -> Unit
) {
    Setting(
        override = override,
        label = label,
        onOverrideChange = onOverrideChange
    ) {
        Row(
            modifier = Modifier.selectableGroup(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = value == RingMode.NORMAL,
                onClick = { onSelectionChange(RingMode.NORMAL) },
                enabled = override
            )
            Icon(imageVector = Icons.Default.RingVolume, contentDescription = "")
            RadioButton(
                selected = value == RingMode.VIBRATE,
                onClick = { onSelectionChange(RingMode.VIBRATE) },
                enabled = override
            )
            Icon(imageVector = Icons.Default.Vibration, contentDescription = "")
            RadioButton(
                selected = value == RingMode.SILENT,
                onClick = { onSelectionChange(RingMode.SILENT) },
                enabled = override
            )
            Icon(imageVector = Icons.Default.DoNotDisturbOnTotalSilence, contentDescription = "")
        }
    }
}
