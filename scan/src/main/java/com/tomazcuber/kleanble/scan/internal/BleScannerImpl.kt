package com.tomazcuber.kleanble.scan.internal

import com.tomazcuber.kleanble.scan.api.BleScanner
import com.tomazcuber.kleanble.scan.domain.model.BleScanFilter
import com.tomazcuber.kleanble.scan.domain.model.BleScanResult
import com.tomazcuber.kleanble.scan.domain.model.BleScanSettings
import com.tomazcuber.kleanble.scan.domain.model.BleScanState
import com.tomazcuber.kleanble.scan.domain.usecase.ObserveScanResultsUseCase
import com.tomazcuber.kleanble.scan.domain.usecase.ObserveScanStateUseCase
import com.tomazcuber.kleanble.scan.domain.usecase.StartScanUseCase
import com.tomazcuber.kleanble.scan.domain.usecase.StopScanUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Default implementation of the [BleScanner] interface.
 *
 * This class acts as a façade, delegating its responsibilities to the specific
 * use cases that contain the business logic.
 */
internal class BleScannerImpl(
    private val observeScanStateUseCase: ObserveScanStateUseCase,
    private val observeScanResultsUseCase: ObserveScanResultsUseCase,
    private val startScanUseCase: StartScanUseCase,
    private val stopScanUseCase: StopScanUseCase,
) : BleScanner {
    override val scanState: StateFlow<BleScanState> by lazy {
        observeScanStateUseCase()
    }

    override val scanResults: Flow<List<BleScanResult>> by lazy {
        observeScanResultsUseCase()
    }

    override fun startScan(
        settings: BleScanSettings,
        filters: List<BleScanFilter>,
    ) {
        startScanUseCase(settings, filters)
    }

    override fun stopScan() {
        stopScanUseCase()
    }
}
