package com.tomazcuber.kleanble.scan.domain.repository

import com.tomazcuber.kleanble.scan.domain.model.BleScanResult
import kotlinx.coroutines.flow.Flow

/**
 * Defines the contract for a data source that caches discovered BLE devices during a scan session.
 */
interface ScanCacheDataSource {

    /**
     * A flow that emits the complete, updated list of cached devices whenever a change occurs.
     */
    val devices: Flow<List<BleScanResult>>

    /**
     * Adds a new scan result to the cache or updates an existing entry.
     * @param bleScanResult The new scan result to process.
     */
    fun addOrUpdate(bleScanResult: BleScanResult)

    /**
     * Removes any devices from the cache that have not been seen for a specified duration.
     * @param timeoutMillis The duration in milliseconds after which a device is considered expired.
     */
    fun pruneExpired(timeoutMillis: Long)

    /**
     * Clears all devices from the cache.
     */
    fun clear()
}
