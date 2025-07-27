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

fun Profile.toEntity(): ProfileEntity {
    return with (this) {
        ProfileEntity(
            id = id,
            name = name,
            applyMediaVolume = mediaVolume.apply,
            mediaVolume = mediaVolume.value,
            applyRingVolume = ringVolume.apply,
            ringVolume = ringVolume.value,
            applyNotificationVolume = notificationVolume.apply,
            notificationVolume = notificationVolume.value,
            applyAlarmVolume = alarmVolume.apply,
            alarmVolume = alarmVolume.value,
            applyAutoBrightness = autoBrightness.apply,
            autoBrightness = autoBrightness.value,
            applyBrightnessLevel = brightnessLevel.apply,
            brightnessLevel = brightnessLevel.value,
            applyRingMode = ringMode.apply,
            ringMode = ringMode.value,
        )
    }
}

fun ProfileEntity.toModel(): Profile {
    return with(this) {
        Profile(
            id = id,
            name = name,
            mediaVolume = IntSetting(applyMediaVolume, mediaVolume),
            ringVolume = IntSetting(applyRingVolume, ringVolume),
            notificationVolume = IntSetting(applyNotificationVolume, notificationVolume),
            alarmVolume = IntSetting(applyAlarmVolume, alarmVolume),
            autoBrightness = BooleanSetting(applyAutoBrightness, autoBrightness),
            brightnessLevel = IntSetting(applyBrightnessLevel, brightnessLevel),
            ringMode = RingModeSetting(applyRingMode, ringMode),
        )
    }
}