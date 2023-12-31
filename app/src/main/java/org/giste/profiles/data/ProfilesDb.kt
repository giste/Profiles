package org.giste.profiles.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ProfileEntity::class,
        SelectedProfileEntity::class,
        SettingEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ProfilesDb : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun selectedProfileDao(): SelectedProfileDao
    abstract fun settingDao(): SettingDao
    abstract fun profileDetailDao(): ProfileDetailDao
}