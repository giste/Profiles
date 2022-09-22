package org.giste.profiles.domain

sealed interface Setting<out T> {
    val id: Long
    val profileId: Long
    val type: SettingType
    val override: Boolean
    val value: T
}