package org.giste.profiles.domain

data class ProfileDetail(
    val id: Long = 0,
    val name: String = "",
    val settings: Map<SettingType, Setting<Any>> = mapOf(
        SettingType.VOLUME_MEDIA to IntSetting(
            profileId = id,
            type = SettingType.VOLUME_MEDIA
        ),
        SettingType.VOLUME_RING to IntSetting(
            profileId = id,
            type = SettingType.VOLUME_RING
        ),
        SettingType.VOLUME_NOTIFICATION to IntSetting(
            profileId = id,
            type = SettingType.VOLUME_NOTIFICATION
        ),
        SettingType.VOLUME_ALARM to IntSetting(
            profileId = id,
            type = SettingType.VOLUME_ALARM
        ),
    )
)
