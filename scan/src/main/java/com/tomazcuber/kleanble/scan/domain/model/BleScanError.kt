package com.tomazcuber.kleanble.scan.domain.model

/**
 * Represents the possible reasons for a BLE scan failure.
 */
enum class BleScanError {
    /**
     * The scan failed because the device hardware does not support Bluetooth.
     */
    BLUETOOTH_UNAVAILABLE,

    /**
     * The scan failed because Bluetooth is currently disabled.
     */
    BLUETOOTH_DISABLED,

    /**
     * The scan failed because the application lacks the required runtime permissions.
     */
    MISSING_PERMISSIONS,

    /**
     * A scan with the same settings is already started.
     */
    SCAN_FAILED_ALREADY_STARTED,

    /**
     * The app could not be registered for scanning.
     */
    SCAN_FAILED_APPLICATION_REGISTRATION_FAILED,

    /**
     * The hardware does not support the requested scan settings.
     */
    SCAN_FAILED_FEATURE_UNSUPPORTED,

    /**
     * The scan failed due to an internal error in the underlying Android framework.
     */
    SCAN_FAILED_INTERNAL_ERROR,

    /**
     * An unknown error occurred.
     */
    UNKNOWN,
}
