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

data class SystemProperties(
    val streamMediaMinValue: Int = 0,
    val streamMediaMaxValue: Int = 15,
    val streamRingMinValue: Int = 0,
    val streamRingMaxValue: Int = 7,
    val streamNotificationMinValue: Int = 0,
    val streamNotificationMaxValue: Int = 7,
    val streamAlarmMinValue: Int = 1,
    val streamAlarmMaxValue: Int = 7,
    val ringAndNotificationLinked: Boolean = false,
)
