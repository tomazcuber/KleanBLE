package com.tomazcuber.kleanble.scan.domain.model

/**
 * Defines the behavior for how BLE scan results are reported.
 */
enum class ScanCallbackType {
    /**
     * A callback is triggered for every advertisement packet received that matches the filter criteria.
     * This is suitable for applications that need to track changes in advertising data or RSSI.
     */
    ALL_MATCHES,

    /**
     * A callback is triggered only for the first advertisement packet received from a given device.
     * Subsequent advertisements from the same device will be ignored.
     */
    FIRST_MATCH,

    /**
     * A callback is triggered when a device that was previously found is no longer being advertised.
     * This requires hardware support and is not guaranteed to be available on all devices.
     */
    MATCH_LOST,
}
