package com.tomazcuber.kleanble.scan.domain.usecase

import com.tomazcuber.kleanble.scan.domain.repository.ScanRepository

/**
 * A use case for stopping any ongoing BLE scan.
 */
internal class StopScanUseCase(
    private val scanRepository: ScanRepository,
) {
    operator fun invoke() {
        scanRepository.stopScan()
    }
}
