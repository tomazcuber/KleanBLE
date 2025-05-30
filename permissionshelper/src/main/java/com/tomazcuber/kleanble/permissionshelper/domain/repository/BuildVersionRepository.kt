package com.tomazcuber.kleanble.permissionshelper.domain.repository

interface BuildVersionRepository {
    fun getCurrentSdkLevel(): Int
}