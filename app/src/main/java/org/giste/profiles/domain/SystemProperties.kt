package org.giste.profiles.domain

interface SystemProperties {
    val streamMediaMinValue: Int
    val streamMediaMaxValue: Int

    val streamRingMinValue: Int
    val streamRingMaxValue: Int

    val streamNotificationMinValue: Int
    val streamNotificationMaxValue: Int

    val streamAlarmMinValue: Int
    val streamAlarmMaxValue: Int

    val ringAndNotificationLinked: Boolean
}