package org.giste.profiles.data

import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.wifi.WifiManager
import android.provider.Settings
import android.telephony.TelephonyManager
import android.telephony.TelephonyManager.DATA_ENABLED_REASON_USER
import android.util.Log
import org.giste.profiles.domain.RingModeSetting
import org.giste.profiles.domain.Setting
import org.giste.profiles.domain.SettingType
import javax.inject.Inject
import kotlin.math.exp
import kotlin.math.roundToInt


class SystemSettingsDataSource @Inject constructor(private val context: Context) {

    fun applySettings(settings: List<Setting>) {
        settings.filter { it.override }
            .forEach {
                Log.d("SystemSettingsDataSource", "Processing = $it")
                when (it.type) {
                    SettingType.VOLUME_MEDIA ->
                        ProfilesLib.setVolume(context, AudioManager.STREAM_MUSIC, it.value as Int)
                    SettingType.VOLUME_RING ->
                        ProfilesLib.setVolume(context, AudioManager.STREAM_RING, it.value as Int)
                    SettingType.VOLUME_NOTIFICATION ->
                        ProfilesLib.setVolume(context, AudioManager.STREAM_NOTIFICATION, it.value as Int)
                    SettingType.VOLUME_ALARM ->
                        ProfilesLib.setVolume(context, AudioManager.STREAM_ALARM, it.value as Int)
                    SettingType.RING_MODE ->
                        ProfilesLib.setRingerMode(context, it.value as RingModeSetting.Companion.RingMode)
                    SettingType.CONNECTION_WIFI ->
                        ProfilesLib.setWiFi(context, it.value as Boolean)
                    SettingType.CONNECTION_DATA ->
                        ProfilesLib.setData(context, it.value as Boolean)
                    SettingType.CONNECTION_BLUETOOTH ->
                        ProfilesLib.setBluetooth(context, it.value as Boolean)
                    SettingType.CONNECTION_NFC ->
                        ProfilesLib.setNfc(context, it.value as Boolean)
                    SettingType.CONNECTION_AIRPLANE ->
                        ProfilesLib.setAirplaneMode(context, it.value as Boolean)
                    SettingType.LOCATION ->
                        ProfilesLib.setLocation(context, it.value as Boolean)
                    SettingType.BRIGHTNESS_AUTO ->
                        ProfilesLib.setBrightnessAuto(context, it.value as Boolean)
                    SettingType.BRIGHTNESS ->
                        ProfilesLib.setBrightness(context, it.value as Int)
                }
            }
    }

}