package com.example.clinexusapp.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.clinexusapp.api.AuthRepository
import com.example.clinexusapp.api.RetrofitClient
import com.example.clinexusapp.ui.navigation.Screen
import com.example.clinexusapp.ui.screens.auth.*
import com.example.clinexusapp.ui.screens.main.MainScreen
import com.example.clinexusapp.ui.screens.appointments.AppointmentBookingScreen
import com.example.clinexusapp.ui.screens.appointments.AppointmentHistoryScreen
import com.example.clinexusapp.ui.screens.chat.ChatScreen
import com.example.clinexusapp.ui.screens.notifications.NotificationScreen
import com.example.clinexusapp.ui.screens.settings.SettingsScreen
import com.example.clinexusapp.viewmodel.*

@Composable
fun SetupNavGraph(navController: NavHostController, settingsViewModel: SettingsViewModel) {
    val repository = AuthRepository(RetrofitClient.instance)
    val factory = ViewModelFactory(repository)

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        enterTransition = { fadeIn(tween(400)) + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(400)) },
        exitTransition = { fadeOut(tween(400)) + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(400)) },
        popEnterTransition = { fadeIn(tween(400)) + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(400)) },
        popExitTransition = { fadeOut(tween(400)) + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(400)) }
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
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
            val loginViewModel: LoginViewModel = viewModel(factory = factory)
            LoginScreen(
                viewModel = loginViewModel,
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }
        composable(route = Screen.Register.route) {
            val registerViewModel: RegisterViewModel = viewModel(factory = factory)
            RegisterScreen(
                viewModel = registerViewModel,
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.OTP.route)
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
            val historyViewModel: HistoryViewModel = viewModel(factory = factory)
            AppointmentHistoryScreen(
                onBack = { navController.popBackStack() },
                onNavigateToBooking = {
                    navController.navigate(Screen.AppointmentBooking.route)
                },
                viewModel = historyViewModel
            )
        }
        composable(route = Screen.Chat.route) {
            val chatViewModel: ChatViewModel = viewModel(factory = factory)
            ChatScreen(
                onBack = { navController.popBackStack() },
                viewModel = chatViewModel
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
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                settingsViewModel = settingsViewModel
            )
        }
    }
}
