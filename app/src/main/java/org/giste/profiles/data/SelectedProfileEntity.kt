package org.giste.profiles.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "selected_profile")
data class SelectedProfileEntity(
    @PrimaryKey
    @ColumnInfo(name = "selectedId")
    val selectedId: Long = 0
)