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

package org.giste.profiles.domain

data class Profile(
    val id: Long = 0L,
    val name: String = "",
    val mediaVolume: IntSetting = IntSetting(),
    val ringVolume: IntSetting = IntSetting(),
    val notificationVolume: IntSetting = IntSetting(),
    val alarmVolume: IntSetting = IntSetting(),
    val autoBrightness: BooleanSetting = BooleanSetting(),
    val brightness: IntSetting = IntSetting(),
    val ringMode: RingModeSetting = RingModeSetting(),
)
