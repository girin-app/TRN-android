plugins {
    id("org.jetbrains.kotlin.jvm")
    `maven-publish`
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

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "com.github.girin-app"
            artifactId = "TRN-android"
            version = "v0.3.1"

            pom {
                name.set("TRN-android")
                description.set("The Root Network Android API")
            }
        }
    }
}
