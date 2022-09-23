package org.giste.profiles.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
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
import org.giste.profiles.domain.SettingType
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
        minMediaVolume = 0,
        maxMediaVolume = 15,
        minRingVolume = 0,
        maxRingVolume = 15,
        minNotificationVolume = 0,
        maxNotificationVolume = 15,
        minAlarmVolume = 1,
        maxAlarmVolume = 15,
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
        minMediaVolume = profileViewModel.minMediaVolume,
        maxMediaVolume = profileViewModel.maxMediaVolume,
        minRingVolume = profileViewModel.minRingVolume,
        maxRingVolume = profileViewModel.maxRingVolume,
        minNotificationVolume = profileViewModel.minNotificationVolume,
        maxNotificationVolume = profileViewModel.maxNotificationVolume,
        minAlarmVolume = profileViewModel.minAlarmVolume,
        maxAlarmVolume = profileViewModel.maxAlarmVolume,
    )
}

@Composable
private fun ProfileContent(
    profile: ProfileDetail,
    onOverrideChange: (SettingType, Boolean) -> Unit,
    onValueChange: (SettingType, Any) -> Unit,
    minMediaVolume: Int,
    maxMediaVolume: Int,
    minRingVolume: Int,
    maxRingVolume: Int,
    minNotificationVolume: Int,
    maxNotificationVolume: Int,
    minAlarmVolume: Int,
    maxAlarmVolume: Int,
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
                    initialValue = value,
                    min = minMediaVolume,
                    max = maxMediaVolume,
                    onOverrideChange = { onOverrideChange(type, it) },
                    onSliderChange = { onValueChange(type, it.roundToInt()) }
                )
            }
            with(profile.ringVolume) {
                SliderSetting(
                    label = stringResource(id = R.string.profile_screen_setting_volume_ring_label),
                    override = override,
                    initialValue = value,
                    min = minRingVolume,
                    max = maxRingVolume,
                    onOverrideChange = { onOverrideChange(type, it) },
                    onSliderChange = { onValueChange(type, it.roundToInt()) }
                )
            }
            with(profile.notificationVolume) {
                SliderSetting(
                    label = stringResource(id = R.string.profile_screen_setting_volume_notification_label),
                    override = override,
                    initialValue = value,
                    min = minNotificationVolume,
                    max = maxNotificationVolume,
                    onOverrideChange = { onOverrideChange(type, it) },
                    onSliderChange = { onValueChange(type, it.roundToInt()) }
                )
            }
            with(profile.alarmVolume) {
                SliderSetting(
                    label = stringResource(id = R.string.profile_screen_setting_volume_alarm_label),
                    override = override,
                    initialValue = value,
                    min = minAlarmVolume,
                    max = maxAlarmVolume,
                    onOverrideChange = { onOverrideChange(type, it) },
                    onSliderChange = { onValueChange(type, it.roundToInt()) }
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
    valueContent: (@Composable () -> Unit),
    onOverrideChange: (Boolean) -> Unit
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
    initialValue: Int,
    min: Int,
    max: Int,
    onOverrideChange: (Boolean) -> Unit,
    onSliderChange: (Float) -> Unit
) {
    Setting(
        override = override,
        label = label,
        onOverrideChange = onOverrideChange,
        valueContent = {
            var value by remember { mutableStateOf(initialValue) }
            Slider(
                value = value.toFloat(),
                onValueChange = { value = it.roundToInt() },
                enabled = override,
                valueRange = min.toFloat().rangeTo(max.toFloat()),
                onValueChangeFinished = { onSliderChange(value.toFloat()) }
            )
        }
    )
}