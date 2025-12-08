package com.tomazcuber.kleanble.scan.data.mapper

import android.bluetooth.le.ScanSettings
import com.tomazcuber.kleanble.scan.domain.model.BleScanSettings
import com.tomazcuber.kleanble.scan.domain.model.ScanCallbackType
import com.tomazcuber.kleanble.scan.domain.model.ScanMode

internal fun BleScanSettings.toAndroid(): ScanSettings =
    ScanSettings
        .Builder()
        .setScanMode(this.scanMode.toAndroid())
        .setCallbackType(this.callbackType.toAndroid())
        .build()

internal fun ScanMode.toAndroid(): Int =
    when (this) {
        ScanMode.LOW_POWER -> ScanSettings.SCAN_MODE_LOW_POWER
        ScanMode.BALANCED -> ScanSettings.SCAN_MODE_BALANCED
        ScanMode.LOW_LATENCY -> ScanSettings.SCAN_MODE_LOW_LATENCY
    }

internal fun ScanCallbackType.toAndroid(): Int =
    when (this) {
        ScanCallbackType.ALL_MATCHES -> ScanSettings.CALLBACK_TYPE_ALL_MATCHES
        ScanCallbackType.FIRST_MATCH -> ScanSettings.CALLBACK_TYPE_FIRST_MATCH
        ScanCallbackType.MATCH_LOST -> ScanSettings.CALLBACK_TYPE_MATCH_LOST
    }
