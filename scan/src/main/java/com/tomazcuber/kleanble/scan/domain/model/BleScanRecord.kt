package com.tomazcuber.kleanble.scan.domain.model

import java.util.UUID

/**
 * Represents the parsed advertisement data from a BLE scan result.
 * This model is framework-agnostic and provides access to the most common
 * types of data found in a BLE advertisement packet. It is designed to be
 * immutable and part of the domain layer.
 *
 * @property advertiseFlags The advertisement flags, indicating the discoverability of the device (e.g., LE General Discoverable Mode)
 * @property deviceName The broadcasted name of the device. This can be either the complete or shortened local name. It is null
 * if not present in the advertisement.
 * @property serviceUuids  A list of service UUIDs that the device advertises. This is essential for clients that want to discover
 * devices providing specific services.
 * @property serviceData A map where keys are service UUIDs and values are the corresponding data advertised by that service
 * @property manufacturerSpecificData A map where keys are the 16-bit manufacturer IDs (assigned by the Bluetooth SIG) and values
 * are the custom data payloads.
 * @property transmissionPowerLevel The transmission power level of the advertisement packet in dBm. This can be used in conjunction with
 * RSSI to estimate the distance to the device. It is null if not present.
 * @property rawBytes  The complete, unparsed byte array of the advertisement data. This serves as a critical "escape hatch,"
 * allowing clients to manually parse any custom or less common data types not exposed as first-class properties in this model.
 */

data class BleScanRecord(
    val advertiseFlags: Int,
    val deviceName: String?,
    val serviceUuids: List<UUID>,
    val serviceData: Map<UUID, ByteArray>,
    val manufacturerSpecificData: Map<Int, ByteArray>,
    val transmissionPowerLevel: Int?,
    val rawBytes: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BleScanRecord

        if (advertiseFlags != other.advertiseFlags) return false
        if (transmissionPowerLevel != other.transmissionPowerLevel) return false
        if (deviceName != other.deviceName) return false
        if (serviceUuids != other.serviceUuids) return false
        if (serviceData != other.serviceData) return false
        if (manufacturerSpecificData != other.manufacturerSpecificData) return false
        if (!rawBytes.contentEquals(other.rawBytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = advertiseFlags
        result = 31 * result + (transmissionPowerLevel ?: 0)
        result = 31 * result + (deviceName?.hashCode() ?: 0)
        result = 31 * result + serviceUuids.hashCode()
        result = 31 * result + serviceData.hashCode()
        result = 31 * result + manufacturerSpecificData.hashCode()
        result = 31 * result + rawBytes.contentHashCode()
        return result
    }
}
