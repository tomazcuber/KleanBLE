package com.tomazcuber.kleanble.permissionshelper.domain.model

data class BlePermissionsReport(
    val requiredPermissionsGranted: Boolean,
    val missingPermissions: List<BlePermissionRequirement>,
    val grantedPermissions: List<BlePermissionRequirement>
)
