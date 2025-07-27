/*
 * Copyright 2025 Giste Trappiste
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.giste.profiles.data

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AndroidSystemRepositoryInstrumentedTests {

    @Test
    fun retrieves_system_values() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val systemRepository = AndroidSystemRepository(context, StandardTestDispatcher())
        val systemProperties = systemRepository.systemProperties.value

        Log.d("AudioManagerSystemRepositoryInstrumentedTests", "System properties: $systemProperties")

        assertTrue(systemProperties.streamMediaMaxValue > 0)
        assertTrue(systemProperties.streamNotificationMaxValue > 0)
        assertTrue(systemProperties.streamRingMaxValue > 0)
        assertTrue(systemProperties.streamAlarmMaxValue > 0)
    }
}