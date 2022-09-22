package org.giste.profiles.domain

data class IntSetting(
    override val id: Long = 0,
    override val profileId: Long,
    override val type: SettingType,
    override val override: Boolean = false,
    override val value: Int = 0
) : Setting<Int>