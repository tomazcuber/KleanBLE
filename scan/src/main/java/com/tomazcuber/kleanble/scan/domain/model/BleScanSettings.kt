package com.tomazcuber.kleanble.scan.domain.model

/**
 * Defines the behavior and power consumption of a BLE scan.
 *
 * @property scanMode The desired trade-off between scan latency and battery usage.
 * @property callbackType Defines how scan results are reported.
 * @property cacheTimeoutMillis The duration in milliseconds after which a device is
 * considered expired and removed from the cache if not seen again.
 */
data class BleScanSettings(
    val scanMode: ScanMode,
    val callbackType: ScanCallbackType,
    val cacheTimeoutMillis: Long = 10000L, // Default to 10 seconds
)
