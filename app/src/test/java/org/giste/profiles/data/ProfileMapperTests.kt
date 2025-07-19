/*
 * Copyright 2025 Giste Trappiste
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.giste.profiles.data

import org.giste.profiles.domain.BooleanSetting
import org.giste.profiles.domain.IntSetting
import org.giste.profiles.domain.Profile
import org.giste.profiles.domain.RingModeSetting
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProfileMapperTests {
    @Test
    fun toEntity_returns_expected_entity() {
        val expectedEntity = ProfileEntity(
            id = 1,
            name = "Name",
            applyMediaVolume = true,
            mediaVolume = 1,
            applyRingVolume = true,
            ringVolume = 2,
            applyNotificationVolume = true,
            notificationVolume = 3,
            applyAlarmVolume = true,
            alarmVolume = 4,
            applyAutoBrightness = true,
            autoBrightness = true,
            applyBrightness = true,
            brightness = 5,
            applyRingMode = true,
            ringMode = RingModeSetting.Companion.RingMode.VIBRATE,
        )
        val profile = Profile(
            id = 1,
            name = "Name",
            mediaVolume = IntSetting(true, 1),
            ringVolume = IntSetting(true, 2),
            notificationVolume = IntSetting(true, 3),
            alarmVolume = IntSetting(true, 4),
            autoBrightness = BooleanSetting(apply = true, value = true),
            brightness = IntSetting(true, 5),
            ringMode = RingModeSetting(true, RingModeSetting.Companion.RingMode.VIBRATE),
        )

        val actualEntity = profile.toEntity()

        assertEquals(expectedEntity, actualEntity)
    }

    @Test
    fun toModel_returns_expected_profile() {
        val expectedProfile = Profile(
            id = 1,
            name = "Name",
            mediaVolume = IntSetting(true, 1),
            ringVolume = IntSetting(true, 2),
            notificationVolume = IntSetting(true, 3),
            alarmVolume = IntSetting(true, 4),
            autoBrightness = BooleanSetting(apply = true, value = true),
            brightness = IntSetting(true, 5),
            ringMode = RingModeSetting(true, RingModeSetting.Companion.RingMode.VIBRATE),
        )
        val entity = ProfileEntity(
            id = 1,
            name = "Name",
            applyMediaVolume = true,
            mediaVolume = 1,
            applyRingVolume = true,
            ringVolume = 2,
            applyNotificationVolume = true,
            notificationVolume = 3,
            applyAlarmVolume = true,
            alarmVolume = 4,
            applyAutoBrightness = true,
            autoBrightness = true,
            applyBrightness = true,
            brightness = 5,
            applyRingMode = true,
            ringMode = RingModeSetting.Companion.RingMode.VIBRATE,
        )

        val actualProfile = entity.toModel()

        assertEquals(expectedProfile, actualProfile)
    }
}