package com.tomazcuber.kleanble.scan

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tomazcuber.kleanble.scan.api.BleScanner
import com.tomazcuber.kleanble.scan.di.scanModule
import com.tomazcuber.kleanble.scan.domain.model.BleScanSettings
import com.tomazcuber.kleanble.scan.domain.model.BleScanState
import com.tomazcuber.kleanble.scan.domain.model.ScanCallbackType
import com.tomazcuber.kleanble.scan.domain.model.ScanMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotEqualTo

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class BleScannerIntegrationTest : KoinTest {

    // Lazily inject the BleScanner instance. Koin will build the entire object graph.
    private val bleScanner: BleScanner by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        // Provide the application context to Koin, which is needed by ScanRepositoryImpl.
        androidContext(ApplicationProvider.getApplicationContext())
        // Load our scanModule, which contains all the definitions for the feature.
        modules(scanModule)
    }

    @Test
    fun test_startScan_changesStateFromIdle() = runTest {
        // This integration test proves that Koin can build the entire object graph
        // (BleScanner -> UseCases -> Repository) and that the basic state machine responds.

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