package org.giste.profiles.domain

data class ProfileDetail(
    val id: Long = 0,
    val name: String = "",
    val settings: Map<SettingType, Setting> = SettingType.values().associate {
        when (it) {
            SettingType.VOLUME_MEDIA,
            SettingType.VOLUME_RING,
            SettingType.VOLUME_NOTIFICATION,
            SettingType.VOLUME_ALARM,
            SettingType.BRIGHTNESS -> Pair(it, IntSetting(profileId = id, type = it))
            SettingType.RING_MODE -> Pair(it, RingModeSetting(profileId = id, type = it))
            SettingType.BRIGHTNESS_AUTO -> Pair(it, BooleanSetting(profileId = id, type = it))
        }
    }
)
