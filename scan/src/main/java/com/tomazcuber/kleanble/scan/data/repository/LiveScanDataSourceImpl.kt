package com.tomazcuber.kleanble.scan.data.repository

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.content.Context
import android.bluetooth.le.ScanResult
import com.tomazcuber.kleanble.scan.data.mapper.toAndroid
import com.tomazcuber.kleanble.scan.data.mapper.toBleScanResult
import com.tomazcuber.kleanble.scan.domain.model.BleScanError
import com.tomazcuber.kleanble.scan.domain.model.BleScanFilter
import com.tomazcuber.kleanble.scan.domain.model.BleScanResult
import com.tomazcuber.kleanble.scan.domain.model.BleScanSettings
import com.tomazcuber.kleanble.scan.domain.model.BleScanState
import com.tomazcuber.kleanble.scan.domain.repository.LiveScanDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
internal class LiveScanDataSourceImpl(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher
) : LiveScanDataSource {

    private val bluetoothManager: BluetoothManager by lazy { context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager }
    private val bluetoothAdapter: BluetoothAdapter? by lazy { bluetoothManager.adapter }
    private val bluetoothLeScanner: BluetoothLeScanner? by lazy { bluetoothAdapter?.bluetoothLeScanner }

    private val _scanState = MutableStateFlow<BleScanState>(BleScanState.Idle)
    override val scanState = _scanState.asStateFlow()

    private val _scanResults = MutableSharedFlow<BleScanResult>()
    override val scanResults = _scanResults.asSharedFlow()

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            CoroutineScope(dispatcher).launch {
                _scanResults.emit(result.toBleScanResult())
            }
        }

        override fun onScanFailed(errorCode: Int) {
            val reason = when (errorCode) {
                ScanCallback.SCAN_FAILED_ALREADY_STARTED -> BleScanError.SCAN_FAILED_ALREADY_STARTED
                ScanCallback.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED -> BleScanError.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED
                ScanCallback.SCAN_FAILED_FEATURE_UNSUPPORTED -> BleScanError.SCAN_FAILED_FEATURE_UNSUPPORTED
                ScanCallback.SCAN_FAILED_INTERNAL_ERROR -> BleScanError.SCAN_FAILED_INTERNAL_ERROR
                else -> BleScanError.UNKNOWN
            }
            _scanState.value = BleScanState.Error(reason)
        }
    }

    override fun startScan(
        settings: BleScanSettings,
        filters: List<BleScanFilter>
    ) {
        CoroutineScope(dispatcher).launch {
            if (bluetoothLeScanner == null) {
                _scanState.value = BleScanState.Error(BleScanError.BLUETOOTH_UNAVAILABLE)
                return@launch
            }

            if (bluetoothAdapter?.isEnabled == false) {
                _scanState.value = BleScanState.Error(BleScanError.BLUETOOTH_DISABLED)
                return@launch
            }

            val androidSettings = settings.toAndroid()
            val androidFilters = filters.map { it.toAndroid() }

            try {
                bluetoothLeScanner?.startScan(androidFilters, androidSettings, scanCallback)
                _scanState.value = BleScanState.Scanning
            } catch (e: SecurityException) {
                _scanState.value = BleScanState.Error(BleScanError.MISSING_PERMISSIONS)
            }
        }
    }

    override fun stopScan() {
        bluetoothLeScanner?.stopScan(scanCallback)
        _scanState.value = BleScanState.Idle
    }
}
