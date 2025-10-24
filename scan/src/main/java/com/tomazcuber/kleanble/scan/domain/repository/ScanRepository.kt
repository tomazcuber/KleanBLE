package com.tomazcuber.kleanble.scan.domain.repository

import com.tomazcuber.kleanble.scan.domain.model.BleScanFilter
import com.tomazcuber.kleanble.scan.domain.model.BleScanResult
import com.tomazcuber.kleanble.scan.domain.model.BleScanSettings
import com.tomazcuber.kleanble.scan.domain.model.BleScanState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * The primary repository for the scan feature.
 *
 * This interface acts as the single source of truth for scanning, orchestrating live data
 * from the hardware with a caching layer to provide a clean, stable list of discovered devices.
 */
interface ScanRepository {

    /**
     * A hot flow that emits the current state of the BLE scanner (e.g., Idle, Scanning, Error).
     */
    val scanState: StateFlow<BleScanState>

    /**
     * A flow that emits a complete and curated list of [BleScanResult] objects.
     * This list represents all devices currently considered "active" by the cache.
     */
    val scanResults: Flow<List<BleScanResult>>

    /**
     * Starts a new BLE scan with the specified configuration.
     *
     * @param settings The settings to configure the scan, including the cache timeout.
     * @param filters A list of filters to apply.
     */
    fun startScan(settings: BleScanSettings, filters: List<BleScanFilter>)

    /**
     * Stops any ongoing BLE scan.
     */
    fun stopScan()
}
