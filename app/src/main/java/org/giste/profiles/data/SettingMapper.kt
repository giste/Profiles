package org.giste.profiles.data

import org.giste.profiles.domain.*
import javax.inject.Inject

class SettingMapper @Inject constructor() {
    fun toEntity(setting: Setting): SettingEntity {
        with(setting) {
            return SettingEntity(
                profileId = profileId,
                type = type,
                override = override,
                value = when (this) {
                    is IntSetting -> value
                    is RingModeSetting -> value.ordinal
                    is BooleanSetting -> if (value) 1 else 0
                }
            )
        }
    }

    fun toModel(settingEntity: SettingEntity): Setting {
        with(settingEntity) {
            return when (type) {
                SettingType.VOLUME_MEDIA,
                SettingType.VOLUME_RING,
                SettingType.VOLUME_NOTIFICATION,
                SettingType.VOLUME_ALARM,
                SettingType.BRIGHTNESS -> IntSetting(profileId, type, override, value)
                SettingType.RING_MODE ->
                    RingModeSetting(
                        profileId,
                        type,
                        override,
                        RingModeSetting.Companion.RingMode.entries[value]
                    )
                SettingType.BRIGHTNESS_AUTO ->
                    BooleanSetting(profileId, type, override, value == 1)
            }
        }
    }
}