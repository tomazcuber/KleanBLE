package com.tomazcuber.kleanble.scan.data.cache

import com.tomazcuber.kleanble.scan.domain.model.BleScanResult
import com.tomazcuber.kleanble.scan.domain.model.CachedBleScanResult
import com.tomazcuber.kleanble.scan.domain.repository.ScanCacheDataSource
import com.tomazcuber.kleanble.scan.domain.util.Clock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.ConcurrentHashMap

/**
 * An in-memory implementation of the [ScanCacheDataSource].
 *
 * This class is thread-safe and uses a [ConcurrentHashMap] to store scan results.
 */
internal class InMemoryDeviceCache(private val clock: Clock) : ScanCacheDataSource {

    private val _devices = MutableStateFlow<List<BleScanResult>>(emptyList())
    override val devices: Flow<List<BleScanResult>> = _devices.asStateFlow()

    private val cache = ConcurrentHashMap<String, CachedBleScanResult>()

    override fun addOrUpdate(bleScanResult: BleScanResult) {
        val cachedResult = CachedBleScanResult(
            result = bleScanResult,
            lastSeen = clock.currentTimeMillis()
        )
        cache[bleScanResult.device.macAddress] = cachedResult
        emitCurrentList()
    }

    override fun pruneExpired(timeoutMillis: Long) {
        val now = clock.currentTimeMillis()
        var changed = false
        cache.values.forEach { cachedResult ->
            if (now - cachedResult.lastSeen > timeoutMillis) {
                cache.remove(cachedResult.result.device.macAddress)
                changed = true
            }
        }
        if (changed) {
            emitCurrentList()
        }
    }

    override fun clear() {
        cache.clear()
        emitCurrentList()
    }

    private fun emitCurrentList() {
        _devices.value = cache.values.map { it.result }.sortedByDescending { it.rssi }
    }
}
