package com.example.bs_addateconverteragecalculator.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bs_addateconverteragecalculator.ui.theme.NotoSansDevanagari
import com.example.bs_addateconverteragecalculator.ui.theme.NotoSerifDevanagari
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun GlowDateSplashScreen(onTimeout: () -> Unit) {
    // 1. Visual & Aesthetic Architecture Colors
    val vedicNavy = Color(0xFF0D1323)
    val midnightSlate = Color(0xFF0F172A)
    val radiantGold = Color(0xFFF59E0B)
    val solarAmber = Color(0xFFF97316)
    val slateLightSilver = Color(0xFF94A3B8)
    val crispWhite = Color(0xFFFFFFFF)

    val backgroundBrush = Brush.radialGradient(
        colors = listOf(midnightSlate, vedicNavy),
        radius = 1500f
    )

    val goldBrush = Brush.horizontalGradient(
        colors = listOf(solarAmber, radiantGold, solarAmber)
    )

    // 2. Animation States
    val omScale = remember { Animatable(0.7f) }
    val omAlpha = remember { Animatable(0f) }
    val haloAlpha = remember { Animatable(0f) }
    val haloScale = remember { Animatable(0.5f) }

    val ganeshOffsetY = remember { Animatable(25f) }
    val ganeshAlpha = remember { Animatable(0f) }

    val titleScale = remember { Animatable(0.9f) }
    val titleAlpha = remember { Animatable(0f) }
    val underlineScaleX = remember { Animatable(0f) }

    val taglineAlpha = remember { Animatable(0f) }

    // 3. Choreography
    LaunchedEffect(Unit) {
        // Phase 1 (0.0s - 0.7s): "ॐ" emerges with scale, alpha, and halo
        launch {
            omScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
            )
        }
        launch {
            omAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 700, easing = LinearOutSlowInEasing)
            )
        }
        launch {
            haloAlpha.animateTo(
                targetValue = 0.6f,
                animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
            )
        }
        launch {
            haloScale.animateTo(
                targetValue = 1.5f,
                animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing)
            )
        }

        // Phase 2 (0.5s - 1.2s): "॥ श्री गणेशाय नमः ॥" slides up and fades in
        delay(500.milliseconds)
        launch {
            ganeshOffsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
            )
        }
        launch {
            ganeshAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 700, easing = LinearOutSlowInEasing)
            )
        }

        // Phase 3 (1.0s - 1.8s): "Glow Date" illuminates with underline
        delay(500.milliseconds)
        launch {
            titleScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
            )
        }
        launch {
            titleAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing)
            )
        }
        launch {
            underlineScaleX.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
            )
        }

        // Phase 4 (1.5s - 2.2s): Tagline fades in
        delay(500.milliseconds)
        launch {
            taglineAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 700, easing = LinearOutSlowInEasing)
            )
        }

        // Phase 5 (2.5s - 3.0s): Wait for breathing pulse to finish, then navigate
        delay(1000.milliseconds)
        onTimeout()
    }

    // 4. UI Composition
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            // Line 1: Sacred "ॐ" with Glowing Halo
            Box(contentAlignment = Alignment.Center) {
                // Soft Solar Halo
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .graphicsLayer {
                            scaleX = haloScale.value
                            scaleY = haloScale.value
                            alpha = haloAlpha.value
                        }
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(radiantGold.copy(alpha = 0.5f), Color.Transparent)
                            )
                        )
                )
                // "ॐ" Text
                Text(
                    text = "ॐ",
                    fontSize = 110.sp,
                    fontFamily = NotoSerifDevanagari,
                    fontWeight = FontWeight.ExtraBold,
                    color = radiantGold,
                    modifier = Modifier.graphicsLayer {
                        scaleX = omScale.value
                        scaleY = omScale.value
                        alpha = omAlpha.value
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Line 2: ॥ श्री गणेशाय नमः ॥
            Text(
                text = "॥ श्री गणेशाय नमः ॥",
                fontSize = 28.sp,
                fontFamily = NotoSerifDevanagari,
                fontWeight = FontWeight.Bold,
                color = solarAmber,
                modifier = Modifier.graphicsLayer {
                    translationY = ganeshOffsetY.value
                    alpha = ganeshAlpha.value
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Line 3: App Title & Underline
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.graphicsLayer {
                    scaleX = titleScale.value
                    scaleY = titleScale.value
                    alpha = titleAlpha.value
                }
            ) {
                Text(
                    text = "Glow Date",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = crispWhite,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                // Golden Underline expanding from center
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(3.dp)
                        .graphicsLayer {
                            scaleX = underlineScaleX.value
                            transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0.5f, 0.5f)
                        }
                        .background(goldBrush)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Line 4: Tagline
            Text(
                text = "पञ्चाङ्ग, राशिफल, पात्रो एवम् मिति परिवर्तन",
                fontSize = 16.sp,
                fontFamily = NotoSansDevanagari,
                fontWeight = FontWeight.Medium,
                color = slateLightSilver,
                textAlign = TextAlign.Center,
                letterSpacing = 0.5.sp, // Slight spread
                lineHeight = 24.sp,
                modifier = Modifier.graphicsLayer {
                    alpha = taglineAlpha.value
                }
            )
        }
    }
}
