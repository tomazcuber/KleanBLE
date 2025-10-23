package com.tomazcuber.kleanble.scan.domain.model

/**
 * Represents the type of Bluetooth radio supported by a device.
 */
enum class DeviceType {
    /**
     * The device supports only Bluetooth Low Energy (LE).
     */
    LE,

    /**
     * The device supports only Bluetooth Classic (BR/EDR).
     */
    CLASSIC,

    /**
     * The device supports both Bluetooth Classic and Low Energy (dual-mode).
     */
    DUAL,

    /**
     * The device type could not be determined.
     */
    UNKNOWN
}