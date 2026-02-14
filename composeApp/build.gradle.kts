import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.buildKonfig)
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

buildkonfig {
    packageName = "com.jmabilon.chefmate"

    val secureProperties = Properties().apply {
        rootProject.file("secure.properties").takeIf { it.exists() }
            ?.inputStream()
            ?.use { load(it) }
    }

    // =============================================================================================
    //  Props
    // =============================================================================================

    //  These props are used to read the values from secure.properties and set them as BuildConfig fields.
    val supabaseUrlDev = "SUPABASE_URL_DEV"
    val supabaseApiKeyDev = "SUPABASE_API_KEY_DEV"
    val supabaseUrlProd = "SUPABASE_URL_PROD"
    val supabaseApiKeyProd = "SUPABASE_API_KEY_PROD"

    // These props are the actual BuildConfig field names that will be generated in the code.
    val supabaseUrl = "SUPABASE_URL"
    val supabaseApiKey = "SUPABASE_API_KEY"


    // =============================================================================================
    //  Default
    // =============================================================================================

    defaultConfigs { /* no-op */ }

    // =============================================================================================
    //  Development
    // =============================================================================================

    defaultConfigs("dev") {
        buildConfigField(
            STRING,
            supabaseUrl,
            "${secureProperties.getProperty(supabaseUrlDev, "")}"
        )

        buildConfigField(
            STRING,
            supabaseApiKey,
            "${secureProperties.getProperty(supabaseApiKeyDev, "")}"
        )
    }

    // =============================================================================================
    //  Production
    // =============================================================================================

    defaultConfigs("prod") {
        buildConfigField(
            STRING,
            supabaseUrl,
            "${secureProperties.getProperty(supabaseUrlProd, "")}"
        )

        buildConfigField(
            STRING,
            supabaseApiKey,
            "${secureProperties.getProperty(supabaseApiKeyProd, "")}"
        )
    }
}
