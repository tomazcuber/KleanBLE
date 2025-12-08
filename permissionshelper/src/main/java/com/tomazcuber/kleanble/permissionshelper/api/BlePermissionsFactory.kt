package com.tomazcuber.kleanble.permissionshelper.api

import android.content.Context
import com.tomazcuber.kleanble.core.KleanBleLibrary
import com.tomazcuber.kleanble.core.api.KleanBleComponentFactory
import com.tomazcuber.kleanble.permissionshelper.di.permissionsHelperModule

object BlePermissionsFactory : KleanBleComponentFactory<BlePermissionsChecker> {
    /**
     * Creates an instance of [BlePermissionsChecker].
     *
     * @param context The application context.
     * @return A ready-to-use [BlePermissionsChecker].
     */
    override fun create(context: Context): BlePermissionsChecker {
        KleanBleLibrary.initialize(context)
        loadModule()
        return KleanBleLibrary.koin.get()
    }

    private var isModuleLoaded = false

    private fun loadModule() {
        if (!isModuleLoaded) {
            KleanBleLibrary.loadModules(listOf(permissionsHelperModule))
            isModuleLoaded = true
        }
    }
}
