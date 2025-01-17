/*
 * Designed and developed by 2022 SungbinLand, Team Duckie
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/sungbinland/quack-quack/blob/main/LICENSE
 */

@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    `kotlin-dsl`
    alias(libs.plugins.dokka)
}

group = "team.duckie.quackquack.convention"

dependencies {
    // Unresolved reference: implementations
    implementation(libs.build.gradle)
    implementation(libs.build.kotlin)
    implementation(libs.build.kover)
    implementation(libs.build.dokka.base)
    implementation(libs.build.dokka.plugin)
}

gradlePlugin {
    val prefix = "quackquack"
    plugins {
        register("androidLintPlugin") {
            id = "$prefix.android.lint"
            implementationClass = "AndroidLintPlugin"
        }
        register("androidCommonLintPlugin") {
            id = "$prefix.android.common.lint"
            implementationClass = "AndroidCommonLintPlugin"
        }
        register("androidBenchmarkPlugin") {
            id = "$prefix.android.benchmark"
            implementationClass = "AndroidBenchmarkPlugin"
        }
        register("androidApplicationPlugin") {
            id = "$prefix.android.application"
            implementationClass = "AndroidApplicationPlugin"
        }
        register("androidApplicationComposePlugin") {
            id = "$prefix.android.application.compose"
            implementationClass = "AndroidApplicationComposePlugin"
        }
        register("androidLibraryPlugin") {
            id = "$prefix.android.library"
            implementationClass = "AndroidLibraryPlugin"
        }
        register("androidLibraryComposePlugin") {
            id = "$prefix.android.library.compose"
            implementationClass = "AndroidLibraryComposePlugin"
        }
        register("androidLibraryComposeUiTestPlugin") {
            id = "$prefix.android.library.compose.uitest"
            implementationClass = "AndroidLibraryComposeUiTestPlugin"
        }
        register("jvmKoverPlugin") {
            id = "$prefix.jvm.kover"
            implementationClass = "JvmKoverPlugin"
        }
        register("jvmLibraryPlugin") {
            id = "$prefix.jvm.library"
            implementationClass = "JvmLibraryPlugin"
        }
        register("jvmDokkaPlugin") {
            id = "$prefix.jvm.dokka"
            implementationClass = "JvmDokkaPlugin"
        }
    }
}
