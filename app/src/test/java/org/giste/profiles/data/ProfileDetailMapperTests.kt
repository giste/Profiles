package org.giste.profiles.data

import org.giste.profiles.domain.IntSetting
import org.giste.profiles.domain.ProfileDetail
import org.giste.profiles.domain.SettingType
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class ProfileDetailMapperTests {
    private val volumeMediaSetting = IntSetting(1, 1, SettingType.VOLUME_MEDIA, true, 10)
    private val volumeRingSetting = IntSetting(2, 1, SettingType.VOLUME_RING, true, 10)
    private val volumeNotificationSetting =
        IntSetting(3, 1, SettingType.VOLUME_NOTIFICATION, true, 10)
    private val volumeAlarmSetting = IntSetting(4, 1, SettingType.VOLUME_ALARM, true, 10)

    private val volumeMediaSettingEntity = SettingEntity(1, 1, SettingType.VOLUME_MEDIA, true, 10)
    private val volumeRingSettingEntity = SettingEntity(2, 1, SettingType.VOLUME_RING, true, 10)
    private val volumeNotificationSettingEntity =
        SettingEntity(3, 1, SettingType.VOLUME_NOTIFICATION, true, 10)
    private val volumeAlarmSettingEntity = SettingEntity(4, 1, SettingType.VOLUME_ALARM, true, 10)

    private val settingEntityList = listOf(
        volumeMediaSettingEntity,
        volumeRingSettingEntity,
        volumeNotificationSettingEntity,
        volumeAlarmSettingEntity
    )

    private val profileDetail = ProfileDetail(
        1, "Profile 1", mapOf(
            SettingType.VOLUME_MEDIA to volumeMediaSetting,
            SettingType.VOLUME_RING to volumeRingSetting,
            SettingType.VOLUME_NOTIFICATION to volumeNotificationSetting,
            SettingType.VOLUME_ALARM to volumeAlarmSetting
        )
    )

    private val profileEntity = ProfileEntity(1, "Profile 1")

    @Test
    fun toModel_allSettingsExist_returnsProfileDetail() {
        val mapper = ProfileDetailMapper(SettingMapper())
        assertThat(mapper.toModel(profileEntity, settingEntityList), equalTo(profileDetail))
    }

    @Test
    fun toModel_someSettingsDoNotExist_returnsAllSettings() {
        val mapper = ProfileDetailMapper(SettingMapper())

        val detail =
            mapper.toModel(profileEntity, listOf(volumeRingSettingEntity, volumeAlarmSettingEntity))

        assertThat(
            detail.settings.values.toList(), equalTo(
                listOf(
                    IntSetting(profileId = profileEntity.id, type = SettingType.VOLUME_MEDIA),
                    volumeRingSetting,
                    IntSetting(
                        profileId = profileEntity.id,
                        type = SettingType.VOLUME_NOTIFICATION
                    ),
                    volumeAlarmSetting
                )
            )
        )
    }

    @Test
    fun toEntity_allSettingsExist_returnEntities() {
        val mapper = ProfileDetailMapper(SettingMapper())

        assertThat(mapper.toEntity(profileDetail), equalTo(Pair(profileEntity, settingEntityList)))
    }

    @Test
    fun toEntity_someSettingsDoNotExist_returnAllSettings() {
        val mapper = ProfileDetailMapper(SettingMapper())

        val entity = mapper.toEntity(
            ProfileDetail(
                id = profileDetail.id,
                name = profileDetail.name,
                settings = mapOf(
                    SettingType.VOLUME_RING to volumeRingSetting,
                    SettingType.VOLUME_ALARM to volumeAlarmSetting
                )
            )
        )

        assertThat(
            entity.second, equalTo(
                listOf(
                    SettingEntity(profileId = profileDetail.id, type = SettingType.VOLUME_MEDIA),
                    volumeRingSettingEntity,
                    SettingEntity(
                        profileId = profileDetail.id,
                        type = SettingType.VOLUME_NOTIFICATION
                    ),
                    volumeAlarmSettingEntity
                )
            )
        )
    }
}