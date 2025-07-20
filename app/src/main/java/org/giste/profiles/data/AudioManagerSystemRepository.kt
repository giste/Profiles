package org.giste.profiles.data

import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager
import android.media.AudioManager.STREAM_ALARM
import android.media.AudioManager.STREAM_MUSIC
import android.media.AudioManager.STREAM_NOTIFICATION
import android.media.AudioManager.STREAM_RING
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.giste.profiles.domain.SystemProperties
import org.giste.profiles.domain.SystemRepository
import javax.inject.Inject

private const val TAG = "AudioManagerSystemRepository"

class AudioManagerSystemRepository @Inject constructor(
    context: Context,
) : SystemRepository {
    private val audioManager = context.getSystemService(AUDIO_SERVICE) as AudioManager
    private val _systemProperties = MutableStateFlow(
        SystemProperties(
            streamMediaMinValue = audioManager.getStreamMinVolume(STREAM_MUSIC),
            streamMediaMaxValue = audioManager.getStreamMaxVolume(STREAM_MUSIC),
            streamRingMinValue = audioManager.getStreamMinVolume(STREAM_RING),
            streamRingMaxValue = audioManager.getStreamMaxVolume(STREAM_RING),
            streamNotificationMinValue = audioManager.getStreamMinVolume(STREAM_NOTIFICATION),
            streamNotificationMaxValue = audioManager.getStreamMaxVolume(STREAM_NOTIFICATION),
            streamAlarmMinValue = audioManager.getStreamMinVolume(STREAM_ALARM),
            streamAlarmMaxValue = audioManager.getStreamMaxVolume(STREAM_ALARM),
            ringAndNotificationLinked = checkLinkedRingAndNotification()
        )
    )

    override val systemProperties: StateFlow<SystemProperties> = _systemProperties.asStateFlow()

    private fun checkLinkedRingAndNotification(): Boolean {
        val currentRing = audioManager.getStreamVolume(STREAM_RING)
        val currentNotification = audioManager.getStreamVolume(STREAM_NOTIFICATION)

        audioManager.setStreamVolume(STREAM_RING, 2, 0)
        audioManager.setStreamVolume(STREAM_NOTIFICATION, 1, 0)

        val newRing = audioManager.getStreamVolume(STREAM_RING)
        val newNotification = audioManager.getStreamVolume(STREAM_NOTIFICATION)

        audioManager.setStreamVolume(STREAM_NOTIFICATION, currentNotification, 0)
        audioManager.setStreamVolume(STREAM_RING, currentRing, 0)

        if (newRing == newNotification) {
            Log.d(TAG, "ringAndNotificationLinked: true")
            return true
        }

        Log.d(TAG, "ringAndNotificationLinked: false")
        return false
    }
}