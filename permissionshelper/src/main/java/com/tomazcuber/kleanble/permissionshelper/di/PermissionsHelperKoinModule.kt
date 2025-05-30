package com.tomazcuber.kleanble.permissionshelper.di

import com.tomazcuber.kleanble.permissionshelper.api.BlePermissionsChecker
import com.tomazcuber.kleanble.permissionshelper.data.repository.PermissionRepositoryImpl
import com.tomazcuber.kleanble.permissionshelper.domain.repository.PermissionRepository
import com.tomazcuber.kleanble.permissionshelper.domain.usecase.GetPermissionsReportForBleOperationUseCase
import com.tomazcuber.kleanble.permissionshelper.internal.BlePermissionsCheckerImpl
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext

val permissionsHelperModule = module {
    single<BlePermissionsChecker> {
        BlePermissionsCheckerImpl(getPermissionsReportForBleOperationUseCase = get())
    }

    factory {
        GetPermissionsReportForBleOperationUseCase(permissionRepository = get(), buildVersionRepository = get())
    }

    single<PermissionRepository> {
        PermissionRepositoryImpl(context = androidContext())
    }
}