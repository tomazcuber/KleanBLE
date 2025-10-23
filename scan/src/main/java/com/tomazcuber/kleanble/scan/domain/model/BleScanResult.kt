package com.tomazcuber.kleanble.scan.domain.model

/**
 * Represents a single advertisement event from a BLE device.
 *
 * @property device The device that was scanned.
 * @property rssi The received signal strength in dBm. The higher the value, the stronger the signal.
 * @property scanRecord The parsed advertisement data.
 * @property timestampNanos The timestamp in nanoseconds when the scan result was observed.
 */
data class BleScanResult(
    val device: BleScannedDevice,
    val rssi: Int,
    val scanRecord: BleScanRecord,
    val timestampNanos: Long
)
