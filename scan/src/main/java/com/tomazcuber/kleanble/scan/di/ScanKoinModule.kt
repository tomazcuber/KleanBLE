package com.tomazcuber.kleanble.scan.di

import com.tomazcuber.kleanble.scan.api.BleScanner
import com.tomazcuber.kleanble.scan.data.cache.InMemoryDeviceCache
import com.tomazcuber.kleanble.scan.data.repository.LiveScanDataSourceImpl
import com.tomazcuber.kleanble.scan.data.repository.ScanRepositoryImpl
import com.tomazcuber.kleanble.scan.data.util.SystemClock
import com.tomazcuber.kleanble.scan.domain.repository.LiveScanDataSource
import com.tomazcuber.kleanble.scan.domain.repository.ScanCacheDataSource
import com.tomazcuber.kleanble.scan.domain.repository.ScanRepository
import com.tomazcuber.kleanble.scan.domain.usecase.ObserveScanResultsUseCase
import com.tomazcuber.kleanble.scan.domain.usecase.ObserveScanStateUseCase
import com.tomazcuber.kleanble.scan.domain.usecase.StartScanUseCase
import com.tomazcuber.kleanble.scan.domain.usecase.StopScanUseCase
import com.tomazcuber.kleanble.scan.domain.util.Clock
import com.tomazcuber.kleanble.scan.internal.BleScannerImpl
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val scanModule = module {

    // Top-level Repository (with caching)
    single<ScanRepository> {
        ScanRepositoryImpl(
            liveDataSource = get(),
            cacheDataSource = get(),
            dispatcher = Dispatchers.IO
        )
    }

    // Live Data Source (hardware)
    single<LiveScanDataSource> {
        LiveScanDataSourceImpl(
            context = androidContext(),
            dispatcher = Dispatchers.IO
        )
    }

    // Cache Data Source (in-memory)
    single<ScanCacheDataSource> { InMemoryDeviceCache(clock = get()) }

    // Clock utility
    single<Clock> { SystemClock() }

    // Use Cases
    factory { ObserveScanStateUseCase(get()) }
    factory { ObserveScanResultsUseCase(get()) }
    factory { StartScanUseCase(get()) }
    factory { StopScanUseCase(get()) }

    // Public API
    single<BleScanner> {
        BleScannerImpl(
            observeScanStateUseCase = get(),
            observeScanResultsUseCase = get(),
            startScanUseCase = get(),
            stopScanUseCase = get()
        )
    }
}
