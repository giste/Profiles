package org.giste.profiles.data

import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.media.AudioManager
import android.media.AudioManager.STREAM_ALARM
import android.media.AudioManager.STREAM_MUSIC
import android.media.AudioManager.STREAM_NOTIFICATION
import android.media.AudioManager.STREAM_RING
import android.provider.Settings
import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.giste.profiles.IoDispatcher
import org.giste.profiles.domain.Profile
import org.giste.profiles.domain.RingModeSetting
import org.giste.profiles.domain.SystemProperties
import org.giste.profiles.domain.SystemRepository
import javax.inject.Inject
import kotlin.math.exp
import kotlin.math.roundToInt

private const val TAG = "AndroidSystemRepository"

internal class AndroidSystemRepository @Inject constructor(
    private val context: Context,
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
) : SystemRepository {
    private val audioManager by lazy {
        context.getSystemService(AUDIO_SERVICE) as AudioManager
    }
    private val ringAndNotificationLinked by lazy {
        checkLinkedRingAndNotification()
    }

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
            ringAndNotificationLinked = ringAndNotificationLinked
        )
    )

    override val systemProperties: StateFlow<SystemProperties> = _systemProperties.asStateFlow()

    override suspend fun applySettings(profile: Profile) {
        withContext(dispatcher) {
            with(profile) {
                if (mediaVolume.apply) setVolume(VolumeStream.MediaStream, mediaVolume.value)
                if (ringVolume.apply) setVolume(VolumeStream.RingStream, ringVolume.value)
                if (!ringAndNotificationLinked) {
                    if (notificationVolume.apply) setVolume(
                        stream = VolumeStream.NotificationStream,
                        value = notificationVolume.value
                    )
                }
                if (alarmVolume.apply) setVolume(VolumeStream.AlarmStream, alarmVolume.value)
                if (ringMode.apply) setRingMode(ringMode.value)
                if (autoBrightness.apply) setAutoBrightness(autoBrightness.value)
                if (brightness.apply) setBrightnessLevel(brightness.value)
            }
        }
    }

    private fun setVolume(stream: VolumeStream, value: Int) {
        val currentValue = audioManager.getStreamVolume(stream.value)

        Log.d(TAG, "Current value for stream $stream: $currentValue, new value: $value")

        if (currentValue != value) {
            Log.d(TAG, "Setting volume for stream $stream = $value")
            audioManager.setStreamVolume(stream.value, value, 0)
        }
    }

    private fun setRingMode(mode: RingModeSetting.Companion.RingMode) {
        val currentMode = audioManager.ringerMode
        val newMode = when (mode) {
            RingModeSetting.Companion.RingMode.NORMAL -> AudioManager.RINGER_MODE_NORMAL
            RingModeSetting.Companion.RingMode.VIBRATE -> AudioManager.RINGER_MODE_VIBRATE
            RingModeSetting.Companion.RingMode.SILENT -> AudioManager.RINGER_MODE_SILENT
        }

        if (newMode != currentMode) {
            Log.d(TAG, "Setting ringer mode = $mode")
            audioManager.ringerMode = newMode
        }
    }

    private fun setAutoBrightness(value: Boolean) {
        val newValue = if (value) 1 else 0
        val currentValue = Settings.System.getInt(
            context.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS_MODE,
            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
        )

        if (currentValue != newValue) {
            Log.d(TAG, "Setting Auto Brightness = $value from $currentValue")
            Settings.System.putInt(
                context.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                newValue
            )
        }
    }

    private fun setBrightnessLevel(value: Int) {
        val isManual = (Settings.System.getInt(
            context.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS_MODE,
            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
        ) == Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL)

        val currentValue = Settings.System.getInt(
            context.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS,
        )

        val newValue = mapBrightness(value)

        if (isManual) {
            Log.d(TAG, "Setting brightness level = $value -> $newValue from $currentValue")
            Settings.System.putInt(
                context.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS,
                newValue
            )
        } else {
            Log.d(TAG, "Not setting brightness, it's automatic")
        }
    }

    private fun mapBrightness(percentage: Int) = if (percentage <= 1) {
            1
        } else if (percentage >= 100) {
            255
        } else {
            exp((percentage + 9.411) / 19.811).roundToInt()
        }

    private fun checkLinkedRingAndNotification(): Boolean {
        val currentRing = audioManager.getStreamVolume(STREAM_RING)
        val currentNotification = audioManager.getStreamVolume(STREAM_NOTIFICATION)

        audioManager.setStreamVolume(STREAM_RING, 2, 0)
        audioManager.setStreamVolume(STREAM_NOTIFICATION, 1, 0)

        val newRing = audioManager.getStreamVolume(STREAM_RING)
        val newNotification = audioManager.getStreamVolume(STREAM_NOTIFICATION)

        audioManager.setStreamVolume(STREAM_NOTIFICATION, currentNotification, 0)
        audioManager.setStreamVolume(STREAM_RING, currentRing, 0)

        val areLinked = (newRing == 2) && (newNotification == 1)

        Log.d(TAG, "Ring & notification streams linked: $areLinked")
        return areLinked
    }

    private sealed class VolumeStream(val value: Int) {
        data object MediaStream : VolumeStream(STREAM_MUSIC)
        data object RingStream : VolumeStream(STREAM_RING)
        data object NotificationStream : VolumeStream(STREAM_NOTIFICATION)
        data object AlarmStream : VolumeStream(STREAM_ALARM)
    }
}