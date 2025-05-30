package com.tomazcuber.kleanble.permissionshelper.domain.usecase

import android.Manifest
import com.tomazcuber.kleanble.common.domain.model.BleOperation
import com.tomazcuber.kleanble.permissionshelper.domain.model.BlePermissionRequirement
import com.tomazcuber.kleanble.permissionshelper.domain.repository.BuildVersionRepository
import com.tomazcuber.kleanble.permissionshelper.domain.repository.PermissionRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.containsExactlyInAnyOrder
import strikt.assertions.isEmpty
import strikt.assertions.isFalse
import strikt.assertions.isTrue


class GetPermissionsReportForBleOperationUseCaseTest {

    private lateinit var mockPermissionRepository: PermissionRepository
    private lateinit var mockBuildVersionRepository: BuildVersionRepository
    private lateinit var getPermissionsReportUseCase: GetPermissionsReportForBleOperationUseCase

    @BeforeEach
    fun setUp(){
        mockPermissionRepository = mockk()
        mockBuildVersionRepository = mockk()
        getPermissionsReportUseCase = GetPermissionsReportForBleOperationUseCase(mockPermissionRepository, mockBuildVersionRepository)
    }

    @Nested
    @DisplayName("Tests for Android 12+ (>= SDK 31)")
    inner class Android12PlusTests {

        private val sdkVersion = 31

        @BeforeEach
        fun setup() {
            every { mockBuildVersionRepository.getCurrentSdkLevel() } returns sdkVersion
        }

        @Test
        @DisplayName("SCAN: WHEN all permissions are granted MUST return a report without missing permissions")
        fun  `scan operation with all permissions granted`() = runTest {
            val operations = setOf(BleOperation.SCAN_DEVICES)

            every { mockBuildVersionRepository.getCurrentSdkLevel() } returns 31
            coEvery { mockPermissionRepository.isPermissionGranted(any()) } returns true

            val permissionsReport = getPermissionsReportUseCase(operations)

            expectThat(permissionsReport){
                get { requiredPermissionsGranted }.isTrue()
                get { missingPermissions }.isEmpty()
                get { grantedPermissions }.containsExactlyInAnyOrder(
                    BlePermissionRequirement.BluetoothScanSAndAbove
                )
            }
        }

        @Test
        @DisplayName("SCAN: WHEN any permissions are missing MUST return a report with missing permissions")
        fun `scan operation with missing permissions`() = runTest {
          
            val operations = setOf(BleOperation.SCAN_DEVICES)
            coEvery { mockPermissionRepository.isPermissionGranted(Manifest.permission.BLUETOOTH_SCAN) } returns false
            coEvery { mockPermissionRepository.isPermissionGranted(Manifest.permission.BLUETOOTH_CONNECT) } returns true

            
            val permissionsReport = getPermissionsReportUseCase(operations)

            expectThat(permissionsReport) {
                get { requiredPermissionsGranted }.isFalse()
                get { missingPermissions }.containsExactlyInAnyOrder(BlePermissionRequirement.BluetoothScanSAndAbove)
            }
        }

        @Test
        @DisplayName("CONNECT: WHEN all permissions are granted MUST return a report without missing permissions")
        fun  `connect operation with all permissions granted`() = runTest {
            val operations = setOf(BleOperation.CONNECT_AND_COMMUNICATE)

            every { mockBuildVersionRepository.getCurrentSdkLevel() } returns 31
            coEvery { mockPermissionRepository.isPermissionGranted(Manifest.permission.BLUETOOTH_CONNECT) } returns true

            val permissionsReport = getPermissionsReportUseCase(operations)

            expectThat(permissionsReport){
                get { requiredPermissionsGranted }.isTrue()
                get { missingPermissions }.isEmpty()
                get { grantedPermissions }.containsExactlyInAnyOrder(BlePermissionRequirement.BluetoothConnectSAndAbove)
            }
        }

        @Test
        @DisplayName("CONNECT: WHEN any permissions are missing MUST return a report with missing permissions")
        fun `connect operation with missing permissions`() = runTest {
          
            val operations = setOf(BleOperation.CONNECT_AND_COMMUNICATE)
            coEvery { mockPermissionRepository.isPermissionGranted(Manifest.permission.BLUETOOTH_CONNECT) } returns false

            
            val permissionsReport = getPermissionsReportUseCase(operations)

            expectThat(permissionsReport) {
                get { requiredPermissionsGranted }.isFalse()
                get { missingPermissions }.containsExactlyInAnyOrder(BlePermissionRequirement.BluetoothConnectSAndAbove)
                get { grantedPermissions }.isEmpty()
            }
        }

        @Test
        @DisplayName("ADVERTISE:  WHEN all permissions are granted MUST return a report without missing permissions")
        fun `advertise operation with permission granted`() = runTest {
          
            val operations = setOf(BleOperation.ADVERTISE_AS_SERVER)
            coEvery { mockPermissionRepository.isPermissionGranted(Manifest.permission.BLUETOOTH_ADVERTISE) } returns true

            
            val permissionsReport = getPermissionsReportUseCase(operations)

            expectThat(permissionsReport) {
                get { requiredPermissionsGranted }.isTrue()
                get { missingPermissions }.isEmpty()
                get { grantedPermissions }.containsExactlyInAnyOrder(BlePermissionRequirement.BluetoothAdvertiseSAndAbove)
            }
        }

        @Test
        @DisplayName("ADVERTISE: WHEN any permissions are missing MUST return a report with missing permissions")
        fun `advertise operation with permission missing`() = runTest {
          
            val operations = setOf(BleOperation.ADVERTISE_AS_SERVER)
            coEvery { mockPermissionRepository.isPermissionGranted(Manifest.permission.BLUETOOTH_ADVERTISE) } returns false

            
            val permissionsReport = getPermissionsReportUseCase(operations)

            expectThat(permissionsReport) {
                get { requiredPermissionsGranted }.isFalse()
                get { missingPermissions }.containsExactlyInAnyOrder(BlePermissionRequirement.BluetoothAdvertiseSAndAbove)
                get { grantedPermissions }.isEmpty()
            }
        }

        @Test
        @DisplayName("SCAN and ADVERTISE: WHEN some permissions are missing MUST combine and list all missing permissions")
        fun `combined scan and advertise with some missing permissions`() = runTest {
            val operations = setOf(BleOperation.SCAN_DEVICES, BleOperation.ADVERTISE_AS_SERVER)

            coEvery { mockPermissionRepository.isPermissionGranted(Manifest.permission.BLUETOOTH_SCAN) } returns true
            coEvery { mockPermissionRepository.isPermissionGranted(Manifest.permission.BLUETOOTH_CONNECT) } returns false
            coEvery { mockPermissionRepository.isPermissionGranted(Manifest.permission.BLUETOOTH_ADVERTISE) } returns false

            
            val permissionsReport = getPermissionsReportUseCase(operations)

            expectThat(permissionsReport) {
                get { requiredPermissionsGranted }.isFalse()
                get { missingPermissions }.containsExactlyInAnyOrder(
                    BlePermissionRequirement.BluetoothAdvertiseSAndAbove
                )
                get { grantedPermissions }.containsExactlyInAnyOrder(
                    BlePermissionRequirement.BluetoothScanSAndAbove
                )
            }
        }

        @Test
        @DisplayName("WHEN no operations are requested MUST return an empty and successful report")
        fun `no operations requested`() = runTest {
          
            val operations = emptySet<BleOperation>()

            
            val permissionsReport = getPermissionsReportUseCase(operations)

            expectThat(permissionsReport) {
                get { requiredPermissionsGranted }.isTrue()
                get { missingPermissions }.isEmpty()
                get { grantedPermissions }.isEmpty()
            }
        }
    }

