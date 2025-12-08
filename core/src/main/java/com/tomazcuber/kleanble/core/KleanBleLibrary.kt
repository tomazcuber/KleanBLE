package com.tomazcuber.kleanble.core

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.dsl.koinApplication
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Internal singleton to manage the isolated Koin instance for the KleanBLE library.
 * This ensures we don't interfere with the host application's DI (Koin or otherwise).
 */
object KleanBleLibrary {
    @Suppress("ktlint:standard:backing-property-naming")
    private var _koinApp: KoinApplication? = null
    private val isInitialized = AtomicBoolean(false)

    // The internal Koin instance.
    // We expose 'koin' so factories can do: KleanBleLibrary.koin.get<MyService>()
    val koin
        get() =
            _koinApp?.koin
                ?: throw IllegalStateException(
                    "KleanBLE library is not initialized. Please ensure the Factory calls initialize() internally.",
                )

    /**
     * Initializes the library's internal DI graph.
     * This is safe to call multiple times; it will only initialize once.
     *
     * @param context Application context
     */
    fun initialize(context: Context) {
        if (isInitialized.compareAndSet(false, true)) {
            _koinApp =
                koinApplication {
                    androidContext(context.applicationContext)
                    // We start with no modules. Modules will 'load' themselves lazily
                    // or we could pass them here if we wanted a central registry.
                    // For better modularity, we'll let factories load their modules.
                }
        }
    }

    /**
     * Helper to load modules safely into the existing isolated context.
     */
    fun loadModules(modules: List<Module>) {
        if (_koinApp == null) {
            throw IllegalStateException("Cannot load modules before initialization.")
        }
        _koinApp?.modules(modules)
    }
}
