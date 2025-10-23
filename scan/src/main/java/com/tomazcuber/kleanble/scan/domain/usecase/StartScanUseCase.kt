package com.tomazcuber.kleanble.scan.domain.usecase

import com.tomazcuber.kleanble.scan.domain.model.BleScanFilter
import com.tomazcuber.kleanble.scan.domain.model.BleScanSettings
import com.tomazcuber.kleanble.scan.domain.repository.LiveScanDataSource

/**
 * A use case for starting a BLE scan.
 */
internal class StartScanUseCase(private val liveScanDataSource: LiveScanDataSource) {
    operator fun invoke(
        settings: BleScanSettings,
        filters: List<BleScanFilter> = emptyList()
    ) {
        liveScanDataSource.startScan(settings, filters)
    }
}
