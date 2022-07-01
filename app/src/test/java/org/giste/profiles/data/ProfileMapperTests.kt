package org.giste.profiles.data

import org.giste.profiles.domain.Profile
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class ProfileMapperTests {

    companion object {
        private val PROFILE = Profile(1, "profile name")
        private val PROFILE_DTO = ProfileEntity(1, "profile name")
    }

    @Test
    fun toDto_returnsProfileDto() {
        val mapper = ProfileMapper()
        val profileDto = mapper.toEntity(PROFILE)

        assertThat(profileDto, equalTo(PROFILE_DTO))
    }

    @Test
    fun toModel_returnsProfile() {
        val mapper = ProfileMapper()
        val profile = mapper.toModel(PROFILE_DTO)

        assertThat(profile, equalTo(PROFILE))
    }
}