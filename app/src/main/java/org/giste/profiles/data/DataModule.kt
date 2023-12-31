package org.giste.profiles.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.giste.profiles.domain.ProfileRepository
import org.giste.profiles.domain.SystemProperties
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {
    private val dbName = "profiles.db"

    @Provides
    @Singleton
    fun provideProfilesDb(@ApplicationContext appContext: Context): ProfilesDb {
        return Room.databaseBuilder(
            appContext,
            ProfilesDb::class.java,
            dbName
        ).build()
    }

    @Provides
    @Singleton
    fun provideProfileDao(profilesDb: ProfilesDb): ProfileDao {
        return profilesDb.profileDao()
    }

    @Provides
    @Singleton
    fun provideProfileDetailDao(profilesDb: ProfilesDb): ProfileDetailDao {
        return profilesDb.profileDetailDao()
    }

    @Provides
    @Singleton
    fun provideSettingDao(profilesDb: ProfilesDb): SettingDao {
        return profilesDb.settingDao()
    }

    @Provides
    @Singleton
    fun provideSelectedProfileDao(profilesDb: ProfilesDb): SelectedProfileDao {
        return profilesDb.selectedProfileDao()
    }

    @Provides
    @Singleton
    fun provideProfileRepository(
        profileDao: ProfileDao,
        profileDetailDao: ProfileDetailDao,
        profileMapper: ProfileMapper,
        profileDetailMapper: ProfileDetailMapper,
        selectedProfileDao: SelectedProfileDao,
        settingDao: SettingDao,
        settingMapper: SettingMapper,
        systemSettingsDataSource: SystemSettingsDataSource
    ): ProfileRepository {
        return ProfileRepositoryImpl(
            profileDao = profileDao,
            profileMapper = profileMapper,
            selectedProfileDao = selectedProfileDao,
            profileDetailDao = profileDetailDao,
            profileDetailMapper = profileDetailMapper,
            settingDao = settingDao,
            settingMapper = settingMapper,
            systemSettingsDataSource = systemSettingsDataSource
        )
    }

    @Provides
    @Singleton
    fun provideSystemProperties(@ApplicationContext appContext: Context): SystemProperties {
        return SystemPropertiesDataSource(appContext)
    }

    @Provides
    @Singleton
    fun provideSystemSettings(
        @ApplicationContext appContext: Context,
        systemProperties: SystemProperties,
    ): SystemSettingsDataSource {
        return SystemSettingsDataSource(appContext, systemProperties)
    }
}