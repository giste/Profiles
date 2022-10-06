package org.giste.profiles.domain

sealed class Setting(
    open val profileId: Long,
    open val type: SettingType,
    open val override: Boolean = false,
    open val value: Any,
)