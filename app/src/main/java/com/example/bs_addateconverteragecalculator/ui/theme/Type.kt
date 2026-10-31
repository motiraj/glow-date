package com.example.bs_addateconverteragecalculator.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.bs_addateconverteragecalculator.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val NotoSansDevanagari = FontFamily(
    Font(googleFont = GoogleFont("Noto Sans Devanagari"), fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = GoogleFont("Noto Sans Devanagari"), fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = GoogleFont("Noto Sans Devanagari"), fontProvider = provider, weight = FontWeight.SemiBold),
    Font(googleFont = GoogleFont("Noto Sans Devanagari"), fontProvider = provider, weight = FontWeight.Bold),
    Font(googleFont = GoogleFont("Noto Sans Devanagari"), fontProvider = provider, weight = FontWeight.ExtraBold)
)

val NotoSerifDevanagari = FontFamily(
    Font(googleFont = GoogleFont("Noto Serif Devanagari"), fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = GoogleFont("Noto Serif Devanagari"), fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = GoogleFont("Noto Serif Devanagari"), fontProvider = provider, weight = FontWeight.SemiBold),
    Font(googleFont = GoogleFont("Noto Serif Devanagari"), fontProvider = provider, weight = FontWeight.Bold),
    Font(googleFont = GoogleFont("Noto Serif Devanagari"), fontProvider = provider, weight = FontWeight.ExtraBold)
)

// Material 3 Typography scale
// +10% line height for Devanagari matras (e.g. 24sp * 1.1 = ~26.4sp, we round up)
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = NotoSerifDevanagari,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = 0.sp
    ),
    displayMedium = TextStyle(
        fontFamily = NotoSerifDevanagari,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = NotoSerifDevanagari,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = NotoSerifDevanagari,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = NotoSerifDevanagari,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = NotoSerifDevanagari,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = NotoSerifDevanagari,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = NotoSerifDevanagari,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = NotoSerifDevanagari,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = NotoSansDevanagari,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 26.sp, // Min 16sp + ~10% line-height (was 24sp standard)
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = NotoSansDevanagari,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp, // Was 20sp
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = NotoSansDevanagari,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp, // Was 16sp
        letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontFamily = NotoSansDevanagari,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 22.sp, // Was 20sp
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = NotoSansDevanagari,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 18.sp, // Was 16sp
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = NotoSansDevanagari,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp, // Min 12sp per constraints (M3 default is 11sp, bumping to 12sp)
        lineHeight = 18.sp,
        letterSpacing = 0.5.sp
    )
)
