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
        onOverrideClick = { _, _ -> },
        onValueChange = { _, _ -> }
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
        profileViewModel::onValueChange
    )
}

@Composable
private fun ProfileContent(
    profile: ProfileDetail,
    onOverrideClick: (SettingType, Boolean) -> Unit,
    onValueChange: (SettingType, Any) -> Unit
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
                    min = 0,
                    max = 15,
                    onOverrideClick = { override -> onOverrideClick(it.type, override) },
                    onSliderChange = { value -> onValueChange(it.type, value) }
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
    valueSetting: (@Composable () -> Unit),
    onOverrideClick: (Boolean) -> Unit
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