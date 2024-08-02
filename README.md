# TRN-android [![](https://jitpack.io/v/girin-app/TRN-android.svg)](https://jitpack.io/#girin-app/TRN-android) 

The Root Network Android API

## Quickstart
All releases are published to jitpack. Changelog of each release can be found under [Releases](https://github.com/girin-app/TRN-android/releases).

1. Add it in `build.gradle` at the end of repositories:
```kt
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}
```
2. Add the dependency
```kt
dependencies {
    implementation('com.github.girin-app:TRN-android:0.2.1')
}
```
