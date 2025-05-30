package com.tomazcuber.kleanble.permissionshelper.data.repository

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.tomazcuber.kleanble.permissionshelper.domain.repository.PermissionRepository

class PermissionRepositoryImpl(private val context: Context) : PermissionRepository {
    override suspend fun isPermissionGranted(permissionName: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permissionName) == PackageManager.PERMISSION_GRANTED
    }
}