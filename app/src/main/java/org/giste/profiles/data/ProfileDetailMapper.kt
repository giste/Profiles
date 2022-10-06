package org.giste.profiles.data

import org.giste.profiles.domain.ProfileDetail
import javax.inject.Inject

class ProfileDetailMapper @Inject constructor(
    private val settingMapper: SettingMapper
) {
    fun toModel(profileEntity: ProfileEntity, settingList: List<SettingEntity>): ProfileDetail {
        return ProfileDetail(
            id = profileEntity.id,
            name = profileEntity.name,
            settings = settingList.map(settingMapper::toModel).associateBy { it.type }
        )
    }

    fun toEntity(profileDetail: ProfileDetail): Pair<ProfileEntity, List<SettingEntity>> {
        val profileEntity = ProfileEntity(id = profileDetail.id, name = profileDetail.name)
        val settingEntityList = profileDetail.settings.values.map(settingMapper::toEntity)

        return Pair(profileEntity, settingEntityList)
    }
}