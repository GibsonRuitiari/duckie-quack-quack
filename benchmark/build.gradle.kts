/*
 * Designed and developed by 2022 SungbinLand, Team Duckie
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/sungbinland/quack-quack/blob/main/LICENSE
 */

@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id(PluginEnum.AndroidBenchmark)
    id(PluginEnum.JvmDokka)
    alias(libs.plugins.kover)
}

android {
    namespace = "team.duckie.quackquack.ui.benchmark"
}
