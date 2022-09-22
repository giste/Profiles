package org.giste.profiles.data

import org.giste.profiles.domain.Profile
import org.giste.profiles.domain.ProfileDetail
import javax.inject.Inject

class ProfileMapper @Inject constructor() {
    fun toEntity(profile: Profile): ProfileEntity {
        with(profile) {
            return ProfileEntity(id, name)
        }
    }

    fun toEntity(profile: ProfileDetail): ProfileEntity {
        with(profile) {
            return ProfileEntity(id, name)
        }
    }

    fun toModel(profileEntity: ProfileEntity): Profile {
        with(profileEntity) {
            return Profile(id, name)
        }
    }
}