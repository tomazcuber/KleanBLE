package com.tomazcuber.kleanble.scan.domain.usecase

import com.tomazcuber.kleanble.scan.domain.FakeScanRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isTrue

class StopScanUseCaseTest {
    private lateinit var fakeScanRepository: FakeScanRepository
    private lateinit var stopScanUseCase: StopScanUseCase

    @BeforeEach
    fun setUp() {
        fakeScanRepository = FakeScanRepository()
        stopScanUseCase = StopScanUseCase(fakeScanRepository)
    }

    @Test
    fun `invoke should call stopScan on the repository`() {
        // Act
        stopScanUseCase()

        // Assert
        expectThat(fakeScanRepository.stopScanCalled).isTrue()
    }
}
