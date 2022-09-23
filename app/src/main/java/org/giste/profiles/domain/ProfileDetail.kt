package org.giste.profiles.domain

data class ProfileDetail(
    val id: Long = 0,
    val name: String = "",
    val mediaVolume: IntSetting = IntSetting(profileId = id, type = SettingType.VOLUME_MEDIA),
    val ringVolume: IntSetting = IntSetting(profileId = id, type = SettingType.VOLUME_RING),
    val notificationVolume: IntSetting = IntSetting(profileId = id, type = SettingType.VOLUME_NOTIFICATION),
    val alarmVolume: IntSetting = IntSetting(profileId = id, type = SettingType.VOLUME_ALARM)
)
