package com.tomazcuber.kleanble.scan.domain.usecase

import com.tomazcuber.kleanble.scan.domain.model.BleScanFilter
import com.tomazcuber.kleanble.scan.domain.model.BleScanSettings
import com.tomazcuber.kleanble.scan.domain.repository.ScanRepository

/**
 * A use case for starting a BLE scan.
 */
internal class StartScanUseCase(private val scanRepository: ScanRepository) {
    operator fun invoke(
        settings: BleScanSettings,
        filters: List<BleScanFilter> = emptyList()
    ) {
        scanRepository.startScan(settings, filters)
    }
}
