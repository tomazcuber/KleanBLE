package com.tomazcuber.kleanble.scan.domain.usecase

import com.tomazcuber.kleanble.scan.domain.model.BleScanState
import com.tomazcuber.kleanble.scan.domain.repository.ScanRepository
import kotlinx.coroutines.flow.StateFlow

/**
 * A use case that provides access to the current state of the BLE scanner.
 */
internal class ObserveScanStateUseCase(
    private val scanRepository: ScanRepository,
) {
    operator fun invoke(): StateFlow<BleScanState> = scanRepository.scanState
}
