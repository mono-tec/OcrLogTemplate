package io.github.monotec.ocrlogger.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.monotec.ocrlogger.database.SendLogDao
import io.github.monotec.ocrlogger.ui.navigation.AppNavHost
import io.github.monotec.ocrlogger.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    dao: SendLogDao
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val isHome = currentRoute == Screen.Home.route
    val showBottomBar =
        currentRoute == Screen.Scan.route ||
                currentRoute == Screen.Log.route ||
                currentRoute == Screen.Settings.route

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = {
                if (!isHome) {
                    TopAppBar(
                        title = {
                            Text(
                                when (currentRoute) {
                                    Screen.Scan.route -> "Scan"
                                    Screen.Log.route -> "Log"
                                    Screen.License.route -> "Libraries / Licenses"
                                    Screen.Settings.route -> "Settings"
                                    else -> "OCRLogger"
                                }
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    navController.navigate(Screen.Home.route) {
                                        launchSingleTop = true
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Home,
                                    contentDescription = "Home"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xFF1976D2),
                            titleContentColor = Color.White,
                            navigationIconContentColor = Color.White
                        )
                    )
                }
            },
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar {
                        NavigationBarItem(
                            selected = currentRoute == Screen.Scan.route,
                            onClick = {
                                navController.navigate(Screen.Scan.route) {
                                    launchSingleTop = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Outlined.PhotoCamera,
                                    contentDescription = "Scan"
                                )
                            },
                            label = { Text("Scan") }
                        )

                        NavigationBarItem(
                            selected = currentRoute == Screen.Log.route,
                            onClick = {
                                navController.navigate(Screen.Log.route) {
                                    launchSingleTop = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Outlined.History,
                                    contentDescription = "Log"
                                )
                            },
                            label = { Text("Log") }
                        )

                        NavigationBarItem(
                            selected = currentRoute == Screen.Settings.route,
                            onClick = {
                                navController.navigate(Screen.Settings.route) {
                                    launchSingleTop = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Outlined.Settings,
                                    contentDescription = "Settings"
                                )
                            },
                            label = { Text("Settings") }
                        )
                    }
                }
            }
        ) { innerPadding ->
            AppNavHost(
                navController = navController,
                dao = dao,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}