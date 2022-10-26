package org.giste.profiles.data

import android.bluetooth.BluetoothManager
import android.content.Context
import android.media.AudioManager
import android.net.wifi.WifiManager
import android.telephony.TelephonyManager
import android.telephony.TelephonyManager.DATA_ENABLED_REASON_USER
import android.util.Log
import org.giste.profiles.domain.RingModeSetting
import org.giste.profiles.domain.Setting
import org.giste.profiles.domain.SettingType
import javax.inject.Inject


class SystemSettingsDataSource @Inject constructor(context: Context) {
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private val telephonyManager =
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter = bluetoothManager.adapter

    fun applySettings(settings: List<Setting>) {
        settings.filter { it.override }
            .forEach {
                Log.d("SystemSettingsDataSource", "Processing = $it")
                when (it.type) {
                    SettingType.VOLUME_MEDIA ->
                        setVolume(AudioManager.STREAM_MUSIC, it.value as Int)
                    SettingType.VOLUME_RING ->
                        setVolume(AudioManager.STREAM_RING, it.value as Int)
                    SettingType.VOLUME_NOTIFICATION ->
                        setVolume(AudioManager.STREAM_NOTIFICATION, it.value as Int)
                    SettingType.VOLUME_ALARM ->
                        setVolume(AudioManager.STREAM_ALARM, it.value as Int)
                    SettingType.RING_MODE ->
                        setRingerMode(it.value as RingModeSetting.Companion.RingMode)
                    SettingType.CONNECTION_WIFI -> applyWiFi(it.value as Boolean)
                    SettingType.CONNECTION_DATA -> applyData(it.value as Boolean)
                    SettingType.CONNECTION_BLUETOOTH -> applyBluetooth(it.value as Boolean)
                    SettingType.CONNECTION_NFC -> {}
                    SettingType.CONNECTION_AIRPLANE -> {}
                    SettingType.LOCATION -> {}
                    SettingType.BRIGHTNESS_AUTO -> {}
                    SettingType.BRIGHTNESS -> {}
                }
            }
    }

    private fun setVolume(stream: Int, value: Int) {
        Log.d("SystemSettingsDataSource", "Setting volume($stream) = $value")
        audioManager.setStreamVolume(stream, value, 0)
    }

    private fun setRingerMode(mode: RingModeSetting.Companion.RingMode) {
        Log.d("SystemSettingsDataSource", "Setting ringer mode = $mode")
        audioManager.ringerMode = when (mode) {
            RingModeSetting.Companion.RingMode.NORMAL -> AudioManager.RINGER_MODE_NORMAL
            RingModeSetting.Companion.RingMode.VIBRATE -> AudioManager.RINGER_MODE_VIBRATE
            RingModeSetting.Companion.RingMode.SILENT -> AudioManager.RINGER_MODE_SILENT
        }
    }

    private fun applyWiFi(value: Boolean) {
        if (wifiManager.isWifiEnabled != value) {
            Log.d("SystemSettingsDataSource", "Setting WiFi = $value")
            @Suppress("DEPRECATION")
            wifiManager.isWifiEnabled = value
        }
    }

    private fun applyData(value: Boolean) {
        if (telephonyManager.isDataEnabled != value) {
            Log.d("SystemSettingsDataSource", "Setting Data = $value")
            telephonyManager.setDataEnabledForReason(DATA_ENABLED_REASON_USER, value)
        }
    }

    private fun applyBluetooth(value: Boolean) {
        if (bluetoothAdapter.isEnabled != value) {
            Log.d("SystemSettingsDataSource", "Setting Bluetooth = $value")
            if (value) {
                @Suppress("DEPRECATION")
                bluetoothAdapter.enable()
            } else {
                @Suppress("DEPRECATION")
                bluetoothAdapter.disable()
            }
        }
    }
}