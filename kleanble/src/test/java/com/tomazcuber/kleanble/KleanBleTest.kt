package com.tomazcuber.kleanble

import android.content.Context
import com.tomazcuber.kleanble.permissionshelper.api.BlePermissionsChecker
import com.tomazcuber.kleanble.permissionshelper.api.BlePermissionsFactory
import com.tomazcuber.kleanble.scan.api.BleScanner
import com.tomazcuber.kleanble.scan.api.BleScannerFactory
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.reflect.Field

class KleanBleTest {
    @BeforeEach
    fun setUp() {
        mockkObject(BleScannerFactory)
        mockkObject(BlePermissionsFactory)
        resetSingleton()
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
        resetSingleton()
    }

    private fun resetSingleton() {
        // Reset private fields via reflection to ensure test isolation
        try {
            val scannerField: Field = KleanBle::class.java.getDeclaredField("_scanner")
            scannerField.isAccessible = true
            scannerField.set(KleanBle, null)

            val permsField: Field = KleanBle::class.java.getDeclaredField("_permissionsChecker")
            permsField.isAccessible = true
            permsField.set(KleanBle, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Test
    fun `accessing scanner before initialization throws IllegalStateException`() {
        assertThrows(IllegalStateException::class.java) {
            KleanBle.scanner
        }
    }

    @Test
    fun `accessing permissionsChecker before initialization throws IllegalStateException`() {
        assertThrows(IllegalStateException::class.java) {
            KleanBle.permissionsChecker
        }
    }

    @Test
    fun `initialize sets up components correctly`() {
        val mockContext: Context = mockk(relaxed = true)
        val mockScanner = mockk<BleScanner>()
        val mockPermissions = mockk<BlePermissionsChecker>()

        every { BleScannerFactory.create(mockContext) } returns mockScanner
        every { BlePermissionsFactory.create(mockContext) } returns mockPermissions

        KleanBle.initialize(mockContext)

        assertEquals(mockScanner, KleanBle.scanner)
        assertEquals(mockPermissions, KleanBle.permissionsChecker)

        verify(exactly = 1) { BleScannerFactory.create(mockContext) }
        verify(exactly = 1) { BlePermissionsFactory.create(mockContext) }
    }
}
