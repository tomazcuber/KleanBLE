package com.tomazcuber.kleanble

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.tomazcuber.kleanble.permissionshelper.api.BlePermissionsFactory
import com.tomazcuber.kleanble.scan.api.BleScannerFactory
import org.junit.Test
import org.junit.runner.RunWith
import strikt.api.expectCatching
import strikt.assertions.isSuccess

@RunWith(AndroidJUnit4::class)
class LibraryIntegrationTest {

    @Test
    fun testScannerInitialization() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        
        expectCatching {
            BleScannerFactory.create(appContext)
        }.isSuccess()
    }

    @Test
    fun testPermissionsInitialization() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        
        expectCatching {
            BlePermissionsFactory.create(appContext)
        }.isSuccess()
    }

    @Test
    fun testBothModulesInitialization() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        
        expectCatching {
            BleScannerFactory.create(appContext)
            BlePermissionsFactory.create(appContext)
        }.isSuccess()
    }
}