//import java.util.Properties
//import java.io.FileInputStream
import java.io.File
import java.util.*

val keystoreProperties =
    Properties().apply {
        var file = File("key.properties")
        if (file.exists()) load(file.reader())
    }

plugins {
    id("com.android.application")
    id("kotlin-android")
    // The Flutter Gradle Plugin must be applied after the Android and Kotlin Gradle plugins.
    id("dev.flutter.flutter-gradle-plugin")
}

//val keystoreProperties = Properties()
//val keystorePropertiesFile = rootProject.file("key.properties")
//if (keystorePropertiesFile.exists()) {
//    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
//}

android {
    namespace = "com.example.ci_alura"
    compileSdk = flutter.compileSdkVersion
    ndkVersion = flutter.ndkVersion

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    val appVersionCode = (System.getenv()["NEW_BUILD_NUMBER"] ?: "1")?.toInt()
    defaultConfig {
        // TODO: Specify your own unique Application ID (https://developer.android.com/studio/build/application-id.html).
        applicationId = "com.example.ci_alura"
        // You can update the following values to match your application needs.
        // For more information, see: https://flutter.dev/to/review-gradle-config.
        minSdk = flutter.minSdkVersion
        targetSdk = flutter.targetSdkVersion
        versionCode = appVersionCode
        versionName = flutter.versionName
    }
    signingConfigs {
        create("release") {
                if (System.getenv()["CI"].toBoolean()) { // CI=true is exported by Codemagic
                storeFile = file(System.getenv()["CM_KEYSTORE_PATH"])
                storePassword = System.getenv()["CM_KEYSTORE_PASSWORD"]
                keyAlias = System.getenv()["CM_KEY_ALIAS"]
                keyPassword = System.getenv()["CM_KEY_PASSWORD"]
            } else {
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
                storeFile = keystoreProperties["storeFile"]?.let { file(it) }
                storePassword = keystoreProperties["storePassword"] as String
            }
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnable = false
            signingConfig = signingConfigs.getByName("debug")
            signingConfig = signingConfigs.getByName("release")
        }
    }
}

flutter {
    source = "../.."
}
