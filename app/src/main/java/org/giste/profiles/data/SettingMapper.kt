package org.giste.profiles.data

import org.giste.profiles.domain.Setting
import org.giste.profiles.domain.SettingType
import org.giste.profiles.domain.IntSetting
import javax.inject.Inject

class SettingMapper @Inject constructor() {
    fun toEntity(setting: Setting<Any>): SettingEntity {
        with(setting) {
            when (this) {
                is IntSetting -> return SettingEntity(
                    id = id,
                    profileId = profileId,
                    type = type,
                    override = override,
                    value = value
                )
            }
        }
    }

    fun toEntity(settingList: Map<SettingType, Setting<Any>>): List<SettingEntity> {
        return settingList.values.map { toEntity(it) }
    }

    fun toModel(settingEntity: SettingEntity): Setting<Any> {
        with(settingEntity) {
            when (type) {
                SettingType.VOLUME_MEDIA,
                SettingType.VOLUME_RING,
                SettingType.VOLUME_NOTIFICATION,
                SettingType.VOLUME_ALARM -> return IntSetting(
                    id = id,
                    profileId = profileId,
                    override = override,
                    value = value,
                    type = type
                )
                else -> {
                    throw RuntimeException("Not implemented!")
                }
            }
        }
    }

    fun toModel(settingEntityList: List<SettingEntity>): Map<SettingType, Setting<Any>> {
        val settingMap = mutableMapOf<SettingType, Setting<Any>>()

        settingEntityList.forEach {
            settingMap[it.type] = toModel(it)
        }

        return settingMap.toMap()
    }
}