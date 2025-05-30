package com.tomazcuber.kleanble.permissionshelper.data.repository

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isTrue

class PermissionRepositoryImplTest{

     private lateinit var mockContext: Context
     private lateinit var permissionRepositoryImpl: PermissionRepositoryImpl
     private lateinit var buildVersionRepositoryImpl: BuildVersionRepositoryImpl

     @BeforeEach
     fun setUp(){
         mockContext = mockk(relaxed = true)
         permissionRepositoryImpl = PermissionRepositoryImpl(mockContext)
         buildVersionRepositoryImpl = mockk()
     }

    @Nested
    @DisplayName("Tests for getCurrentSdkLevel() method")
    inner class GetCurrentSdkLevelTests {

        @Test
        fun `SHOULD return correct value of Build's VERSION_SDK_INT`() {
            val expectedSdkLevel = 30

            every { buildVersionRepositoryImpl.getCurrentSdkLevel() } returns expectedSdkLevel

            val actualSdkLevel = buildVersionRepositoryImpl.getCurrentSdkLevel()

            expectThat(actualSdkLevel).isEqualTo(expectedSdkLevel)
        }
    }

    @Nested
    @DisplayName("Tests for isPermissionGranted() method")
    inner class IsPermissionGrantedTests {

        private val TEST_PERMISSION_NAME = "android.permission.TEST_PERMISSION"

        @BeforeEach
        fun setupStaticMocks() {
            mockkStatic(ContextCompat::class)
        }

        @AfterEach
        fun tearDownStaticMocks() {
            unmockkStatic(ContextCompat::class)
        }

        @Test
        fun `SHOULD return true WHEN ContextCompat checkSelfPermission returns PERMISSION_GRANTED`() = runTest {
            every { ContextCompat.checkSelfPermission(mockContext, TEST_PERMISSION_NAME) } returns PackageManager.PERMISSION_GRANTED

            val isGranted = permissionRepositoryImpl.isPermissionGranted(TEST_PERMISSION_NAME)

            expectThat(isGranted).isTrue()
        }

        @Test
        fun `SHOULD return false WHEN ContextCompat checkSelfPermission returns PERMISSION_DENIED`() = runTest {
            every { ContextCompat.checkSelfPermission(mockContext, TEST_PERMISSION_NAME) } returns PackageManager.PERMISSION_DENIED

            val isGranted = permissionRepositoryImpl.isPermissionGranted(TEST_PERMISSION_NAME)

            expectThat(isGranted).isFalse()
        }

        @Test
        fun `SHOULD verify the correct permission when passed as argument`() = runTest {
            val specificPermission = "android.permission.SPECIFIC_TEST_PERMISSION"
            every { ContextCompat.checkSelfPermission(mockContext, specificPermission) } returns PackageManager.PERMISSION_GRANTED

            val isGranted = permissionRepositoryImpl.isPermissionGranted(specificPermission)

            expectThat(isGranted).isTrue()
        }
    }
 }