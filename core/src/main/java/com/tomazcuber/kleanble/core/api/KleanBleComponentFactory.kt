package com.tomazcuber.kleanble.core.api

import android.content.Context

/**
 * Standard interface for all KleanBLE feature module factories.
 * Ensures consistent initialization behavior across the library.
 *
 * @param T The public API interface type of the feature (e.g., BleScanner).
 */
interface KleanBleComponentFactory<T> {
    
    /**
     * Creates and initializes the feature component.
     *
     * @param context The application context.
     * @return An instance of the feature component.
     */
    fun create(context: Context): T
}
