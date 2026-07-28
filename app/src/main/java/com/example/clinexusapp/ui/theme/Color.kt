package com.example.clinexusapp.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Bright & Premium Royal Blue
val BluePrimary = Color(0xFF0077B6) 
val BlueSecondary = Color(0xFF00B4D8)
val BlueDark = Color(0xFF023E8A)
val BlueLight = Color(0xFFADE8F4)
val BlueExtraLight = Color(0xFFE0F7FA) // Even brighter and cleaner

// Luminous Champagne Peach
val PeachPrimary = Color(0xFFFFE5D9) 
val PeachLight = Color(0xFFFFF9F0) // Brighter, creamier white
val PeachDark = Color(0xFFFEC89A)

// Minimalist Neutral Palette
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF1B1B1B) 
val GrayLight = Color(0xFFFCFCFD) // Brighter, near-pure white background
val GrayMedium = Color(0xFFF1F5F9)
val GrayDark = Color(0xFF64748B)

// Premium Luminous Gradients
val PremiumBlueGradient = Brush.verticalGradient(
    colors = listOf(BluePrimary, BlueDark)
)

val PremiumPeachGradient = Brush.verticalGradient(
    colors = listOf(PeachPrimary, Color(0xFFFFD7BA))
)

val SoftBlueGradient = Brush.horizontalGradient(
    colors = listOf(BlueExtraLight, White)
)

// Material 3 Mappings
val PrimaryBlue = BluePrimary
val OnPrimaryBlue = White
val PrimaryContainerBlue = BlueExtraLight
val OnPrimaryContainerBlue = BlueDark

val SecondaryPeach = PeachPrimary
val OnSecondaryPeach = Black
val SecondaryContainerPeach = PeachLight
val OnSecondaryContainerPeach = PeachDark
