package org.giste.profiles.data

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AudioManagerSystemRepositoryInstrumentedTests {
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val systemRepository = AudioManagerSystemRepository(context)

    @Test
    fun retrieves_system_values() = runTest {
        val systemProperties = systemRepository.systemProperties.value

        Log.d("AudioManagerSystemRepositoryInstrumentedTests", "System properties: $systemProperties")

        assertTrue(systemProperties.streamMediaMaxValue > 0)
        assertTrue(systemProperties.streamNotificationMaxValue > 0)
        assertTrue(systemProperties.streamRingMaxValue > 0)
        assertTrue(systemProperties.streamAlarmMaxValue > 0)
    }
}