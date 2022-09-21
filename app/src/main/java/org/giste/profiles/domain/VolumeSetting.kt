package org.giste.profiles.domain

data class VolumeSetting(
    override val id: Long,
    override val profileId: Long,
    override val override: Boolean,
    val type: VolumeType,
    override val value: Int = 0
) : Setting<Int>