package com.tomazcuber.kleanble.scan.api

import com.tomazcuber.kleanble.scan.domain.model.BleScanFilter
import com.tomazcuber.kleanble.scan.domain.model.BleScanResult
import com.tomazcuber.kleanble.scan.domain.model.BleScanSettings
import com.tomazcuber.kleanble.scan.domain.model.BleScanState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * The public-facing API for the KleanBLE scanning feature.
 *
 * This interface serves as the primary entry point for clients wishing to scan for nearby
 * Bluetooth Low Energy devices. It abstracts away all the underlying implementation details,
 * providing a clean, reactive API for starting/stopping scans and observing results.
 */
interface BleScanner {
    /**
     * A hot flow that emits the current state of the BLE scanner (e.g., Idle, Scanning, Error).
     *
     * Clients should collect this flow to be notified of changes in the scanner's status
     * and to update their UI accordingly.
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
     * If a scan is already in progress, it will be replaced by the new one.
     *
     * @param settings The settings to configure the scan's behavior and power consumption.
     * @param filters A list of filters to apply. The scanner will only report devices
     * that match one or more of these filters. If the list is empty, all found devices will be reported.
     */
    fun startScan(
        settings: BleScanSettings,
        filters: List<BleScanFilter> = emptyList(),
    )

    /**
     * Stops any ongoing BLE scan.
     *
     * If no scan is active, this method does nothing. The [scanState] will transition to [BleScanState.Idle].
     */
    fun stopScan()
}
