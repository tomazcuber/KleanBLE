import com.tomazcuber.kleanble.scan.domain.FakeScanDataSource
import com.tomazcuber.kleanble.scan.domain.model.BleScanFilter
import com.tomazcuber.kleanble.scan.domain.model.BleScanSettings
import com.tomazcuber.kleanble.scan.domain.model.ScanCallbackType
import com.tomazcuber.kleanble.scan.domain.model.ScanMode
import com.tomazcuber.kleanble.scan.domain.usecase.StartScanUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.first
import strikt.assertions.hasSize
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.isTrue

class StartScanUseCaseTest {

    private lateinit var fakeScanDataSource: FakeScanDataSource
    private lateinit var startScanUseCase: StartScanUseCase

    @BeforeEach
    fun setUp() {
        fakeScanDataSource = FakeScanDataSource()
        startScanUseCase = StartScanUseCase(fakeScanDataSource)
    }

    @Test
    fun `invoke should call startScan on the repository`() {
        // Arrange
        val settings = BleScanSettings(
            scanMode = ScanMode.LOW_LATENCY,
            callbackType = ScanCallbackType.ALL_MATCHES
        )
        val filters = emptyList<BleScanFilter>()

        // Act
        startScanUseCase(settings, filters)

        // Assert
        expectThat(fakeScanDataSource.startScanCalled).isTrue()
    }

    @Test
    fun `invoke with filter passes filter to repository`() {
        // Arrange
        val settings = BleScanSettings(
            scanMode = ScanMode.LOW_LATENCY,
            callbackType = ScanCallbackType.ALL_MATCHES
        )
        val testFilter = BleScanFilter(deviceName = "TestDevice")
        val filters = listOf(testFilter)

        // Act
        startScanUseCase(settings, filters)

        // Assert
        expectThat(fakeScanDataSource.receivedFilters)
            .isNotNull()
            .hasSize(1)
            .first()
            .isEqualTo(testFilter)
    }
}
