package com.tomazcuber.kleanble.scan.domain.model

/**
 * A wrapper for a [BleScanResult] that includes caching-specific metadata.
 *
 * @property result The original scan result from the device.
 * @property lastSeen The timestamp (in milliseconds) when this device was last seen.
 */
data class CachedBleScanResult(
    val result: BleScanResult,
    val lastSeen: Long
)
