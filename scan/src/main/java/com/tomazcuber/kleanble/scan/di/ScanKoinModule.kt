package com.tomazcuber.kleanble.scan.di

import com.tomazcuber.kleanble.scan.api.BleScanner
import com.tomazcuber.kleanble.scan.data.ScanRepositoryImpl
import com.tomazcuber.kleanble.scan.domain.repository.ScanRepository
import com.tomazcuber.kleanble.scan.domain.usecase.ObserveScanResultsUseCase
import com.tomazcuber.kleanble.scan.domain.usecase.ObserveScanStateUseCase
import com.tomazcuber.kleanble.scan.domain.usecase.StartScanUseCase
import com.tomazcuber.kleanble.scan.domain.usecase.StopScanUseCase
import com.tomazcuber.kleanble.scan.internal.BleScannerImpl
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val scanModule = module {

    // Repository
    single<ScanRepository> {
        ScanRepositoryImpl(
            context = androidContext(),
            dispatcher = Dispatchers.IO
        )
    }

    // Use Cases
    factory { ObserveScanStateUseCase(get()) }
    factory { ObserveScanResultsUseCase(get()) }
    factory { StartScanUseCase(get()) }
    factory { StopScanUseCase(get()) }

    // The public API façade (BleScanner)
    single<BleScanner> {
        BleScannerImpl(
            observeScanStateUseCase = get(),
            observeScanResultsUseCase = get(),
            startScanUseCase = get(),
            stopScanUseCase = get()
        )
    }
}
