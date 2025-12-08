package com.tomazcuber.kleanble.permissionshelper.api

import com.tomazcuber.kleanble.common.domain.model.BleOperation
import com.tomazcuber.kleanble.permissionshelper.domain.model.BlePermissionsReport

interface BlePermissionsChecker {
    suspend fun getPermissionsReportFor(
        operations: Set<BleOperation>,
        appDerivesLocationFromScan: Boolean = false,
    ): BlePermissionsReport
}
