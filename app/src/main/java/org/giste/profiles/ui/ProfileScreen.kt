package org.giste.profiles.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    ProfileContent(
        ProfileDetail(name = "Profile Name")
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
    )
}

@Composable
private fun ProfileContent(
    profile: ProfileDetail,
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
                    onOverrideClick = {  },
                    onSliderChange = {}
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
    onSliderChange: (Float) -> Unit
) {
    Preference(
        override = override,
        label = label,
        onOverrideClick = onOverrideClick,
        valueSetting = {
            Slider(
                value = value.toFloat(),
                onValueChange = onSliderChange,
                enabled = override,
                valueRange = min.toFloat().rangeTo(max.toFloat()),
                //steps = max - min
            )
        }
    )
}