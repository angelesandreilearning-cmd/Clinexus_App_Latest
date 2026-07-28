package com.example.clinexusapp.ui.screens.main

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.clinexusapp.ui.navigation.BottomBarScreen
import com.example.clinexusapp.ui.navigation.Screen
import com.example.clinexusapp.ui.screens.dashboard.DashboardScreen
import com.example.clinexusapp.ui.screens.doctors.DoctorListScreen
import com.example.clinexusapp.ui.screens.chat.ChatScreen
import com.example.clinexusapp.ui.screens.profile.ProfileScreen
import com.example.clinexusapp.ui.theme.BluePrimary
import com.example.clinexusapp.viewmodel.SettingsViewModel

@Composable
fun MainScreen(rootNavController: NavHostController, settingsViewModel: SettingsViewModel) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screen.Dashboard.route) {
                DashboardScreen(navController, rootNavController)
            }
            composable(route = Screen.DoctorList.route) {
                DoctorListScreen(
                    onDoctorClick = { name ->
                        rootNavController.navigate(Screen.AppointmentBooking.route)
                    }
                )
            }
            composable(route = Screen.Chat.route) {
                ChatScreen(onBack = { navController.popBackStack() })
            }
            composable(route = Screen.Profile.route) {
                ProfileScreen(
                    onLogout = {
                        rootNavController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                    onNavigateToSettings = {
                        rootNavController.navigate(Screen.Settings.route)
                    }
                )
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Dashboard,
        BottomBarScreen.Doctors,
        BottomBarScreen.Chat,
        BottomBarScreen.Profile
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        contentColor = BluePrimary,
        tonalElevation = 8.dp,
        modifier = Modifier.shadow(16.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
    ) {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    NavigationBarItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = BluePrimary,
            selectedTextColor = BluePrimary,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            indicatorColor = MaterialTheme.colorScheme.secondaryContainer
        )
    )
}
