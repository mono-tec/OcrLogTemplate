package io.github.monotec.ocrlogger.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.github.monotec.ocrlogger.database.SendLogDao
import io.github.monotec.ocrlogger.ui.HomeScreen
import io.github.monotec.ocrlogger.ui.LicenseScreen
import io.github.monotec.ocrlogger.ui.LogScreen
import io.github.monotec.ocrlogger.ui.ScanScreen
import io.github.monotec.ocrlogger.ui.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    dao: SendLogDao,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onOpenScan = { navController.navigate(Screen.Scan.route) },
                onOpenLog = { navController.navigate(Screen.Log.route) },
                onOpenLicense = { navController.navigate(Screen.License.route) },
                onOpenSettings = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(Screen.Scan.route) {
            ScanScreen(dao = dao)
        }

        composable(Screen.Log.route) {
            LogScreen(dao = dao)
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }

        composable(Screen.License.route) {
            LicenseScreen()
        }
    }
}