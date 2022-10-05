package org.giste.profiles.domain

data class ProfileDetail(
    val id: Long = 0,
    val name: String = "",
    val settings: Map<SettingType, Setting<Any>> = SettingType.values().associate {
        when (it) {
            SettingType.VOLUME_MEDIA,
            SettingType.VOLUME_RING,
            SettingType.VOLUME_NOTIFICATION,
            SettingType.VOLUME_ALARM -> Pair(it, IntSetting(profileId = id, type = it))
            SettingType.RING_MODE -> Pair(it, RingModeSetting(profileId = id, type = it))
        }
    }
)
