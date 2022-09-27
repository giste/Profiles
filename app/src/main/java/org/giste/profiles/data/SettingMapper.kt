package org.giste.profiles.data

import org.giste.profiles.domain.*
import javax.inject.Inject

class SettingMapper @Inject constructor() {
    fun toEntity(setting: Setting<Any>): SettingEntity {
        with(setting) {
            return SettingEntity(
                id = id,
                profileId = profileId,
                type = type,
                override = override,
                value = when (this) {
                    is IntSetting -> value
                    is RingModeSetting -> value.ringMode
                }
            )
        }
    }

    fun toEntity(settingList: Map<SettingType, Setting<Any>>): List<SettingEntity> {
        return settingList.values.map { toEntity(it) }
    }

    fun toModel(settingEntity: SettingEntity): Setting<Any> {
        with(settingEntity) {
            return when (type) {
                SettingType.VOLUME_MEDIA,
                SettingType.VOLUME_RING,
                SettingType.VOLUME_NOTIFICATION,
                SettingType.VOLUME_ALARM -> IntSetting(id, profileId, type, override, value)
                SettingType.RING_MODE -> RingModeSetting(
                    id,
                    profileId,
                    type,
                    override,
                    RingMode.getRingMode(value)
                )
            }
        }
    }
}