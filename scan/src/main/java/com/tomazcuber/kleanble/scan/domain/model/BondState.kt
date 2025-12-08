package com.tomazcuber.kleanble.scan.domain.model

/**
 * Represents the pairing (bond) state of a Bluetooth device.
 */
enum class BondState {
    /**
     * The device is not paired.
     */
    NONE,

    /**
     * A pairing process is currently in progress with the device.
     */
    BONDING,

    /**
     * The device is paired.
     */
    BONDED,
}
