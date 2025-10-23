package com.tomazcuber.kleanble.scan.data.mapper

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanRecord
import android.bluetooth.le.ScanResult
import android.os.ParcelUuid
import com.tomazcuber.kleanble.scan.domain.model.BleScanRecord
import com.tomazcuber.kleanble.scan.domain.model.BleScanResult
import com.tomazcuber.kleanble.scan.domain.model.BleScannedDevice
import com.tomazcuber.kleanble.scan.domain.model.BondState
import com.tomazcuber.kleanble.scan.domain.model.DeviceType
import androidx.core.util.size

@SuppressLint("MissingPermission")
fun ScanResult.toBleScanResult(): BleScanResult {
    val bleScannedDevice = BleScannedDevice(
        name = device.name,
        macAddress = device.address,
        bondState = device.bondState.toBondState(),
        deviceType = device.type.toDeviceType(),
    )

    val bleScanRecord = scanRecord?.toBleScanRecord() ?: BleScanRecord(
        advertiseFlags = -1,
        deviceName = null,
        serviceUuids = emptyList(),
        serviceData = emptyMap(),
        manufacturerSpecificData = emptyMap(),
        transmissionPowerLevel = null,
        rawBytes = byteArrayOf()
    )

    return BleScanResult(
        device = bleScannedDevice,
        rssi = rssi,
        scanRecord = bleScanRecord,
        timestampNanos = timestampNanos
    )
}

private fun Int.toDeviceType(): DeviceType = when (this) {
    BluetoothDevice.DEVICE_TYPE_LE -> DeviceType.LE
    BluetoothDevice.DEVICE_TYPE_CLASSIC -> DeviceType.CLASSIC
    BluetoothDevice.DEVICE_TYPE_DUAL -> DeviceType.DUAL
    else -> DeviceType.UNKNOWN
}

private fun Int.toBondState(): BondState = when (this) {
    BluetoothDevice.BOND_BONDED -> BondState.BONDED
    BluetoothDevice.BOND_BONDING -> BondState.BONDING
    else -> BondState.NONE
}


private fun ScanRecord.toBleScanRecord(): BleScanRecord {
    val serviceUuids = this.serviceUuids?.map(ParcelUuid::getUuid) ?: emptyList()
    val serviceData = this.serviceData?.mapKeys { it.key.uuid } ?: emptyMap()

    val manufacturerData = mutableMapOf<Int, ByteArray>()
    for (i in 0 until (this.manufacturerSpecificData?.size ?: 0)) {
        manufacturerData[this.manufacturerSpecificData.keyAt(i)] = this.manufacturerSpecificData.valueAt(i)
    }

    return BleScanRecord(
        advertiseFlags = this.advertiseFlags,
        deviceName = this.deviceName,
        serviceUuids = serviceUuids,
        serviceData = serviceData,
        manufacturerSpecificData = manufacturerData,
        transmissionPowerLevel = if (this.txPowerLevel == Int.MIN_VALUE) null else this.txPowerLevel,
        rawBytes = this.bytes
    )
}
