package com.tomazcuber.kleanble.permissionshelper.domain.repository

interface PermissionRepository {
    suspend fun isPermissionGranted(permissionName: String): Boolean
}