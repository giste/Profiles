package org.giste.profiles.domain

data class RingModeSetting(
    override val profileId: Long,
    override val type: SettingType,
    override val override: Boolean = false,
    override val value: RingMode = RingMode.NORMAL
) : Setting<RingModeSetting.Companion.RingMode> {
    companion object {
        enum class RingMode { NORMAL, VIBRATE, SILENT }
    }
}
