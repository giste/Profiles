package org.giste.profiles.data

import androidx.room.*
import org.giste.profiles.domain.SettingType

@Entity(
    tableName = "settings",
    foreignKeys = [ForeignKey(
        entity = ProfileEntity::class,
        parentColumns = ["profile_id"],
        childColumns = ["profile_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["profile_id"])]
)
data class SettingEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "setting_id")
    val id: Long = 0,

    @ColumnInfo(name = "profile_id")
    val profileId: Long,

    @ColumnInfo(name = "type")
    val type: SettingType,

    @ColumnInfo(name = "override")
    val override: Boolean = false,

    @ColumnInfo(name = "value")
    val value: Int = 0
)