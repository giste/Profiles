package org.giste.profiles.data

import androidx.room.*

@Entity(
    tableName = "profiles",
    indices = [Index(value = ["name"], unique = true)]
)
data class ProfileEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "profile_id")
    val id: Long = 0,

    @ColumnInfo(name = "name", collate = ColumnInfo.NOCASE)
    val name: String = ""
)