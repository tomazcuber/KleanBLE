package com.tomazcuber.kleanble.scan.domain.model

/**
 * Represents a Bluetooth device discovered during a scan.
 *
 * @property name The name of the device as broadcasted or cached. Can be null if no name is available.
 * @property macAddress The unique MAC address of the device, which is used to identify and connect to it.
 * @property bondState The pairing state of the device.
 * @property deviceType The Bluetooth radio type supported by the device.
 */
data class BleScannedDevice(
    val name: String?,
    val macAddress: String,
    val bondState: BondState,
    val deviceType: DeviceType
)
