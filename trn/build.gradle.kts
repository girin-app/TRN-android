plugins {
//    id("com.android.library")
//    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.jvm")
    `maven-publish`
}

//android {
//    namespace = "app.girin.trn_android"
//    compileSdk = 35
//
//    defaultConfig {
//        minSdk = 24
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles("consumer-rules.pro")
//    }
//
//    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_1_8
//        targetCompatibility = JavaVersion.VERSION_1_8
//    }
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
//}
//java {
//    sourceCompatibility = JavaVersion.VERSION_1_8
//    targetCompatibility = JavaVersion.VERSION_1_8
//}
//kotlin {
//    compilerOptions {
//        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8
//    }
//}
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

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "com.github.girin-app"
            artifactId = "TRN-android"
            version = "v0.3.0"

            pom {
                name.set("TRN-android")
                description.set("The Root Network Android API")
            }
        }
    }
}
