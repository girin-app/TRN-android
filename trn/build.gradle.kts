plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    `maven-publish`
}

android {
    namespace = "app.girin.trn_android"
    compileSdk = 34

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Define a BOM and its version
    api(platform(libs.ethers.bom))

    // Define any required artifacts without version
    api(libs.ethers.abi)
    api(libs.ethers.core)
    api(libs.ethers.providers)
    api(libs.ethers.signers)
    implementation(libs.bcprov.jdk18on)
    testImplementation(libs.junit)
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.github.girin-app"
                artifactId = "TRN-android"
                version = "v0.2.3"

                pom {
                    name.set("TRN-android")
                    description.set("The Root Network Android API")
                }
            }
        }
    }
}