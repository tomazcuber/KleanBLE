package com.tomazcuber.kleanble.scan.data.mapper

import android.bluetooth.le.ScanFilter
import android.os.ParcelUuid
import com.tomazcuber.kleanble.scan.domain.model.BleScanFilter

internal fun BleScanFilter.toAndroid(): ScanFilter {
    val builder = ScanFilter.Builder()
    this.deviceName?.let { builder.setDeviceName(it) }
    this.deviceAddress?.let { builder.setDeviceAddress(it) }
    this.serviceUuid?.let { builder.setServiceUuid(ParcelUuid(it)) }
    this.manufacturerData?.let { (id, data) -> builder.setManufacturerData(id, data) }
    return builder.build()
}
