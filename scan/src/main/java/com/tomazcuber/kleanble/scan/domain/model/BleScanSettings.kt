package com.tomazcuber.kleanble.scan.domain.model

/**
 * Settings to define the behavior and power consumption of the scan.
 *
 * @property scanMode enum (ScanMode) to specify the desired trade-off between scan latency and battery usage (e.g., LOW_POWER, BALANCED,
 * LOW_LATENCY).
 * @property callbackType enum (ScanCallbackType) to define when to notify the client (e.g., ALL_MATCHES for every advertisement, or
 *  FIRST_MATCH for only the first time a device is seen).
 */
/**
 * Defines the behavior and power consumption of a BLE scan.
 *
 * @property scanMode The desired trade-off between scan latency and battery usage.
 * @property callbackType Defines how scan results are reported.
 * @property cacheTimeoutMillis The duration in milliseconds after which a device is considered expired and removed from the cache if not seen again.
 */
data class BleScanSettings(
    val scanMode: ScanMode,
    val callbackType: ScanCallbackType,
    val cacheTimeoutMillis: Long = 10000L // Default to 10 seconds
)
