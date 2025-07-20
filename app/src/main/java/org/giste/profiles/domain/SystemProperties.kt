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
