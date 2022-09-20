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
}