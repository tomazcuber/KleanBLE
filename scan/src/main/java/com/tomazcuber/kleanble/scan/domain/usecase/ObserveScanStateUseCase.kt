package com.tomazcuber.kleanble.scan.domain.usecase

import com.tomazcuber.kleanble.scan.domain.model.BleScanState
import com.tomazcuber.kleanble.scan.domain.repository.LiveScanDataSource
import kotlinx.coroutines.flow.StateFlow

/**
 * A use case that provides access to the current state of the BLE scanner.
 */
internal class ObserveScanStateUseCase(private val liveScanDataSource: LiveScanDataSource) {
    operator fun invoke(): StateFlow<BleScanState> = liveScanDataSource.scanState
}
