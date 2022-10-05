package org.giste.profiles.data

import org.giste.profiles.domain.IntSetting
import org.giste.profiles.domain.RingModeSetting
import org.giste.profiles.domain.Setting
import org.giste.profiles.domain.SettingType
import javax.inject.Inject

class SettingMapper @Inject constructor() {
    fun toEntity(setting: Setting<Any>): SettingEntity {
        with(setting) {
            return SettingEntity(
                profileId = profileId,
                type = type,
                override = override,
                value = when(this) {
                    is IntSetting -> value
                    is RingModeSetting -> value.ordinal
                }
            )
        }
    }

    fun toModel(settingEntity: SettingEntity): Setting<Any> {
        with(settingEntity) {
            return when (type) {
                SettingType.VOLUME_MEDIA,
                SettingType.VOLUME_RING,
                SettingType.VOLUME_NOTIFICATION,
                SettingType.VOLUME_ALARM -> IntSetting(profileId, type, override, value)
                SettingType.RING_MODE -> RingModeSetting(profileId, type, override,
                    RingModeSetting.Companion.RingMode.values()[value]
                )
            }
        }
    }
}