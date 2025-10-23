package com.tomazcuber.kleanble.scan.domain.usecase

import com.tomazcuber.kleanble.scan.domain.model.BleScanResult
import com.tomazcuber.kleanble.scan.domain.repository.ScanRepository
import kotlinx.coroutines.flow.Flow

/**
 * A use case that provides a flow of discovered BLE devices.
 */
internal class ObserveScanResultsUseCase(private val scanRepository: ScanRepository) {
    operator fun invoke(): Flow<BleScanResult> = scanRepository.scanResults
}
