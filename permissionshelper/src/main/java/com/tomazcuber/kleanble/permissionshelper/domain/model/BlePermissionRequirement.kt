package com.tomazcuber.kleanble.permissionshelper.domain.model

import android.Manifest
import android.os.Build
import com.tomazcuber.kleanble.common.R

/**
 * Representa um requisito de permissão BLE específico, modelado como uma sealed class.
 * Cada subclasse define uma permissão particular do Android e as condições sob as quais ela é necessária.
 *
 * @property permissionName O nome da permissão Android (ex: android.Manifest.permission.BLUETOOTH_SCAN).
 * @property rationaleTitleResId O ID do recurso de string para o título da justificativa (ex: R.string.title_bluetooth_scan).
 * @property rationaleMessageResId O ID do recurso de string para a mensagem da justificativa (ex: R.string.message_bluetooth_scan).
 */
sealed class BlePermissionRequirement(
    val permissionName: String,
    val rationaleTitleResId: Int,
    val rationaleMessageResId: Int,
) {

    abstract fun isRequired(checkContext: CheckContext): Boolean

    data class CheckContext(
        val sdkVersion: Int,
        val appDerivesLocationFromScan: Boolean = false
    )

    // --- Permission Requirements for Android 12 (SDK 31) or greater ---

    data object BluetoothScanSAndAbove : BlePermissionRequirement(
        permissionName = Manifest.permission.BLUETOOTH_SCAN,
        rationaleTitleResId = R.string.kleanble_perm_scan_s_title,
        rationaleMessageResId = R.string.kleanble_perm_scan_s_message
    ) {
        override fun isRequired(checkContext: CheckContext): Boolean =
            checkContext.sdkVersion >= Build.VERSION_CODES.S
    }

    data object BluetoothConnectSAndAbove : BlePermissionRequirement(
        permissionName = Manifest.permission.BLUETOOTH_CONNECT,
        rationaleTitleResId = R.string.kleanble_perm_connect_s_title ,
        rationaleMessageResId = R.string.kleanble_perm_connect_s_message
    ) {
        override fun isRequired(checkContext: CheckContext): Boolean =
            checkContext.sdkVersion >= Build.VERSION_CODES.S
    }

    data object BluetoothAdvertiseSAndAbove : BlePermissionRequirement(
        permissionName = Manifest.permission.BLUETOOTH_ADVERTISE,
        rationaleTitleResId = R.string.kleanble_perm_advertise_s_title,
        rationaleMessageResId =  R.string.kleanble_perm_advertise_s_message
    ) {
        override fun isRequired(checkContext: CheckContext): Boolean =
            checkContext.sdkVersion >= Build.VERSION_CODES.S
    }

    // --- Permission Requirements for versions lower than Android 12 (SDK 31) ---

    data object BluetoothAdminLegacy : BlePermissionRequirement(
        permissionName = Manifest.permission.BLUETOOTH_ADMIN,
        rationaleTitleResId =  R.string.kleanble_perm_admin_legacy_title,
        rationaleMessageResId =  R.string.kleanble_perm_admin_legacy_message
    ) {
        override fun isRequired(checkContext: CheckContext): Boolean =
            checkContext.sdkVersion < Build.VERSION_CODES.S
    }

    data object AccessFineLocation : BlePermissionRequirement(
        permissionName = Manifest.permission.ACCESS_FINE_LOCATION,
        rationaleTitleResId = R.string.kleanble_perm_fine_location_title,
        rationaleMessageResId = R.string.kleanble_perm_fine_location_message
    ) {
        override fun isRequired(checkContext: CheckContext): Boolean {
            return checkContext.sdkVersion < Build.VERSION_CODES.S || (checkContext.sdkVersion >= Build.VERSION_CODES.S && checkContext.appDerivesLocationFromScan)
        }
    }
}