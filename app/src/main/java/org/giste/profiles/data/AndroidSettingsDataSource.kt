package org.giste.profiles.data

import android.content.Context
import android.media.AudioManager
import android.provider.Settings
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import org.giste.profiles.domain.Profile
import org.giste.profiles.domain.RingModeSetting
import javax.inject.Inject
import kotlin.math.exp
import kotlin.math.roundToInt

private const val TAG = "AndroidSettingsDataSource"

class AndroidSettingsDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    private val audioManager by lazy { context.getSystemService(Context.AUDIO_SERVICE) as AudioManager }

    fun applySettings(profile: Profile) {
        with (profile) {
            if (mediaVolume.apply) setVolume(VolumeStream.MediaStream, mediaVolume.value)
            if (ringVolume.apply) setVolume(VolumeStream.RingStream, ringVolume.value)
            if (notificationVolume.apply)
                setVolume(VolumeStream.NotificationStream, notificationVolume.value)
            if (alarmVolume.apply) setVolume(VolumeStream.AlarmStream, alarmVolume.value)
            if (ringMode.apply) setRingMode(ringMode.value)
            if (autoBrightness.apply) setAutoBrightness(autoBrightness.value)
            if (brightness.apply) setBrightnessLevel(brightness.value)
        }
    }

    private fun setVolume(stream: VolumeStream, value: Int) {
        val currentValue = audioManager.getStreamVolume(stream.value)

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
            Log.d(TAG, "Setting Auto Brightness = $value")
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

        val newValue = mapBrightness(value)

        if (isManual) {
            val currentValue = Settings.System.getInt(
                context.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS, 0
            )

            if (currentValue != newValue) {
                Log.d("SystemSettingsDataSource", "Setting Brightness = $value -> $newValue")
                Settings.System.putInt(
                    context.contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS,
                    newValue
                )
            }
        } else {
            Log.d("SystemSettingsDataSource", "Not setting brightness, it's auto")
        }
    }

    private fun mapBrightness(percentage: Int) =
        if (percentage <= 1) {
            1
        } else if (percentage >= 100) {
            255
        } else {
            exp((percentage + 9.411) / 19.811).roundToInt()
        }

    sealed class VolumeStream(val value: Int) {
        data object MediaStream : VolumeStream(AudioManager.STREAM_MUSIC)
        data object RingStream : VolumeStream(AudioManager.STREAM_MUSIC)
        data object NotificationStream : VolumeStream(AudioManager.STREAM_MUSIC)
        data object AlarmStream : VolumeStream(AudioManager.STREAM_MUSIC)
    }
}