    @Nested
    @DisplayName("Tests for Android versions lower than 12 (< SDK 31)")
    inner class LegacyAndroidTests {
        private val sdkVersion = 30

        @BeforeEach
        fun setup() {
            every { mockBuildVersionRepository.getCurrentSdkLevel() } returns sdkVersion
        }

        @Test
        @DisplayName("SCAN: WHEN all permissions are granted MUST return a report without missing permissions")
        fun `scan operation with all permissions granted`() = runTest {
            val operations = setOf(BleOperation.SCAN_DEVICES)

            coEvery { mockPermissionRepository.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION) } returns true
            coEvery { mockPermissionRepository.isPermissionGranted(Manifest.permission.BLUETOOTH_ADMIN) } returns true

            val permissionsReport = getPermissionsReportUseCase(operations)

            expectThat(permissionsReport) {
                get { requiredPermissionsGranted }.isTrue()
                get { missingPermissions }.isEmpty()
                get { grantedPermissions }.containsExactlyInAnyOrder(
                    BlePermissionRequirement.AccessFineLocation,
                    BlePermissionRequirement.BluetoothAdminLegacy
                )
            }
        }

        @Test
        @DisplayName("SCAN: WHEN one permission is missing MUST return a report with missing permissions")
        fun `scan operation with one missing permission`() = runTest {
            val operations = setOf(BleOperation.SCAN_DEVICES)
            coEvery { mockPermissionRepository.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION) } returns false
            coEvery { mockPermissionRepository.isPermissionGranted(Manifest.permission.BLUETOOTH_ADMIN) } returns true

