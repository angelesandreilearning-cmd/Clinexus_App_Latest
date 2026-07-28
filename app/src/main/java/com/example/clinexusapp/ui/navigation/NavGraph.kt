package com.example.clinexusapp.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.clinexusapp.ui.screens.auth.*
import com.example.clinexusapp.ui.screens.main.MainScreen
import com.example.clinexusapp.ui.screens.appointments.AppointmentBookingScreen
import com.example.clinexusapp.ui.screens.appointments.AppointmentHistoryScreen
import com.example.clinexusapp.ui.screens.chat.ChatScreen
import com.example.clinexusapp.ui.screens.notifications.NotificationScreen
import com.example.clinexusapp.ui.screens.settings.SettingsScreen
import com.example.clinexusapp.viewmodel.SettingsViewModel

@Composable
fun SetupNavGraph(navController: NavHostController, settingsViewModel: SettingsViewModel) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        enterTransition = { fadeIn(tween(400)) + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(400)) },
        exitTransition = { fadeOut(tween(400)) + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(400)) },
        popEnterTransition = { fadeIn(tween(400)) + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(400)) },
        popExitTransition = { fadeOut(tween(400)) + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(400)) }
    ) {
        // ... (auth routes)
        composable(route = Screen.Splash.route) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        composable(route = Screen.Onboarding.route) {
            OnboardingScreen(
                onFinish = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        composable(route = Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }
        composable(route = Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.OTP.route)
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = Screen.OTP.route) {
            OTPVerificationScreen(
                onVerifySuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onResendOTP = { /* Simulated */ }
            )
        }
        composable(route = Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onSendResetLink = {
                    navController.popBackStack()
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = Screen.Home.route) {
            MainScreen(rootNavController = navController, settingsViewModel = settingsViewModel)
        }
        composable(route = Screen.AppointmentBooking.route) {
            AppointmentBookingScreen(
                onBack = { navController.popBackStack() },
                onBookSuccess = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = Screen.AppointmentHistory.route) {
            AppointmentHistoryScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(route = Screen.Chat.route) {
            ChatScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(route = Screen.Notifications.route) {
            NotificationScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                settingsViewModel = settingsViewModel
            )
        }
    }
}
