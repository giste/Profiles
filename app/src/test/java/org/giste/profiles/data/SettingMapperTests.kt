package org.giste.profiles.data

import org.giste.profiles.domain.IntSetting
import org.giste.profiles.domain.SettingType
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class SettingMapperTests {
    private val volumeMediaSetting = IntSetting(1, SettingType.VOLUME_MEDIA, true, 10)
    private val volumeRingSetting = IntSetting(1, SettingType.VOLUME_RING, true, 10)
    private val volumeNotificationSetting =
        IntSetting(1, SettingType.VOLUME_NOTIFICATION, true, 10)
    private val volumeAlarmSetting = IntSetting(1, SettingType.VOLUME_ALARM, true, 10)

    private val volumeMediaSettingEntity = SettingEntity(1, SettingType.VOLUME_MEDIA, true, 10)
    private val volumeRingSettingEntity = SettingEntity(1, SettingType.VOLUME_RING, true, 10)
    private val volumeNotificationSettingEntity =
        SettingEntity(1, SettingType.VOLUME_NOTIFICATION, true, 10)
    private val volumeAlarmSettingEntity = SettingEntity(1, SettingType.VOLUME_ALARM, true, 10)

    @Test
    fun toEntity_returnsSettingEntity() {
        val mapper = SettingMapper()

        assertThat(mapper.toEntity(volumeMediaSetting), equalTo(volumeMediaSettingEntity))
        assertThat(mapper.toEntity(volumeRingSetting), equalTo(volumeRingSettingEntity))
        assertThat(
            mapper.toEntity(volumeNotificationSetting),
            equalTo(volumeNotificationSettingEntity)
        )
        assertThat(mapper.toEntity(volumeAlarmSetting), equalTo(volumeAlarmSettingEntity))
    }

    @Test
    fun toModel_returnSetting() {
        val mapper = SettingMapper()

        assertThat(mapper.toModel(volumeMediaSettingEntity), equalTo(volumeMediaSetting))
        assertThat(mapper.toModel(volumeRingSettingEntity), equalTo(volumeRingSetting))
        assertThat(
            mapper.toModel(volumeNotificationSettingEntity),
            equalTo(volumeNotificationSetting)
        )
        assertThat(mapper.toModel(volumeAlarmSettingEntity), equalTo(volumeAlarmSetting))
    }
}