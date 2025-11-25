package com.tomazcuber.kleanble.permissionshelper.internal

import com.tomazcuber.kleanble.common.domain.model.BleOperation
import com.tomazcuber.kleanble.permissionshelper.api.BlePermissionsChecker
import com.tomazcuber.kleanble.permissionshelper.domain.model.BlePermissionsReport
import com.tomazcuber.kleanble.permissionshelper.domain.usecase.GetPermissionsReportForBleOperationUseCase

internal class BlePermissionsCheckerImpl(
    private val getPermissionsReportForBleOperationUseCase: GetPermissionsReportForBleOperationUseCase,
) : BlePermissionsChecker {
    override suspend fun getPermissionsReportFor(
        operations: Set<BleOperation>,
        appDerivesLocationFromScan: Boolean,
    ): BlePermissionsReport =
        getPermissionsReportForBleOperationUseCase(
            operations = operations,
            appDerivesLocationFromScan = appDerivesLocationFromScan,
        )
}
