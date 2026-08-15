package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val BgColor = Color(0xFFF2F4FC)
val SurfaceSolid = Color(0xFFFFFFFF)
val SurfaceTranslucent = Color(0xB8FFFFFF)
val VioletPrimary = Color(0xFF6C5CE7)
val VioletDim = Color(0xFF5646C9)
val VioletLight = Color(0xFF8B7CF6)
val CyanAccent = Color(0xFF22D3EE)
val CyanLight = Color(0xFF67E8F9)
val PinkAccent = Color(0xFFF45FA0)
val PinkLight = Color(0xFFFB9DC6)
val PinkDim = Color(0xFFD6478C)
val AmberAccent = Color(0xFFFBBF24)
val AmberLight = Color(0xFFFDE68A)
val AmberDim = Color(0xFFB4790B)
val MintAccent = Color(0xFF2DD4A7)
val MintLight = Color(0xFF6EE7C9)
val MintDim = Color(0xFF149C78)
val TextColor = Color(0xFF20223B)
val TextMuted = Color(0xFF6B7089)
val BorderLine = Color(0x296C5CE7)
val DangerColor = Color(0xFFEF4B6A)
val DangerBg = Color(0x10EF4B6A)

val BrandGradient = Brush.linearGradient(
    colors = listOf(VioletPrimary, CyanAccent)
)

val BrandGradient120 = Brush.linearGradient(
    colors = listOf(VioletPrimary, VioletLight, CyanAccent)
)

val BrandGradientViolet = Brush.linearGradient(
    colors = listOf(VioletPrimary, VioletLight)
)
