package org.giste.profiles.data

import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.media.AudioManager
import android.net.wifi.WifiManager
import android.nfc.NfcAdapter
import android.provider.Settings
import android.telephony.TelephonyManager
import android.telephony.TelephonyManager.DATA_ENABLED_REASON_USER
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

        fun setWiFi(context: Context, value: Boolean) {
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager

            if (wifiManager.isWifiEnabled != value) {
                Log.d("ProfilesLib", "Setting WiFi = $value")
                @Suppress("DEPRECATION")
                wifiManager.isWifiEnabled = value
            }
        }

        fun setData(context: Context, value: Boolean) {
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

            if (telephonyManager.isDataEnabled != value) {
                Log.d("ProfilesLib", "Setting Data = $value")
                telephonyManager.setDataEnabledForReason(DATA_ENABLED_REASON_USER, value)
            }
        }

        fun setBluetooth(context: Context, value: Boolean) {
            val bluetoothManager =
                context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            val bluetoothAdapter = bluetoothManager.adapter

            if (bluetoothAdapter.isEnabled != value) {
                Log.d("ProfilesLib", "Setting Bluetooth = $value")
                if (value) {
                    @Suppress("DEPRECATION")
                    bluetoothAdapter.enable()
                } else {
                    @Suppress("DEPRECATION")
                    bluetoothAdapter.disable()
                }
            }
        }

        fun setNfc(context: Context, value: Boolean) {
            val nfcAdapter: NfcAdapter? = try {
                NfcAdapter.getDefaultAdapter(context)
            } catch (e: UnsupportedOperationException) {
                null
            }

            if (nfcAdapter?.isEnabled != value) {
                if (value) {
                    Log.d("ProfilesLib", "Enabling NFC")
                    //nfcAdapter.enable()
                } else {
                    Log.d("ProfilesLib", "Disabling NFC")
                    //nfcAdapter.disable()
                }
            }
        }

        fun setAirplaneMode(context: Context, value: Boolean) {
            val newValue = if (value) 1 else 0
            val current =
                Settings.Global.getInt(context.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0)

            if (current != newValue) {
                Log.d("ProfilesLib", "Setting Airplane Mode = $value")
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

        fun setLocation(context: Context, value: Boolean) {
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (lm.isLocationEnabled != value) {
                Log.d("ProfilesLib", "Setting Location = $value")
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
