package org.giste.profiles.data

import android.content.Context
import android.media.AudioManager
import android.util.Log
import org.giste.profiles.domain.RingModeSetting
import org.giste.profiles.domain.Setting
import org.giste.profiles.domain.SettingType
import javax.inject.Inject

class SystemSettingsDataSource @Inject constructor(private val context: Context) {

    fun applySettings(settings: List<Setting>) {
        settings.filter { it.override }
            .forEach {
                Log.d("SystemSettingsDataSource", "Processing = $it")
                when (it.type) {
                    SettingType.VOLUME_MEDIA ->
                        ProfilesInternal.setVolume(context, AudioManager.STREAM_MUSIC, it.value as Int)
                    SettingType.VOLUME_RING ->
                        ProfilesInternal.setVolume(context, AudioManager.STREAM_RING, it.value as Int)
                    SettingType.VOLUME_NOTIFICATION ->
                        ProfilesInternal.setVolume(context, AudioManager.STREAM_NOTIFICATION, it.value as Int)
                    SettingType.VOLUME_ALARM ->
                        ProfilesInternal.setVolume(context, AudioManager.STREAM_ALARM, it.value as Int)
                    SettingType.RING_MODE -> {
                        val value = when(it.value as RingModeSetting.Companion.RingMode) {
                            RingModeSetting.Companion.RingMode.NORMAL -> AudioManager.RINGER_MODE_NORMAL
                            RingModeSetting.Companion.RingMode.VIBRATE -> AudioManager.RINGER_MODE_VIBRATE
                            RingModeSetting.Companion.RingMode.SILENT -> AudioManager.RINGER_MODE_SILENT
                        }

                        ProfilesInternal.setRingerMode(context, value)
                    }
                    SettingType.BRIGHTNESS_AUTO ->
                        ProfilesInternal.setBrightnessAuto(context, it.value as Boolean)
                    SettingType.BRIGHTNESS ->
                        ProfilesInternal.setBrightness(context, it.value as Int)
                }
            }
    }

}