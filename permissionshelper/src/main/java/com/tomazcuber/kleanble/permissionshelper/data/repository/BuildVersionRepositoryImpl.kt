package com.tomazcuber.kleanble.permissionshelper.data.repository

import android.os.Build
import com.tomazcuber.kleanble.permissionshelper.domain.repository.BuildVersionRepository

class BuildVersionRepositoryImpl : BuildVersionRepository {
    override fun getCurrentSdkLevel(): Int {
        return Build.VERSION.SDK_INT
    }
}