package org.giste.profiles.domain

data class BooleanSetting(
    override val profileId: Long,
    override val type: SettingType,
    override val override: Boolean = false,
    override val value: Boolean = false
) : Setting(profileId, type, override, value)
