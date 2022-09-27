package org.giste.profiles.domain

data class RingModeSetting(
    override val id: Long = 0,
    override val profileId: Long,
    override val type: SettingType,
    override val override: Boolean = false,
    override val value: RingMode = RingMode.NORMAL
) : Setting<RingMode>