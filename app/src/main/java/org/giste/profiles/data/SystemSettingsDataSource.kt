package org.giste.profiles.data

import android.content.Context
import android.media.AudioManager
import android.net.wifi.WifiManager
import org.giste.profiles.domain.RingModeSetting
import org.giste.profiles.domain.Setting
import org.giste.profiles.domain.SettingType
import javax.inject.Inject

class SystemSettingsDataSource @Inject constructor(context: Context) {
    private val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val wm = context.getSystemService(Context.WIFI_SERVICE) as WifiManager

    fun applySettings(settings: List<Setting>) {
        settings.filter { it.override }
            .forEach {
                when (it.type) {
                    SettingType.VOLUME_MEDIA ->
                        am.setStreamVolume(AudioManager.STREAM_MUSIC, it.value as Int, 0)
                    SettingType.VOLUME_RING ->
                        am.setStreamVolume(AudioManager.STREAM_RING, it.value as Int, 0)
                    SettingType.VOLUME_NOTIFICATION ->
                        am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, it.value as Int, 0)
                    SettingType.VOLUME_ALARM ->
                        am.setStreamVolume(AudioManager.STREAM_ALARM, it.value as Int, 0)
                    SettingType.RING_MODE -> {}
//                        am.ringerMode =
//                        when (it.value as RingModeSetting.Companion.RingMode) {
//                            RingModeSetting.Companion.RingMode.NORMAL -> AudioManager.RINGER_MODE_NORMAL
//                            RingModeSetting.Companion.RingMode.VIBRATE -> AudioManager.RINGER_MODE_VIBRATE
//                            RingModeSetting.Companion.RingMode.SILENT -> AudioManager.RINGER_MODE_SILENT
//                        }
                    SettingType.CONNECTION_WIFI -> {} //applyWiFi(it.value as Boolean)
                    SettingType.CONNECTION_DATA -> {}
                    SettingType.CONNECTION_BLUETOOTH -> {}
                    SettingType.CONNECTION_NFC -> {}
                    SettingType.CONNECTION_AIRPLANE -> {}
                    SettingType.LOCATION -> {}
                    SettingType.BRIGHTNESS_AUTO -> {}
                    SettingType.BRIGHTNESS -> {}
                }
            }
    }

    private fun applyWiFi(value: Boolean) {
        if (wm.isWifiEnabled != value) {
            wm.isWifiEnabled = value
        }
    }

}