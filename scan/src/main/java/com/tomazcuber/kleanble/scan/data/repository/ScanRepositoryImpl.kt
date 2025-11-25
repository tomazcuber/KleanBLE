package com.tomazcuber.kleanble.scan.data.repository

import com.tomazcuber.kleanble.scan.domain.model.BleScanFilter
import com.tomazcuber.kleanble.scan.domain.model.BleScanResult
import com.tomazcuber.kleanble.scan.domain.model.BleScanSettings
import com.tomazcuber.kleanble.scan.domain.model.BleScanState
import com.tomazcuber.kleanble.scan.domain.repository.LiveScanDataSource
import com.tomazcuber.kleanble.scan.domain.repository.ScanCacheDataSource
import com.tomazcuber.kleanble.scan.domain.repository.ScanRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * The default implementation of the [ScanRepository].
 *
 * This class orchestrates the live data from the [LiveScanDataSource] and the cached data
 * from the [ScanCacheDataSource] to provide a stable, curated list of discovered devices.
 */
internal class ScanRepositoryImpl(
    private val liveDataSource: LiveScanDataSource,
    private val cacheDataSource: ScanCacheDataSource,
    private val dispatcher: CoroutineDispatcher,
) : ScanRepository {
    private var scanJob: Job? = null

    override val scanState: StateFlow<BleScanState> = liveDataSource.scanState
    override val scanResults: Flow<List<BleScanResult>> = cacheDataSource.devices

    override fun startScan(
        settings: BleScanSettings,
        filters: List<BleScanFilter>,
    ) {
        scanJob?.cancel()
        cacheDataSource.clear()

        scanJob =
            CoroutineScope(dispatcher).launch {
                // Launch a job to listen to the live data source and populate the cache
                launch {
                    liveDataSource.scanResults.collect { result ->
                        cacheDataSource.addOrUpdate(result)
                    }
                }

                // Launch a job to periodically prune the cache
                launch {
                    while (isActive) {
                        delay(settings.cacheTimeoutMillis / 2) // Prune more frequently than the timeout
                        cacheDataSource.pruneExpired(settings.cacheTimeoutMillis)
                    }
                }
            }

        // Finally, start the underlying hardware scan
        liveDataSource.startScan(settings, filters)
    }

    override fun stopScan() {
        scanJob?.cancel()
        liveDataSource.stopScan()
        cacheDataSource.clear()
    }
}
