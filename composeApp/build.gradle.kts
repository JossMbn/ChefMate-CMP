import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    androidLibrary {
        namespace = "com.jmabilon.chefmate.composeApp"
        compileSdk = libs.versions.android.compileSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }

        androidResources {
            enable = true
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.bundles.common.compose)
            implementation(libs.androidx.navigation.compose)

            // Koin
            implementation(libs.bundles.common.koin)

            // Lifecycle
            implementation(libs.bundles.androidx.lifecycle.compose)

            // Coroutines
            implementation(libs.kotlinx.coroutines.core)

            // Supabase
            implementation(project.dependencies.platform(libs.supabase.bom))
            implementation(libs.bundles.common.supabase)

            // Ktor
            implementation(libs.ktor.client.core)

            // Serialization
            implementation(libs.kotlinx.serialization.json)

            // Coil
            implementation(libs.bundles.common.coil)
        }
        iosMain.dependencies {
            // Ktor
            implementation(libs.ktor.client.darwin)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}
