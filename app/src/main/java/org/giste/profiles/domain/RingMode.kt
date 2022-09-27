package org.giste.profiles.domain

import android.media.AudioManager

enum class RingMode(val ringMode: Int) {
    NORMAL(AudioManager.RINGER_MODE_NORMAL),
    VIBRATE(AudioManager.RINGER_MODE_VIBRATE),
    SILENT(AudioManager.RINGER_MODE_SILENT);

    companion object {
        fun getRingMode(ringMode: Int): RingMode {
            return when (ringMode) {
                AudioManager.RINGER_MODE_VIBRATE -> VIBRATE
                AudioManager.RINGER_MODE_SILENT -> SILENT
                else -> NORMAL
            }
        }
    }

}