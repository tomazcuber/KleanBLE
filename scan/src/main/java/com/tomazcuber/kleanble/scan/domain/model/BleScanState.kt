package com.tomazcuber.kleanble.scan.domain.model

/**
 * Represents the various states of the BLE scanner.
 */
sealed interface BleScanState {
    /**
     * The scanner is initialized but not actively scanning for devices.
     * This is the default state.
     */
    data object Idle : BleScanState

    /**
     * The scanner is actively searching for nearby devices that match the filter criteria.
     */
    data object Scanning: BleScanState

    /**
     * The scanner has stopped due to a failure.
     * @property reason The specific reason for the error, as defined in [BleScanError].
     */
    data class Error(val reason: BleScanError) : BleScanState
}