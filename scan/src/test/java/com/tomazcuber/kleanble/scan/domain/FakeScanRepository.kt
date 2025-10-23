package com.tomazcuber.kleanble.scan.domain

import com.tomazcuber.kleanble.scan.domain.model.BleScanFilter
import com.tomazcuber.kleanble.scan.domain.model.BleScanResult
import com.tomazcuber.kleanble.scan.domain.model.BleScanSettings
import com.tomazcuber.kleanble.scan.domain.model.BleScanState
import com.tomazcuber.kleanble.scan.domain.repository.ScanRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * A fake implementation of the [ScanRepository] for use in unit tests.
 *
 * This class allows for controlling the state and results flows and for verifying
 * that repository methods are called as expected.
 */
internal class FakeScanRepository : ScanRepository {

    private val _scanState = MutableStateFlow<BleScanState>(BleScanState.Idle)
    override val scanState = _scanState.asStateFlow()

    private val _scanResults = MutableSharedFlow<BleScanResult>()
    override val scanResults = _scanResults.asSharedFlow()

    var startScanCalled = false
        private set
    var stopScanCalled = false
        private set

    // Argument captors
    var receivedSettings: BleScanSettings? = null
        private set
    var receivedFilters: List<BleScanFilter>? = null
        private set

    override fun startScan(settings: BleScanSettings, filters: List<BleScanFilter>) {
        startScanCalled = true
        receivedSettings = settings
        receivedFilters = filters
    }

    override fun stopScan() {
        stopScanCalled = true
    }

    // Helper methods for tests to control the fake
    fun setScanState(state: BleScanState) {
        _scanState.value = state
    }

    suspend fun emitScanResult(result: BleScanResult) {
        _scanResults.emit(result)
    }
}
