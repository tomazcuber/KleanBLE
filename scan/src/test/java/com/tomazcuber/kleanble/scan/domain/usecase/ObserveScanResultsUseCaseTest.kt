package com.tomazcuber.kleanble.scan.domain.usecase

import com.tomazcuber.kleanble.scan.domain.FakeScanRepository
import com.tomazcuber.kleanble.scan.domain.model.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.util.UUID

class ObserveScanResultsUseCaseTest {

    private lateinit var fakeScanRepository: FakeScanRepository
    private lateinit var observeScanResultsUseCase: ObserveScanResultsUseCase

    @BeforeEach
    fun setUp() {
        fakeScanRepository = FakeScanRepository()
        observeScanResultsUseCase = ObserveScanResultsUseCase(fakeScanRepository)
    }

    @Test
    fun `invoke should return the scan results flow from the repository`() = runTest(UnconfinedTestDispatcher()) {
        // Arrange
        val dummyRecord = BleScanRecord(
            advertiseFlags = -1,
            deviceName = "TestDevice",
            serviceUuids = listOf(UUID.randomUUID()),
            serviceData = emptyMap(),
            manufacturerSpecificData = emptyMap(),
            transmissionPowerLevel = -60,
            rawBytes = byteArrayOf(1, 2, 3)
        )
        val dummyDevice = BleScannedDevice(
            name = "TestDevice",
            macAddress = "00:11:22:33:44:55",
            bondState = BondState.NONE,
            deviceType = DeviceType.LE
        )
        val expectedResult = BleScanResult(
            device = dummyDevice,
            rssi = -50,
            scanRecord = dummyRecord,
            timestampNanos = 12345L
        )

        // Act
        val scanResultsFlow = observeScanResultsUseCase()

        // Assert
        // Launch a collector in the background to receive the emission
        val job = launch {
            val actualResult = scanResultsFlow.first()
            expectThat(actualResult).isEqualTo(expectedResult)
        }

        // Emit the value from the fake repository
        fakeScanRepository.emitScanResult(expectedResult)

        // Cancel the collector to clean up resources
        job.cancel()
    }
}
