package org.giste.profiles.data

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.greaterThan
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SystemPropertiesDataSourceTests {
    private lateinit var systemProperties: SystemPropertiesDataSource

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        systemProperties = SystemPropertiesDataSource(context)
    }

    @Test
    fun init_valuesAreLoaded() {
        assertThat(systemProperties.streamMediaMinValue, equalTo(0))
        assertThat(systemProperties.streamMediaMaxValue, greaterThan(0))

        assertThat(systemProperties.streamRingMinValue, equalTo(0))
        assertThat(systemProperties.streamRingMaxValue, greaterThan(0))

        assertThat(systemProperties.streamNotificationMinValue, equalTo(0))
        assertThat(systemProperties.streamNotificationMaxValue, greaterThan(0))

        assertThat(systemProperties.streamAlarmMinValue, equalTo(1))
        assertThat(systemProperties.streamAlarmMaxValue, greaterThan(0))
    }
}