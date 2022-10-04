package org.giste.profiles.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import org.giste.profiles.domain.SettingType

@Entity(
    tableName = "settings",
    primaryKeys = ["profile_id", "type"],
    foreignKeys = [ForeignKey(
        entity = ProfileEntity::class,
        parentColumns = ["profile_id"],
        childColumns = ["profile_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["profile_id"])]
)
data class SettingEntity(
    @ColumnInfo(name = "profile_id")
    val profileId: Long,

    @ColumnInfo(name = "type")
    val type: SettingType,

    @ColumnInfo(name = "override")
    val override: Boolean = false,

    @ColumnInfo(name = "value")
    val value: Int = 0
)