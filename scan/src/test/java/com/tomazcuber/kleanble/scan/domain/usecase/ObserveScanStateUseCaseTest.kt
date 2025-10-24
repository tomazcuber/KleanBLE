package com.tomazcuber.kleanble.scan.domain.usecase

import com.tomazcuber.kleanble.scan.domain.FakeScanRepository
import com.tomazcuber.kleanble.scan.domain.model.BleScanError
import com.tomazcuber.kleanble.scan.domain.model.BleScanState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@OptIn(ExperimentalCoroutinesApi::class)
class ObserveScanStateUseCaseTest {

    private lateinit var fakeScanRepository: FakeScanRepository
    private lateinit var observeScanStateUseCase: ObserveScanStateUseCase

    @BeforeEach
    fun setUp() {
        fakeScanRepository = FakeScanRepository()
        observeScanStateUseCase = ObserveScanStateUseCase(fakeScanRepository)
    }

    @Test
    fun `invoke should return the scan state flow from the repository`() = runTest {
        // Arrange
        val expectedState = BleScanState.Scanning
        fakeScanRepository.setScanState(expectedState)

        // Act
        val scanStateFlow = observeScanStateUseCase()

        // Assert
        val actualState = scanStateFlow.first()
        expectThat(actualState).isEqualTo(expectedState)
    }

    @Test
    fun `invoke should return error state when repository is in error state`() = runTest {
        // Arrange
        val expectedErrorReason = BleScanError.BLUETOOTH_DISABLED
        val expectedState = BleScanState.Error(expectedErrorReason)
        fakeScanRepository.setScanState(expectedState)

        // Act
        val scanStateFlow = observeScanStateUseCase()

        // Assert
        val actualState = scanStateFlow.first()
        assertTrue(actualState is BleScanState.Error)
        expectThat((actualState as BleScanState.Error).reason).isEqualTo(expectedErrorReason)
    }
}
