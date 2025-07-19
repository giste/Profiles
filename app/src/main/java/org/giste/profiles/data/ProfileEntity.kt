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

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.giste.profiles.domain.RingModeSetting

@Entity(
    tableName = "profiles",
    indices = [Index(value = ["name"], unique = true)]
)
data class ProfileEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "profile_id")
    val id: Long = 0,

    @ColumnInfo(name = "name", collate = ColumnInfo.NOCASE)
    val name: String = "",

    @ColumnInfo(name = "apply_media_volume")
    val applyMediaVolume: Boolean = false,

    @ColumnInfo(name = "media_volume")
    val mediaVolume: Int = 0,

    @ColumnInfo(name = "apply_ring_volume")
    val applyRingVolume: Boolean = false,

    @ColumnInfo(name = "ring_volume")
    val ringVolume: Int = 0,

    @ColumnInfo(name = "apply_notification_volume")
    val applyNotificationVolume: Boolean = false,

    @ColumnInfo(name = "notification_volume")
    val notificationVolume: Int = 0,

    @ColumnInfo(name = "apply_alarm_volume")
    val applyAlarmVolume: Boolean = false,

    @ColumnInfo(name = "alarm_volume")
    val alarmVolume: Int = 0,

    @ColumnInfo(name = "apply_auto_brightness")
    val applyAutoBrightness: Boolean = false,

    @ColumnInfo(name = "auto_brightness")
    val autoBrightness: Boolean = false,

    @ColumnInfo(name = "apply_brightness")
    val applyBrightness: Boolean = false,

    @ColumnInfo(name = "brightness")
    val brightness: Int = 0,

    @ColumnInfo(name = "apply_ring_mode")
    val applyRingMode: Boolean = false,

    @ColumnInfo(name = "ring_mode")
    val ringMode: RingModeSetting.Companion.RingMode = RingModeSetting.Companion.RingMode.NORMAL,
)
