package io.github.monotec.ocrlogger.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Scan : Screen("scan")
    data object Log : Screen("log")
    data object License : Screen("license")
    data object Settings : Screen("settings")
}