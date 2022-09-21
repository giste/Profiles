package org.giste.profiles.data

import org.giste.profiles.domain.Setting
import org.giste.profiles.domain.SettingType
import org.giste.profiles.domain.VolumeSetting
import org.giste.profiles.domain.VolumeType
import javax.inject.Inject

class SettingMapper @Inject constructor() {
    fun toEntity(setting: Setting<Any>): SettingEntity {
        with(setting) {
            when (this) {
                is VolumeSetting -> return SettingEntity(
                    id = id,
                    profileId = profileId,
                    type = when (type) {
                        VolumeType.MEDIA -> SettingType.VOLUME_MEDIA
                        VolumeType.RING -> SettingType.VOLUME_RING
                        VolumeType.NOTIFICATION -> SettingType.VOLUME_NOTIFICATION
                        VolumeType.ALARM -> SettingType.VOLUME_ALARM
                    },
                    override = override,
                    value = value
                )
            }
        }
    }

    fun toModel(settingEntity: SettingEntity): Setting<Any> {
        with(settingEntity) {
            when (type) {
                SettingType.VOLUME_MEDIA -> return VolumeSetting(
                    id = id,
                    profileId = profileId,
                    override = override,
                    value = value,
                    type = VolumeType.MEDIA
                )
                SettingType.VOLUME_RING -> return VolumeSetting(
                    id = id,
                    profileId = profileId,
                    override = override,
                    value = value,
                    type = VolumeType.RING
                )
                SettingType.VOLUME_NOTIFICATION -> return VolumeSetting(
                    id = id,
                    profileId = profileId,
                    override = override,
                    value = value,
                    type = VolumeType.NOTIFICATION
                )
                SettingType.VOLUME_ALARM -> return VolumeSetting(
                    id = id,
                    profileId = profileId,
                    override = override,
                    value = value,
                    type = VolumeType.ALARM
                )
                else -> {
                    throw RuntimeException("Not implemented!")
                }
            }
        }
    }


}