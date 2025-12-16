package com.tomazcuber.kleanble

import android.content.Context
import com.tomazcuber.kleanble.permissionshelper.api.BlePermissionsChecker
import com.tomazcuber.kleanble.permissionshelper.api.BlePermissionsFactory
import com.tomazcuber.kleanble.scan.api.BleScanner
import com.tomazcuber.kleanble.scan.api.BleScannerFactory

/**
 * The unified entry point for the KleanBLE library.
 *
 * This singleton provides access to all library features (Scanning, Permissions, etc.)
 * and manages the common initialization process.
 */
object KleanBle {

    private var _scanner: BleScanner? = null
    private var _permissionsChecker: BlePermissionsChecker? = null

    /**
     * Access the BLE Scanner feature.
     * @throws IllegalStateException if [initialize] has not been called.
     */
    val scanner: BleScanner
        get() = _scanner ?: throw IllegalStateException("KleanBle is not initialized. Call KleanBle.initialize(context) first.")

    /**
     * Access the BLE Permissions feature.
     * @throws IllegalStateException if [initialize] has not been called.
     */
    val permissionsChecker: BlePermissionsChecker
        get() = _permissionsChecker ?: throw IllegalStateException("KleanBle is not initialized. Call KleanBle.initialize(context) first.")

    /**
     * Initializes the KleanBLE library and all its sub-modules.
     *
     * @param context The application context.
     */
    fun initialize(context: Context) {
        // Factories handle their own module loading and idempotent initialization
        // via Core.KleanBleLibrary.
        _scanner = BleScannerFactory.create(context)
        _permissionsChecker = BlePermissionsFactory.create(context)
    }
}
