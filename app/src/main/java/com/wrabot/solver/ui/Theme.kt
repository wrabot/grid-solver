package com.wrabot.solver.ui

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.wrabot.solver.R

val fontFamily = FontFamily(listOf(Font(R.font.source_code_pro_regular)))

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFb1c5ff),
    secondary = Color(0xFF1de60e),
    tertiary = Color(0xFFffb86c)
)

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0056d0),
    secondary = Color(0xFF056e00),
    tertiary = Color(0xFF895100)
)
