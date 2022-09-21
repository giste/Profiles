package org.giste.profiles.domain

data class VolumeSetting(
    override val id: Long,
    override var override: Boolean,
    val type: VolumeType,
    override var value: Int = 0
) : Setting<Int>