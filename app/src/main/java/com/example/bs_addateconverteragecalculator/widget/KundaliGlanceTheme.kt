package com.example.bs_addateconverteragecalculator.widget

import android.content.Context
import android.content.Intent
import androidx.compose.ui.graphics.Color
import androidx.glance.action.Action
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.unit.ColorProvider
import com.example.bs_addateconverteragecalculator.MainActivity

object KundaliWidgetTheme {
    val DeepCosmicNavyColor = Color(0xFF0D1323)
    val MidnightSlateColor = Color(0xFF0F172A)
    val SolarAmberColor = Color(0xFFF59E0B)
    val SolarOrangeColor = Color(0xFFF97316)
    val CrispWhiteColor = Color(0xFFFFFFFF)
    val SlateSilverColor = Color(0xFF94A3B8)
    val MutedRedColor = Color(0xFFEF4444)

    @Suppress("RestrictedApi")
    val DeepCosmicNavy = ColorProvider(DeepCosmicNavyColor)
    @Suppress("RestrictedApi")
    val MidnightSlate = ColorProvider(MidnightSlateColor)
    @Suppress("RestrictedApi")
    val SolarAmber = ColorProvider(SolarAmberColor)
    @Suppress("RestrictedApi")
    val SolarOrange = ColorProvider(SolarOrangeColor)
    @Suppress("RestrictedApi")
    val CrispWhite = ColorProvider(CrispWhiteColor)
    @Suppress("RestrictedApi")
    val SlateSilver = ColorProvider(SlateSilverColor)
    @Suppress("RestrictedApi")
    val MutedRed = ColorProvider(MutedRedColor)

    val KeyguardScrimColor = Color(0xCC0D1323)
    @Suppress("RestrictedApi")
    val KeyguardScrim = ColorProvider(KeyguardScrimColor)

    fun getDeepLinkAction(context: Context, destination: String): Action {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra("EXTRA_DESTINATION", destination)
            putExtra("route", destination)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        return actionStartActivity(intent)
    }
}
