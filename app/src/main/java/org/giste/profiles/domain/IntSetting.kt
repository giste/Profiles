package org.giste.profiles.domain

data class IntSetting(
    override val id: Long,
    override val profileId: Long,
    override val type: SettingType,
    override val override: Boolean,
    override val value: Int = 0
) : Setting<Int>