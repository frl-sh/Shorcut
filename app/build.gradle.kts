plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdk = AppConfig.compileSdkVersion

    defaultConfig {
        applicationId = "sharif.feryal.shortcut.task"
        minSdk = AppConfig.minSdk
        targetSdk = AppConfig.targetSdk
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName

        testInstrumentationRunner = AppConfig.androidTestInstrumentation
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            isDebuggable = true
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    with(Dependencies) {
        implementation(
            listOf(
                androidxCore,
                appCompat,
                material,
                constraintLayout,
                navigationFragment,
                navigationUi,
                glide,
                okhttp,
                loggingInterceptor,
                retrofit,
                gsonConverter,
                gson,
                coroutinesCore,
                coroutinesAndroid,
                coroutinesAdapter,
                koin
            )
        )

        annotationProcessor(glideAnnotation)

        testImplementation(
            listOf(
                junit,
                koinTest,
                mockk,
                coroutinesTest,
                archTest
            )
        )
    }
}