package com.tomazcuber.kleanble.scan.domain.repository

import com.tomazcuber.kleanble.scan.domain.model.BleScanFilter
import com.tomazcuber.kleanble.scan.domain.model.BleScanResult
import com.tomazcuber.kleanble.scan.domain.model.BleScanSettings
import com.tomazcuber.kleanble.scan.domain.model.BleScanState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Defines the contract for the live data source of BLE scan results.
 *
 * This interface abstracts the underlying hardware-specific BLE scanning framework,
 * allowing the domain and repository layers to remain decoupled from the platform details.
 */
interface LiveScanDataSource {
    /**
     * A hot flow that emits the current state of the BLE scanner (e.g., Idle, Scanning, Error).
     * Clients can collect this flow to react to changes in the scanner's status.
     */
    val scanState: StateFlow<BleScanState>

    /**
     * A flow that emits [BleScanResult] objects as devices are discovered.
     * This flow is cold and will only start emitting when collected.
     */
    val scanResults: Flow<BleScanResult>

    /**
     * Starts a new BLE scan with the specified configuration.
     *
     * If a scan is already in progress, the ongoing scan will be stopped and a new one will begin.
     *
     * @param settings The settings to configure the scan's behavior and power consumption.
     * @param filters A list of filters to apply. The scanner will only report devices
     * that match one or more of these filters. If the list is empty, all found devices will be reported.
     */
    fun startScan(
        settings: BleScanSettings,
        filters: List<BleScanFilter>,
    )

    /**
     * Stops any ongoing BLE scan.
     * If no scan is active, this method does nothing.
     */
    fun stopScan()
}
