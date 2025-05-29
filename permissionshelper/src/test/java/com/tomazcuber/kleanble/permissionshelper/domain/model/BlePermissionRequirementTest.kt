package com.tomazcuber.kleanble.permissionshelper.domain.model

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isFalse
import strikt.assertions.isTrue

class BlePermissionRequirementTest {

    @Test
    fun `BluetoothScanSAndAbove MUST be required when on SDK 31 or above`(){
        val requirement = BlePermissionRequirement.BluetoothScanSAndAbove
        val contextAndroid12 = BlePermissionRequirement.CheckContext(sdkVersion = 31)
        val contextAndroid11 = BlePermissionRequirement.CheckContext(sdkVersion = 30)

        expectThat(requirement.isRequired(contextAndroid12)).isTrue()
        expectThat(requirement.isRequired(contextAndroid11)).isFalse()
    }

    @Test
    fun `BluetoothConnectSAndAbove MUST be required when on SDK 31 or above`(){
        val requirement = BlePermissionRequirement.BluetoothConnectSAndAbove
        val contextAndroid12 = BlePermissionRequirement.CheckContext(sdkVersion = 31)
        val contextAndroid11 = BlePermissionRequirement.CheckContext(sdkVersion = 30)

        expectThat(requirement.isRequired(contextAndroid12)).isTrue()
        expectThat(requirement.isRequired(contextAndroid11)).isFalse()
    }

    @Test
    fun `BluetoothAdvertiseSAndAbove MUST be required when on SDK 31 or above`(){
        val requirement = BlePermissionRequirement.BluetoothAdvertiseSAndAbove
        val contextAndroid12 = BlePermissionRequirement.CheckContext(sdkVersion = 31)
        val contextAndroid11 = BlePermissionRequirement.CheckContext(sdkVersion = 30)

        expectThat(requirement.isRequired(contextAndroid12)).isTrue()
        expectThat(requirement.isRequired(contextAndroid11)).isFalse()
    }

    @Test
    fun `BluetoothAdminLegacy MUST be required when on SDK lower than 31`(){
        val requirement = BlePermissionRequirement.BluetoothAdminLegacy
        val contextAndroid12 = BlePermissionRequirement.CheckContext(sdkVersion = 31)
        val contextAndroid11 = BlePermissionRequirement.CheckContext(sdkVersion = 30)

        expectThat(requirement.isRequired(contextAndroid11)).isTrue()
        expectThat(requirement.isRequired(contextAndroid12)).isFalse()
    }

    @Test
    fun `AccessFineLocation MUST be required when on SDK lower than 31`(){
        val requirement = BlePermissionRequirement.AccessFineLocation
        val contextAndroid11 = BlePermissionRequirement.CheckContext(sdkVersion = 30)

        expectThat(requirement.isRequired(contextAndroid11)).isTrue()
    }

    @Test
    fun `AccessFineLocation MUST NOT be required when on SDK higher than 31 and app DOESN'T derive location from scan`() {
        val requirement = BlePermissionRequirement.AccessFineLocation
        val contextAndroid12 = BlePermissionRequirement.CheckContext(
            sdkVersion = 31,
            appDerivesLocationFromScan = false
        )

        expectThat(requirement.isRequired(contextAndroid12)).isFalse()
    }

    @Test
    fun `AccessFineLocation MUST be required when on SDK higher than 31 and app DOES derive location from scan`() {
        val requirement = BlePermissionRequirement.AccessFineLocation
        val contextAndroid12 = BlePermissionRequirement.CheckContext(
            sdkVersion = 31,
            appDerivesLocationFromScan = true
        )

        expectThat(requirement.isRequired(contextAndroid12)).isTrue()
    }
}