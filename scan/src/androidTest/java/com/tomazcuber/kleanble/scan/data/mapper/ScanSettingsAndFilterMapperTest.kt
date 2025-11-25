package com.tomazcuber.kleanble.scan.data.mapper

import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.os.ParcelUuid
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tomazcuber.kleanble.scan.domain.model.BleScanFilter
import com.tomazcuber.kleanble.scan.domain.model.BleScanSettings
import com.tomazcuber.kleanble.scan.domain.model.ScanCallbackType
import com.tomazcuber.kleanble.scan.domain.model.ScanMode
import org.junit.Test
import org.junit.runner.RunWith
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class ScanSettingsAndFilterMapperTest {
    @Test
    fun test_bleScanSettings_mapsToAndroidScanSettingsCorrectly() {
        // Arrange
        val domainSettings =
            BleScanSettings(
                scanMode = ScanMode.LOW_LATENCY,
                callbackType = ScanCallbackType.ALL_MATCHES,
            )

        // Act
        val androidSettings = domainSettings.toAndroid()

        // Assert
        expectThat(androidSettings.scanMode).isEqualTo(ScanSettings.SCAN_MODE_LOW_LATENCY)
        expectThat(androidSettings.callbackType).isEqualTo(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
    }

    @Test
    fun test_bleScanFilter_mapsToAndroidScanFilterCorrectly() {
        // Arrange
        val serviceUuid = UUID.randomUUID()
        val manufacturerId = 0x004C // Apple
        val manufacturerData = byteArrayOf(0x02, 0x15)

        val domainFilter =
            BleScanFilter(
                deviceName = "TestDevice",
                deviceAddress = "00:11:22:33:44:55",
                serviceUuid = serviceUuid,
                manufacturerData = Pair(manufacturerId, manufacturerData),
            )

        // Act
        val androidFilter = domainFilter.toAndroid()

        // Assert
        expectThat(androidFilter.deviceName).isEqualTo("TestDevice")
        expectThat(androidFilter.deviceAddress).isEqualTo("00:11:22:33:44:55")
        expectThat(androidFilter.serviceUuid).isEqualTo(ParcelUuid(serviceUuid))
        expectThat(androidFilter.manufacturerData).isNotNull().isEqualTo(manufacturerData)
    }

    @Test
    fun test_emptyBleScanFilter_mapsToEmptyAndroidScanFilter() {
        // Arrange
        val domainFilter = BleScanFilter()

        // Act
        val androidFilter = domainFilter.toAndroid()

        // Assert
        expectThat(androidFilter.deviceName).isEqualTo(null)
        expectThat(androidFilter.deviceAddress).isEqualTo(null)
        expectThat(androidFilter.serviceUuid).isEqualTo(null)
        expectThat(androidFilter.manufacturerData).isEqualTo(null)
    }
}
