package com.tomazcuber.kleanble.scan

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tomazcuber.kleanble.scan.api.BleScannerFactory
import com.tomazcuber.kleanble.scan.domain.model.BleScanSettings
import com.tomazcuber.kleanble.scan.domain.model.BleScanState
import com.tomazcuber.kleanble.scan.domain.model.ScanCallbackType
import com.tomazcuber.kleanble.scan.domain.model.ScanMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotEqualTo

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class BleScannerIntegrationTest {

    @Test
    fun test_startScan_changesStateFromIdle() =
        runTest {
            // Use the public Factory to instantiate the BleScanner.
            // This verifies the production initialization path (Isolated Koin Context).
            val context = ApplicationProvider.getApplicationContext<Context>()
            val bleScanner = BleScannerFactory.create(context)

            // Arrange
            val settings = BleScanSettings(ScanMode.LOW_POWER, ScanCallbackType.ALL_MATCHES)
            // Confirm our initial state is Idle.
            val initialState = bleScanner.scanState.first()
            expectThat(initialState).isEqualTo(BleScanState.Idle)

            // Act
            bleScanner.startScan(settings)

            // Assert
            // Drop the initial Idle state and wait for the new state.
            val stateAfterScan = bleScanner.scanState.drop(1).first()

            // We expect the state to change from Idle. On a real device, this could be
            // Scanning or an Error (e.g., if Bluetooth is off). For this test, we just
            // confirm it's not Idle anymore, proving the call went through all the layers.
            expectThat(stateAfterScan).isNotEqualTo(BleScanState.Idle)
        }
}
