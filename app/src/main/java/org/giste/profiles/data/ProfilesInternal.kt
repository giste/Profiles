package org.giste.profiles.data

import android.content.Context
import android.media.AudioManager
import android.provider.Settings
import android.util.Log
import kotlin.math.exp
import kotlin.math.roundToInt

class ProfilesInternal {
    companion object {
        fun setVolume(context: Context, stream: Int, value: Int) {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

            Log.d("ProfilesLib", "Setting volume($stream) = $value")
            audioManager.setStreamVolume(stream, value, 0)
        }

        fun setRingerMode(context: Context, mode: Int) {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val current = audioManager.ringerMode

            Log.d("ProfilesLib", "Setting ringer mode = $mode")
            audioManager.ringerMode = when (mode) {
                AudioManager.RINGER_MODE_NORMAL -> AudioManager.RINGER_MODE_NORMAL
                AudioManager.RINGER_MODE_VIBRATE -> AudioManager.RINGER_MODE_VIBRATE
                AudioManager.RINGER_MODE_SILENT -> AudioManager.RINGER_MODE_SILENT
                else -> {
                    current
                }
            }
        }

        fun setBrightnessAuto(context: Context, value: Boolean) {
            val newValue = if (value) 1 else 0
            val current = Settings.System.getInt(
                context.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
            )
            if (current != newValue) {
                Log.d("SystemSettingsDataSource", "Setting Brightness Auto = $value")
                Settings.System.putInt(
                    context.contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    newValue
                )
            }
        }

        fun setBrightness(context: Context, value: Int) {
            val manual = (Settings.System.getInt(
                context.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
            ) == Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL)

            val newValue = mapBrightness(value)

            if (manual) {
                val current = Settings.System.getInt(
                    context.contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS, 0
                )

                if (current != newValue) {
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
    }
}
