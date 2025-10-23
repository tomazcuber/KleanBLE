package com.tomazcuber.kleanble.scan.domain.model

/**
 * Settings to define the behavior and power consumption of the scan.
 *
 * @property scanMode enum (ScanMode) to specify the desired trade-off between scan latency and battery usage (e.g., LOW_POWER, BALANCED,
 * LOW_LATENCY).
 * @property callbackType enum (ScanCallbackType) to define when to notify the client (e.g., ALL_MATCHES for every advertisement, or
 *  FIRST_MATCH for only the first time a device is seen).
 */
data class BleScanSettings(
    val scanMode: ScanMode,
    val callbackType: ScanCallbackType
)
