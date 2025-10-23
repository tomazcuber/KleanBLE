package com.tomazcuber.kleanble.scan.data.repository

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tomazcuber.kleanble.scan.data.ScanRepositoryImpl
import com.tomazcuber.kleanble.scan.domain.model.BleScanError
import com.tomazcuber.kleanble.scan.domain.model.BleScanSettings
import com.tomazcuber.kleanble.scan.domain.model.BleScanState
import com.tomazcuber.kleanble.scan.domain.model.ScanCallbackType
import com.tomazcuber.kleanble.scan.domain.model.ScanMode
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class ScanRepositoryImplTest {

    private lateinit var mockContext: Context
    private lateinit var mockBluetoothManager: BluetoothManager
    private lateinit var mockBluetoothAdapter: BluetoothAdapter
    private lateinit var mockBluetoothLeScanner: BluetoothLeScanner

    private lateinit var repository: ScanRepositoryImpl

    @Before
    fun setUp() {
        // Mock the Android Bluetooth Stack
        mockBluetoothLeScanner = mockk(relaxed = true)
        mockBluetoothAdapter = mockk(relaxed = true) {
            every { bluetoothLeScanner } returns mockBluetoothLeScanner
        }
        mockBluetoothManager = mockk {
            every { adapter } returns mockBluetoothAdapter
        }
        mockContext = mockk {
            every { getSystemService(Context.BLUETOOTH_SERVICE) } returns mockBluetoothManager
        }

        // Instantiate the repository with mocks and a test dispatcher
        repository = ScanRepositoryImpl(mockContext, Dispatchers.Main)
    }

    @Test
    fun test_startScan_whenScannerNotAvailable_setsErrorState() = runTest {
        // Arrange
        every { mockBluetoothAdapter.bluetoothLeScanner } returns null
        val settings = BleScanSettings(ScanMode.LOW_LATENCY, ScanCallbackType.ALL_MATCHES)

        // Act
        repository.startScan(settings, emptyList())

        // Assert
        val state = repository.scanState.drop(1).first()
        assertTrue(state is BleScanState.Error)
        expectThat((state as BleScanState.Error).reason).isEqualTo(BleScanError.BLUETOOTH_UNAVAILABLE)
    }

    @Test
    fun test_startScan_whenBluetoothIsDisabled_setsErrorState() = runTest {
        // Arrange
        every { mockBluetoothAdapter.isEnabled } returns false
        val settings = BleScanSettings(ScanMode.LOW_LATENCY, ScanCallbackType.ALL_MATCHES)

        // Act
        repository.startScan(settings, emptyList())

        // Assert
        val state = repository.scanState.drop(1).first()
        assertTrue(state is BleScanState.Error)
        expectThat((state as BleScanState.Error).reason).isEqualTo(BleScanError.BLUETOOTH_DISABLED)
    }

    @Test
    fun test_startScan_withPermissions_callsStartScanAndSetsScanningState() = runTest {
        // Arrange
        every { mockBluetoothAdapter.isEnabled } returns true
        val settings = BleScanSettings(ScanMode.LOW_LATENCY, ScanCallbackType.ALL_MATCHES)

        // Act
        repository.startScan(settings, emptyList())

        // Assert
        val state = repository.scanState.drop(1).first()
        verify(exactly = 1) { mockBluetoothLeScanner.startScan(any<List<ScanFilter>>(), any<ScanSettings>(), any<ScanCallback>()) }
        expectThat(state).isEqualTo(BleScanState.Scanning)
    }

    @Test
    fun test_stopScan_callsStopScanAndSetsIdleState() = runTest {
        every { mockBluetoothAdapter.isEnabled } returns true
        repository.startScan(BleScanSettings(ScanMode.LOW_LATENCY, ScanCallbackType.ALL_MATCHES), emptyList())
        repository.scanState.first { it == BleScanState.Scanning } // Wait until scanning

        // Act
        repository.stopScan()

        // Assert
        val state = repository.scanState.first()
        verify(exactly = 1) { mockBluetoothLeScanner.stopScan(any<ScanCallback>()) }
        expectThat(state).isEqualTo(BleScanState.Idle)
    }
}
