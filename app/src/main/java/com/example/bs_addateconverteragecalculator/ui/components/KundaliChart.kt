@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bs_addateconverteragecalculator.data.NepaliNumerals
import com.example.bs_addateconverteragecalculator.data.OfflineVedicEphemeris
import com.example.bs_addateconverteragecalculator.data.PlanetarySnapshot

enum class KundaliChartType {
    LAGNA,
    NAVAMSHA,
    CHANDRA
}

data class KundaliPlanetInfo(
    val id: String,
    val nameDevanagari: String,
    val shortSymbol: String,      // सू, च, मं, बु, गु, शु, श, रा, के
    val rashiIndex: Int,           // 1..12
    val exactLon: Double,
    val isRetrograde: Boolean = false
)

@OptIn(ExperimentalTextApi::class)
@Composable
fun KundaliChart(
    lagnaRashiIndex: Int,          // 1..12
    lagnaRashiName: String,
    snapshot: PlanetarySnapshot,
    modifier: Modifier = Modifier,
    isMoonChart: Boolean = false,
    chartType: KundaliChartType = if (isMoonChart) KundaliChartType.CHANDRA else KundaliChartType.LAGNA,
    navamshaLagnaIndex: Int = 1,
    navamshaLagnaName: String = "मेष"
) {
    val textMeasurer = rememberTextMeasurer()
    val primaryColor = MaterialTheme.colorScheme.primary
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val primaryContainer = MaterialTheme.colorScheme.primaryContainer
    val errorColor = MaterialTheme.colorScheme.error

    val effectiveChartType = if (isMoonChart) KundaliChartType.CHANDRA else chartType

    // Calculate base Rashi depending on chart type
    val baseRashiIndex = when (effectiveChartType) {
        KundaliChartType.NAVAMSHA -> navamshaLagnaIndex
        KundaliChartType.CHANDRA -> snapshot.moonRashi
        KundaliChartType.LAGNA -> lagnaRashiIndex
    }

    val displayLagnaName = when (effectiveChartType) {
        KundaliChartType.NAVAMSHA -> navamshaLagnaName
        KundaliChartType.CHANDRA -> lagnaRashiName
        KundaliChartType.LAGNA -> lagnaRashiName
    }

    // Map planets to 1-based Rashi index
    val planets = remember(snapshot, effectiveChartType) {
        if (effectiveChartType == KundaliChartType.NAVAMSHA) {
            listOf(
                KundaliPlanetInfo("sun", "सूर्य", "सू", ((kotlin.math.floor(snapshot.sunLon * 0.3).toInt() % 12) + 1), snapshot.sunLon),
                KundaliPlanetInfo("moon", "चन्द्र", "च", ((kotlin.math.floor(snapshot.moonLon * 0.3).toInt() % 12) + 1), snapshot.moonLon),
                KundaliPlanetInfo("mars", "मंगल", "मं", ((kotlin.math.floor(snapshot.marsLon * 0.3).toInt() % 12) + 1), snapshot.marsLon, snapshot.isMarsRetrograde),
                KundaliPlanetInfo("mercury", "बुध", "बु", ((kotlin.math.floor(snapshot.mercuryLon * 0.3).toInt() % 12) + 1), snapshot.mercuryLon, snapshot.isMercuryRetrograde),
                KundaliPlanetInfo("jupiter", "बृहस्पति", "गु", ((kotlin.math.floor(snapshot.jupiterLon * 0.3).toInt() % 12) + 1), snapshot.jupiterLon, snapshot.isJupiterRetrograde),
                KundaliPlanetInfo("venus", "शुक्र", "शु", ((kotlin.math.floor(snapshot.venusLon * 0.3).toInt() % 12) + 1), snapshot.venusLon, snapshot.isVenusRetrograde),
                KundaliPlanetInfo("saturn", "शनि", "श", ((kotlin.math.floor(snapshot.saturnLon * 0.3).toInt() % 12) + 1), snapshot.saturnLon, snapshot.isSaturnRetrograde),
                KundaliPlanetInfo("rahu", "राहु", "रा", ((kotlin.math.floor(snapshot.rahuLon * 0.3).toInt() % 12) + 1), snapshot.rahuLon, true),
                KundaliPlanetInfo("ketu", "केतु", "के", ((kotlin.math.floor(snapshot.ketuLon * 0.3).toInt() % 12) + 1), snapshot.ketuLon, true)
            )
        } else {
            listOf(
                KundaliPlanetInfo("sun", "सूर्य", "सू", snapshot.sunRashi, snapshot.sunLon),
                KundaliPlanetInfo("moon", "चन्द्र", "च", snapshot.moonRashi, snapshot.moonLon),
                KundaliPlanetInfo("mars", "मंगल", "मं", snapshot.marsRashi, snapshot.marsLon, snapshot.isMarsRetrograde),
                KundaliPlanetInfo("mercury", "बुध", "बु", snapshot.mercuryRashi, snapshot.mercuryLon, snapshot.isMercuryRetrograde),
                KundaliPlanetInfo("jupiter", "बृहस्पति", "गु", snapshot.jupiterRashi, snapshot.jupiterLon, snapshot.isJupiterRetrograde),
                KundaliPlanetInfo("venus", "शुक्र", "शु", snapshot.venusRashi, snapshot.venusLon, snapshot.isVenusRetrograde),
                KundaliPlanetInfo("saturn", "शनि", "श", snapshot.saturnRashi, snapshot.saturnLon, snapshot.isSaturnRetrograde),
                KundaliPlanetInfo("rahu", "राहु", "रा", snapshot.rahuRashi, snapshot.rahuLon, true),
                KundaliPlanetInfo("ketu", "केतु", "के", snapshot.ketuRashi, snapshot.ketuLon, true)
            )
        }
    }

    // Group planets by House (1..12) relative to baseRashiIndex
    val housePlanetsMap = remember(baseRashiIndex, planets) {
        val map = mutableMapOf<Int, MutableList<KundaliPlanetInfo>>()
        for (h in 1..12) map[h] = mutableListOf()

        planets.forEach { planet ->
            var house = ((planet.rashiIndex - baseRashiIndex + 12) % 12) + 1
            map[house]?.add(planet)
        }
        map
    }

    // Centers for 12 Houses in North-Indian Diamond Layout (in normalized 0..1 coordinates)
    val houseCenters = remember {
        listOf(
            Offset(0.50f, 0.22f), // House 1 (Lagna / Top Center Diamond)
            Offset(0.70f, 0.10f), // House 2 (Top Right Triangle)
            Offset(0.90f, 0.25f), // House 3 (Right Top Triangle)
            Offset(0.75f, 0.50f), // House 4 (Right Center Diamond)
            Offset(0.90f, 0.75f), // House 5 (Right Bottom Triangle)
            Offset(0.70f, 0.90f), // House 6 (Bottom Right Triangle)
            Offset(0.50f, 0.78f), // House 7 (Bottom Center Diamond)
            Offset(0.30f, 0.90f), // House 8 (Bottom Left Triangle)
            Offset(0.10f, 0.75f), // House 9 (Left Bottom Triangle)
            Offset(0.25f, 0.50f), // House 10 (Left Center Diamond)
            Offset(0.10f, 0.25f), // House 11 (Left Top Triangle)
            Offset(0.30f, 0.10f)  // House 12 (Top Left Triangle)
        )
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Chart Title
            val chartTitle = when (effectiveChartType) {
                KundaliChartType.NAVAMSHA -> "नवमांश कुण्डली चक्र (D-9 Navamsha)"
                KundaliChartType.CHANDRA -> "चन्द्र कुण्डली चक्र (Moon Chart)"
                KundaliChartType.LAGNA -> "जन्म लग्न कुण्डली चक्र (D-1 Lagna)"
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = chartTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )
                Surface(
                    color = primaryContainer,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "लग्न: $displayLagnaName",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // North Indian Diamond Canvas Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val strokePx = 2.dp.toPx()

                    // 1. Draw Outer Frame
                    drawRect(
                        color = primaryColor,
                        style = Stroke(width = strokePx)
                    )

                    // 2. Draw Main Diagonals
                    drawLine(primaryColor, Offset(0f, 0f), Offset(w, h), strokeWidth = strokePx)
                    drawLine(primaryColor, Offset(w, 0f), Offset(0f, h), strokeWidth = strokePx)

                    // 3. Draw Inner Diamond
                    val diamondPath = Path().apply {
                        moveTo(w * 0.5f, 0f)
                        lineTo(w, h * 0.5f)
                        lineTo(w * 0.5f, h)
                        lineTo(0f, h * 0.5f)
                        close()
                    }
                    drawPath(path = diamondPath, color = primaryColor, style = Stroke(width = strokePx))

                    // 4. Render House Rashi Numbers and Planets
                    for (houseIndex in 1..12) {
                        val center = houseCenters[houseIndex - 1]
                        val cx = center.x * w
                        val cy = center.y * h

                        // Rashi Number for this house
                        val houseRashiNumber = ((baseRashiIndex - 1 + houseIndex - 1) % 12) + 1
                        val rashiTextNe = NepaliNumerals.toNepaliDigits(houseRashiNumber)

                        // Render Rashi Number
                        val rashiLayout = textMeasurer.measure(
                            text = AnnotatedString(rashiTextNe),
                            style = TextStyle(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = onSurfaceVariant
                            )
                        )

                        // Draw Rashi Number at offset
                        val rashiX = cx - (rashiLayout.size.width / 2f)
                        val rashiY = cy - 14.dp.toPx()
                        drawText(rashiLayout, topLeft = Offset(rashiX, rashiY))

                        // Build Planets Text in this House
                        val plist = housePlanetsMap[houseIndex] ?: emptyList()
                        val planetStr = buildString {
                            if (houseIndex == 1) {
                                append("ल ")
                            }
                            plist.forEachIndexed { i, p ->
                                append(p.shortSymbol)
                                if (p.isRetrograde) append("(व)")
                                if (i < plist.size - 1) append(",")
                            }
                        }.trim()

                        if (planetStr.isNotEmpty()) {
                            val planetLayout = textMeasurer.measure(
                                text = AnnotatedString(planetStr),
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (houseIndex == 1) primaryColor else onSurfaceColor
                                )
                            )
                            val pX = cx - (planetLayout.size.width / 2f)
                            val pY = cy + 2.dp.toPx()
                            drawText(planetLayout, topLeft = Offset(pX, pY))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Planetary DMS Degrees Table Summary below Chart
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "ग्रह स्पष्ट तथा अक्षांश (Nirayana Planetary Positions)",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(6.dp))

            // 3-Column Grid for Planets DMS
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                val rowPlanets = planets.chunked(3)
                rowPlanets.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        row.forEach { p ->
                            val rak = OfflineVedicEphemeris.toRashiAmshaKala(p.exactLon)
                            val degStr = "${p.nameDevanagari}: ${rak.rashiName} ${NepaliNumerals.toNepaliDigits(rak.degrees)}°${NepaliNumerals.toNepaliDigits(String.format(java.util.Locale.US, "%02d", rak.arcMinutes))}'"

                            Text(
                                text = degStr,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (p.isRetrograde) errorColor else onSurfaceColor,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}
