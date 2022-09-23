package org.giste.profiles.data

import org.giste.profiles.domain.IntSetting
import org.giste.profiles.domain.ProfileDetail
import org.giste.profiles.domain.Setting
import org.giste.profiles.domain.SettingType
import javax.inject.Inject

class ProfileDetailMapper @Inject constructor(
    private val settingMapper: SettingMapper
) {
    fun toModel(profileEntity: ProfileEntity, settingList: List<SettingEntity>): ProfileDetail {
        val settingEntityMap = settingList.associateBy { it.type }

        val settingMap: Map<SettingType, Setting<Any>> =
            SettingType.values().associateWith {
                settingEntityMap[it]?.let { setting -> settingMapper.toModel(setting) }
                    ?: settingMapper.toModel(SettingEntity(profileId = profileEntity.id, type = it))
            }

        return ProfileDetail(
            id = profileEntity.id,
            name = profileEntity.name,
            mediaVolume = settingMap[SettingType.VOLUME_MEDIA] as IntSetting,
            ringVolume = settingMap[SettingType.VOLUME_RING] as IntSetting,
            notificationVolume = settingMap[SettingType.VOLUME_NOTIFICATION] as IntSetting,
            alarmVolume = settingMap[SettingType.VOLUME_ALARM] as IntSetting
        )
    }
}