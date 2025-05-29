package com.tomazcuber.kleanble.permissionshelper.domain.usecase

import com.tomazcuber.kleanble.common.domain.model.BleOperation
import com.tomazcuber.kleanble.permissionshelper.domain.model.BlePermissionRequirement
import com.tomazcuber.kleanble.permissionshelper.domain.model.BlePermissionsReport
import com.tomazcuber.kleanble.permissionshelper.domain.repository.PermissionRepository

class GetPermissionsReportForBleOperationUseCase(private val permissionRepository: PermissionRepository) {
    private val allPossibleRequirements = listOf(
        BlePermissionRequirement.BluetoothScanSAndAbove,
        BlePermissionRequirement.BluetoothConnectSAndAbove,
        BlePermissionRequirement.BluetoothAdvertiseSAndAbove,
        BlePermissionRequirement.AccessFineLocation,
        BlePermissionRequirement.BluetoothAdminLegacy
    )

    private fun mapOperationsToRequirements(operations: Set<BleOperation>): Set<BlePermissionRequirement> {
        val requirements = mutableSetOf<BlePermissionRequirement>()
        operations.forEach { operation ->
            when (operation) {
                BleOperation.SCAN_DEVICES -> {
                    requirements.add(BlePermissionRequirement.BluetoothScanSAndAbove)
                    requirements.add(BlePermissionRequirement.AccessFineLocation)
                    requirements.add(BlePermissionRequirement.BluetoothAdminLegacy)
                }
                BleOperation.CONNECT_AND_COMMUNICATE -> {
                    requirements.add(BlePermissionRequirement.BluetoothConnectSAndAbove)
                }
                BleOperation.ADVERTISE_AS_SERVER -> {
                    requirements.add(BlePermissionRequirement.BluetoothAdvertiseSAndAbove)
                    requirements.add(BlePermissionRequirement.BluetoothAdminLegacy)
                }
            }
        }
        return requirements
    }

    suspend operator fun invoke(
        operations: Set<BleOperation>,
        appDerivesLocationFromScan: Boolean = false
    ): BlePermissionsReport {
        val necessaryRequirements = mapOperationsToRequirements(operations)

        val checkContext = BlePermissionRequirement.CheckContext(
            sdkVersion = permissionRepository.getCurrentSdkLevel(),
            appDerivesLocationFromScan = appDerivesLocationFromScan
        )
        val applicableRequirements = necessaryRequirements.filter { it.isRequired(checkContext) }

        val grantedPermissions = mutableListOf<BlePermissionRequirement>()
        val missingPermissions = mutableListOf<BlePermissionRequirement>()

        applicableRequirements.forEach { requirement ->
            if (permissionRepository.isPermissionGranted(requirement.permissionName)) {
                grantedPermissions.add(requirement)
            } else {
                missingPermissions.add(requirement)
            }
        }

        return BlePermissionsReport(
            requiredPermissionsGranted = missingPermissions.isEmpty(),
            missingPermissions = missingPermissions,
            grantedPermissions = grantedPermissions
        )
    }
}