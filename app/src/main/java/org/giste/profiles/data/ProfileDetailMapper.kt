package org.giste.profiles.data

import org.giste.profiles.domain.ProfileDetail
import org.giste.profiles.domain.Setting
import org.giste.profiles.domain.SettingType
import javax.inject.Inject

class ProfileDetailMapper @Inject constructor(
    private val settingMapper: SettingMapper
) {
    fun toModel(profileEntity: ProfileEntity, settingList: List<SettingEntity>): ProfileDetail {
        return ProfileDetail(
            id = profileEntity.id,
            name = profileEntity.name,
            settings = settingMapper.toModel(settingList)
        )
    }
}