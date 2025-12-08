package com.tomazcuber.kleanble.scan.api

import android.content.Context
import com.tomazcuber.kleanble.core.KleanBleLibrary
import com.tomazcuber.kleanble.core.api.KleanBleComponentFactory
import com.tomazcuber.kleanble.scan.di.scanModule

object BleScannerFactory : KleanBleComponentFactory<BleScanner> {
    /**
     * Creates an instance of [BleScanner].
     *
     * @param context The application context.
     * @return A ready-to-use [BleScanner] implementation.
     */
    override fun create(context: Context): BleScanner {
        // 1. Ensure the core library context is initialized
        KleanBleLibrary.initialize(context)

        // 2. Load the Scan module into the core context (idempotent-ish check)
        loadModule()

        // 3. Retrieve the instance
        return KleanBleLibrary.koin.get()
    }

    private var isModuleLoaded = false

    private fun loadModule() {
        if (!isModuleLoaded) {
            KleanBleLibrary.loadModules(listOf(scanModule))
            isModuleLoaded = true
        }
    }
}
