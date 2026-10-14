package ru.kaminfsol.notes

import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Главная")
    object Settings : BottomNavItem("settings", Icons.Default.Settings, "Настройки")
    object About : BottomNavItem("about", Icons.Default.Info, "О приложении")
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val bottomBarScreens = listOf(
        BottomNavItem.Home.route,
        BottomNavItem.Settings.route,
        BottomNavItem.About.route
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = currentDestination?.route in bottomBarScreens

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    val items = listOf(BottomNavItem.Home, BottomNavItem.Settings, BottomNavItem.About)
                    items.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(onAddNoteClick = { navController.navigate("add_note") })
            }
            composable(BottomNavItem.Settings.route) { SettingsScreen() }
            composable(BottomNavItem.About.route) { AboutScreen() }
            composable("add_note") {
                val viewModel = viewModel<NoteViewModel>(
                    factory = NoteViewModel.Factory(androidx.compose.ui.platform.LocalContext.current.applicationContext as Application)
                )
                AddNoteScreen(
                    onBackClick = { navController.popBackStack() },
                    onSaveClick = { title, content ->
                        viewModel.saveNote(title, content)
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}