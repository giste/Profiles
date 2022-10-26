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


class SystemSettingsDataSource @Inject constructor(private val context: Context) {
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    private val telephonyManager =
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    private val bluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter = bluetoothManager.adapter
//    private val nfcAdapter: NfcAdapter? = try {
//        NfcAdapter.getDefaultAdapter(context)
//    } catch (e: UnsupportedOperationException) {
//        null
//    }

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
                    SettingType.CONNECTION_AIRPLANE -> applyAirplaneMode(it.value as Boolean)
                    SettingType.LOCATION -> {}
                    SettingType.BRIGHTNESS_AUTO -> applyBrightnessAuto(it.value as Boolean)
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

//    private fun applyNfc(value: Boolean) {
//        if (nfcAdapter?.isEnabled != value) {
//            if (value) {
//                //nfcAdapter.enable()
//            }
//        }
//    }

    private fun applyAirplaneMode(value: Boolean) {
        val newValue = if (value) 1 else 0
        val current =
            Settings.Global.getInt(context.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0)
        if (current != newValue) {
            Log.d("SystemSettingsDataSource", "Setting Airplane Mode = $value")
            Settings.Global.putInt(
                context.contentResolver,
                Settings.Global.AIRPLANE_MODE_ON,
                newValue
            )
            val intent = Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED)
            intent.putExtra("state", newValue == 1)
            context.sendBroadcast(intent)
        }
    }

//    private fun applyLocation(value: Boolean) {
//        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        if (lm.isLocationEnabled != value) {
//
//        }
//    }

    private fun applyBrightnessAuto(value: Boolean) {
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
}