package com.example.clinexusapp.ui.navigation

sealed class Screen(val route: String) {
    // Auth
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Register : Screen("register")
    object OTP : Screen("otp")
    object ForgotPassword : Screen("forgot_password")

    // Main
    object Home : Screen("home")
    object Dashboard : Screen("dashboard")
    object DoctorList : Screen("doctor_list")
    object AppointmentBooking : Screen("appointment_booking")
    object AppointmentHistory : Screen("appointment_history")
    object Chat : Screen("chat")
    object Notifications : Screen("notifications")
    object Settings : Screen("settings")
    object Profile : Screen("profile")
}
