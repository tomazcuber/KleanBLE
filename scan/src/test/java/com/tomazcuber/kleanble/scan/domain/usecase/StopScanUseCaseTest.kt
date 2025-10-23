package com.tomazcuber.kleanble.scan.domain.usecase

import com.tomazcuber.kleanble.scan.domain.FakeScanDataSource
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isTrue

class StopScanUseCaseTest {

    private lateinit var fakeScanDataSource: FakeScanDataSource
    private lateinit var stopScanUseCase: StopScanUseCase

    @BeforeEach
    fun setUp() {
        fakeScanDataSource = FakeScanDataSource()
        stopScanUseCase = StopScanUseCase(fakeScanDataSource)
    }

    @Test
    fun `invoke should call stopScan on the repository`() {
        // Act
        stopScanUseCase()

        // Assert
        expectThat(fakeScanDataSource.stopScanCalled).isTrue()
    }
}
