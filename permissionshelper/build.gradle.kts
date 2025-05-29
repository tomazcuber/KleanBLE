plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.junit5)
}

android {
    namespace = "com.tomazcuber.kleanble.permissionshelper"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)

    testImplementation(libs.bundles.junit5)
    testRuntimeOnly(libs.junit.jupiter.engine)
    androidTestImplementation(libs.androidx.espresso.core)

    testImplementation(libs.mockk)
    testImplementation(libs.strikt)
    testImplementation(libs.kotlinx.coroutines.test)

    implementation(project(":commonutils"))
}