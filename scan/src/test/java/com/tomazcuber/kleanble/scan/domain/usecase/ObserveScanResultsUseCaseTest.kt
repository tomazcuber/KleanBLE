package com.tomazcuber.kleanble.scan.domain.usecase

import com.tomazcuber.kleanble.scan.domain.FakeScanRepository
import com.tomazcuber.kleanble.scan.domain.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class ObserveScanResultsUseCaseTest {

    private lateinit var fakeScanRepository: FakeScanRepository
    private lateinit var observeScanResultsUseCase: ObserveScanResultsUseCase

    @BeforeEach
    fun setUp() {
        fakeScanRepository = FakeScanRepository()
        observeScanResultsUseCase = ObserveScanResultsUseCase(fakeScanRepository)
    }

    @Test
    fun `invoke should return the scan results flow from the repository`() = runTest {
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
        val expectedList = listOf(expectedResult)

        // Act
        val scanResultsFlow = observeScanResultsUseCase()
        fakeScanRepository.emitScanResultList(expectedList)

        // Assert
        val actualList = scanResultsFlow.first()
        expectThat(actualList).isEqualTo(expectedList)
    }
}