            val permissionsReport = getPermissionsReportUseCase(operations)

            expectThat(permissionsReport) {
                get { requiredPermissionsGranted }.isFalse()
                get { missingPermissions }.containsExactlyInAnyOrder(BlePermissionRequirement.AccessFineLocation)
                get { grantedPermissions }.containsExactlyInAnyOrder(BlePermissionRequirement.BluetoothAdminLegacy)
            }
        }

        @Test
        @DisplayName("CONNECT: MUST NOT require any additional permissions")
        fun `connect operation requires no additional dangerous permissions`() = runTest {
            val operations = setOf(BleOperation.CONNECT_AND_COMMUNICATE)

            val permissionsReport = getPermissionsReportUseCase(operations)

            expectThat(permissionsReport) {
                get { requiredPermissionsGranted }.isTrue()
                get { missingPermissions }.isEmpty()
                get { grantedPermissions }.isEmpty()
            }
        }

        @Test
        @DisplayName("ADVERTISE:  WHEN all permissions are granted MUST return a report without missing permissions")
        fun `advertise operation with permission granted`() = runTest {
            val operations = setOf(BleOperation.ADVERTISE_AS_SERVER)

            coEvery { mockPermissionRepository.isPermissionGranted(Manifest.permission.BLUETOOTH_ADMIN) } returns true

            val permissionsReport = getPermissionsReportUseCase(operations)

            expectThat(permissionsReport) {
                get { requiredPermissionsGranted }.isTrue()
                get { missingPermissions }.isEmpty()
                get { grantedPermissions }.containsExactlyInAnyOrder(BlePermissionRequirement.BluetoothAdminLegacy)
            }
        }

        @Test
        @DisplayName("ADVERTISE: WHEN any permissions are missing MUST return a report with missing permissions")
        fun `advertise operation with permission missing`() = runTest {
            val operations = setOf(BleOperation.ADVERTISE_AS_SERVER)
            coEvery { mockPermissionRepository.isPermissionGranted(Manifest.permission.BLUETOOTH_ADMIN) } returns false

            val permissionsReport = getPermissionsReportUseCase(operations)

            expectThat(permissionsReport) {
                get { requiredPermissionsGranted }.isFalse()
                get { missingPermissions }.containsExactlyInAnyOrder(BlePermissionRequirement.BluetoothAdminLegacy)
                get { grantedPermissions }.isEmpty()
            }
        }

        @Test
        @DisplayName("SCAN and ADVERTISE: SHOULD combine and deduplicate requirements")
        fun `combined scan and advertise should combine and deduplicate requirements`() = runTest {
            val operations = setOf(BleOperation.SCAN_DEVICES, BleOperation.ADVERTISE_AS_SERVER)

            coEvery { mockPermissionRepository.isPermissionGranted(any()) } returns false
            val permissionsReport = getPermissionsReportUseCase(operations)

            expectThat(permissionsReport) {
                get { requiredPermissionsGranted }.isFalse()
                get { grantedPermissions }.isEmpty()
                get { missingPermissions }.containsExactlyInAnyOrder(
                    BlePermissionRequirement.AccessFineLocation,
                    BlePermissionRequirement.BluetoothAdminLegacy
                )
            }
        }
    }
}