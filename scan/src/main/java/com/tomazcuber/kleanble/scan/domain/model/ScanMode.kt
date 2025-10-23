package com.tomazcuber.kleanble.scan.domain.model

/**
 * Defines the power consumption and latency trade-off for a BLE scan.
 */
enum class ScanMode {
    /**
     * Perform a low-power scan. This mode uses the least amount of energy but has the highest latency.
     * Scans are performed infrequently.
     */
    LOW_POWER,

    /**
     * Perform a balanced scan. This provides a compromise between power consumption and latency.
     */
    BALANCED,

    /**
     * Perform a low-latency scan. This mode has the lowest latency but consumes the most power.
     * It is recommended for short-duration scans or when a fast response is critical.
     */
    LOW_LATENCY,
}