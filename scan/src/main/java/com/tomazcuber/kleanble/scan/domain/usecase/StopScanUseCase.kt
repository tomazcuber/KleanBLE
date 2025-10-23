package com.tomazcuber.kleanble.scan.domain.usecase

import com.tomazcuber.kleanble.scan.domain.repository.LiveScanDataSource

/**
 * A use case for stopping any ongoing BLE scan.
 */
internal class StopScanUseCase(private val liveScanDataSource: LiveScanDataSource) {
    operator fun invoke() {
        liveScanDataSource.stopScan()
    }
}
