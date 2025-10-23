package com.tomazcuber.kleanble.scan.data.mapper

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanRecord
import android.bluetooth.le.ScanResult
import android.os.ParcelUuid
import android.util.SparseArray
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tomazcuber.kleanble.scan.domain.model.BondState
import com.tomazcuber.kleanble.scan.domain.model.DeviceType
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.junit.runner.RunWith
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.isNull
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class ScanResultMapperTest {

    @Test
    fun test_fullScanResult_mapsToDomainModelCorrectly() {
        // Arrange
        val serviceUuid = UUID.randomUUID()
        val manufacturerId = 0x004C
        val manufacturerData = byteArrayOf(0x01, 0x02)
        val serviceDataUuid = UUID.randomUUID()
        val serviceData = byteArrayOf(0x03, 0x04)
        val rawBytes = byteArrayOf(0x01, 0x02, 0x03, 0x04, 0x05)

        val mockDevice = mockk<BluetoothDevice> {
            every { name } returns "TestDevice"
            every { address } returns "00:11:22:AA:BB:CC"
            every { bondState } returns BluetoothDevice.BOND_BONDED
            every { type } returns BluetoothDevice.DEVICE_TYPE_DUAL
        }

        val mockScanRecord = mockk<ScanRecord> {
            every { advertiseFlags } returns 0b0110
            every { deviceName } returns "TestDevice"
            every { serviceUuids } returns listOf(ParcelUuid(serviceUuid))
            every { getServiceData(ParcelUuid(serviceDataUuid)) } returns serviceData
            every { this@mockk.serviceData } returns mapOf(ParcelUuid(serviceDataUuid) to serviceData)
            every { manufacturerSpecificData } returns SparseArray<ByteArray>().apply { put(manufacturerId, manufacturerData) }
            every { txPowerLevel } returns -50
            every { bytes } returns rawBytes
        }

        val mockScanResult = mockk<ScanResult> {
            every { device } returns mockDevice
            every { rssi } returns -65
            every { timestampNanos } returns 123456789L
            every { scanRecord } returns mockScanRecord
        }

        // Act
        val domainResult = mockScanResult.toBleScanResult()

        // Assert
        expectThat(domainResult.rssi).isEqualTo(-65)
        expectThat(domainResult.timestampNanos).isEqualTo(123456789L)

        // Assert Device
        val domainDevice = domainResult.device
        expectThat(domainDevice.name).isEqualTo("TestDevice")
        expectThat(domainDevice.macAddress).isEqualTo("00:11:22:AA:BB:CC")
        expectThat(domainDevice.bondState).isEqualTo(BondState.BONDED)
        expectThat(domainDevice.deviceType).isEqualTo(DeviceType.DUAL)

        // Assert Scan Record
        val domainRecord = domainResult.scanRecord
        expectThat(domainRecord.advertiseFlags).isEqualTo(0b0110)
        expectThat(domainRecord.deviceName).isEqualTo("TestDevice")
        expectThat(domainRecord.serviceUuids).isEqualTo(listOf(serviceUuid))
        expectThat(domainRecord.serviceData[serviceDataUuid]).isEqualTo(serviceData)
        expectThat(domainRecord.manufacturerSpecificData[manufacturerId]).isEqualTo(manufacturerData)
        expectThat(domainRecord.transmissionPowerLevel).isEqualTo(-50)
        expectThat(domainRecord.rawBytes).isEqualTo(rawBytes)
    }

    @Test
    fun test_scanResultWithNulls_mapsGracefully() {
        // Arrange
        val mockDevice = mockk<BluetoothDevice> {
            every { name } returns null
            every { address } returns "00:11:22:AA:BB:CC"
            every { bondState } returns BluetoothDevice.BOND_NONE
            every { type } returns BluetoothDevice.DEVICE_TYPE_UNKNOWN
        }

        val mockScanResult = mockk<ScanResult> {
            every { device } returns mockDevice
            every { rssi } returns -70
            every { timestampNanos } returns 98765L
            every { scanRecord } returns null // Test null scan record
        }

        // Act
        val domainResult = mockScanResult.toBleScanResult()

        // Assert
        expectThat(domainResult.device.name).isNull()
        expectThat(domainResult.device.bondState).isEqualTo(BondState.NONE)
        expectThat(domainResult.device.deviceType).isEqualTo(DeviceType.UNKNOWN)

        // Assert that we get a default, empty scan record
        val domainRecord = domainResult.scanRecord
        expectThat(domainRecord.advertiseFlags).isEqualTo(-1)
        expectThat(domainRecord.serviceUuids).isEqualTo(emptyList())
    }
}
