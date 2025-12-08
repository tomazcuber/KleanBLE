package com.tomazcuber.kleanble.scan.domain.model

import java.util.UUID

/**
 * Represents a set of criteria for filtering BLE scan results
 *
 * An empty BleScanFilter object will match all devices. If multiple properties are set,
 * they are treated with an AND logic (a device must meet all specified criteria).
 * To achieve OR logic, multiple BleScanFilter objects can be used for a single scan.
 *
 * @property deviceName The device's local name to filter for.
 * @property serviceUuid The 48-bit MAC address of the device to filter for (e.g., "00:11:22:AA:BB:CC").
 * @property deviceAddress  A service UUID that must be present in the device's advertisement packet.
 * @property manufacturerData  A pair containing the manufacturer ID and a data prefix that the device's advertisement must match.
 */
data class BleScanFilter(
    val deviceName: String? = null,
    val serviceUuid: UUID? = null,
    val deviceAddress: String? = null,
    val manufacturerData: Pair<Int, ByteArray>? = null,
)
