package org.giste.profiles.domain

data class IntSetting(
    override val profileId: Long,
    override val type: SettingType,
    override val override: Boolean = false,
    override val value: Int = 0
) : Setting(profileId, type, override, value)