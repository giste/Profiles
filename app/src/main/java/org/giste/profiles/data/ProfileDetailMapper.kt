package org.giste.profiles.data

import android.util.Log
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
            settings = settingMap
        )
    }

    fun toEntity(profileDetail: ProfileDetail): Pair<ProfileEntity, List<SettingEntity>> {
        val profileEntity = ProfileEntity(id = profileDetail.id, name = profileDetail.name)
        val settingEntityList = profileDetail.settings.values.map { settingMapper.toEntity(it) }

        Log.d("ProfileDetailMapper", "toEntity = ($profileEntity, $settingEntityList)")

        return Pair(profileEntity, settingEntityList)
    }
}