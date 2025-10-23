package com.tomazcuber.kleanble.scan.domain.usecase

import com.tomazcuber.kleanble.scan.domain.model.BleScanResult
import com.tomazcuber.kleanble.scan.domain.repository.LiveScanDataSource
import kotlinx.coroutines.flow.Flow

/**
 * A use case that provides a flow of discovered BLE devices.
 */
internal class ObserveScanResultsUseCase(private val liveScanDataSource: LiveScanDataSource) {
    operator fun invoke(): Flow<BleScanResult> = liveScanDataSource.scanResults
}
