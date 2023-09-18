package org.giste.profiles.data

import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager
import android.media.AudioManager.*
import android.util.Log
import org.giste.profiles.domain.SystemProperties
import javax.inject.Inject

class SystemPropertiesDataSource @Inject constructor(context: Context) : SystemProperties {
    private val am = context.getSystemService(AUDIO_SERVICE) as AudioManager

    override val streamMediaMinValue = am.getStreamMinVolume(STREAM_MUSIC)
    override val streamMediaMaxValue = am.getStreamMaxVolume(STREAM_MUSIC)

    override val streamRingMinValue = am.getStreamMinVolume(STREAM_RING)
    override val streamRingMaxValue = am.getStreamMaxVolume(STREAM_RING)

    override val streamNotificationMinValue = am.getStreamMinVolume(STREAM_NOTIFICATION)
    override val streamNotificationMaxValue = am.getStreamMaxVolume(STREAM_NOTIFICATION)

    override val streamAlarmMinValue = am.getStreamMinVolume(STREAM_ALARM)
    override val streamAlarmMaxValue = am.getStreamMaxVolume(STREAM_ALARM)

    override val ringAndNotificationLinked = checkLinkedRingAndNotification()

    init {
        Log.d("SystemPropertiesDataSource", "minMedia: $streamMediaMinValue")
        Log.d("SystemPropertiesDataSource", "maxMedia: $streamMediaMaxValue")
        Log.d("SystemPropertiesDataSource", "minRing: $streamRingMinValue")
        Log.d("SystemPropertiesDataSource", "maxRing: $streamRingMaxValue")
        Log.d("SystemPropertiesDataSource", "minNotification: $streamNotificationMinValue")
        Log.d("SystemPropertiesDataSource", "maxNotification: $streamNotificationMaxValue")
        Log.d("SystemPropertiesDataSource", "minAlarm: $streamAlarmMinValue")
        Log.d("SystemPropertiesDataSource", "maxAlarm: $streamAlarmMaxValue")
    }

    private fun checkLinkedRingAndNotification(): Boolean {
        val currentRing = am.getStreamVolume(STREAM_RING)
        val currentNotification = am.getStreamVolume(STREAM_NOTIFICATION)

        am.setStreamVolume(STREAM_RING, 0, 0)
        am.setStreamVolume(STREAM_NOTIFICATION, 1, 0)

        val newRing = am.getStreamVolume(STREAM_RING)
        val newNotification = am.getStreamVolume(STREAM_NOTIFICATION)

        am.setStreamVolume(STREAM_RING, currentRing, 0)
        am.setStreamVolume(STREAM_NOTIFICATION, currentNotification, 0)

        if (newRing == newNotification) {
            Log.d("SystemPropertiesDataSource", "ringAndNotificationLinked: true")
            return true
        }

        Log.d("SystemPropertiesDataSource", "ringAndNotificationLinked: false")
        return false
    }
}