@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.ui.panchanga

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bs_addateconverteragecalculator.data.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LifeEventsSheet(
    natalProfile: NatalProfile,
    transitMap: Map<String, Int>,
    birthAdYear: Int,
    onDismiss: () -> Unit
) {
    val lang = com.example.bs_addateconverteragecalculator.utils.LocalAppLanguage.current

    val majorYogas = remember(natalProfile, transitMap) {
        LifeEventAnalyzerEngine.detectMajorYogas(natalProfile.lagnaIndex + 1, transitMap, lang)
    }

    val eventCategories = remember {
        LifeEventCategory.entries
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timeline,
                        contentDescription = "Life Events",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (lang == "en") "Life Events & Rajayoga Analysis" else "जीवनका प्रमुख घटनाहरू र राजयोग विश्लेषण",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // 1. ACTIVE RAJAYOGAS SECTION
                if (majorYogas.isNotEmpty()) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Stars,
                                    contentDescription = "Yogas",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (lang == "en") "Active Rajayogas (${majorYogas.size})" else "कुण्डलीमा सक्रिय मुख्य राजयोगहरू (${majorYogas.size})",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(majorYogas, key = { it.nameEnglish }) { yoga ->
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
                                        modifier = Modifier.width(260.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = if (lang == "en") yoga.nameEnglish else yoga.nameNepali,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    style = MaterialTheme.typography.titleSmall,
                                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                                )
                                                Surface(
                                                    color = MaterialTheme.colorScheme.primary,
                                                    shape = RoundedCornerShape(6.dp)
                                                ) {
                                                    Text(
                                                        text = "★ ${yoga.strengthStars}/5",
                                                        color = MaterialTheme.colorScheme.onPrimary,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = if (lang == "en") yoga.descriptionEnglish else yoga.descriptionNepali,
                                                style = MaterialTheme.typography.bodySmall,
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                lineHeight = 16.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 2. CHRONOLOGICAL LIFE TIMELINE ROADMAP
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Timeline,
                                contentDescription = "Timeline",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (lang == "en") "Chronological Life Roadmap (Timeline)" else "उमेर र वर्ष अनुसारको घटना टाइमलाइन (Roadmap)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        val currentAd = remember { java.util.Calendar.getInstance().get(java.util.Calendar.YEAR) }
                        val currentAge = (currentAd - birthAdYear).coerceAtLeast(18)

                        val timelineWindows = listOf(
                            Triple(
                                if (lang == "en") "Age $currentAge–${currentAge + 2}" else "उमेर $currentAge–${currentAge + 2} वर्ष",
                                if (lang == "en") "Years $currentAd–${currentAd + 2} AD (BS ${currentAd + 57}–${currentAd + 59})" else "सन् $currentAd–${currentAd + 2} AD (वि.सं. ${currentAd + 57}–${currentAd + 59} BS)",
                                if (lang == "en") "Jupiter Dasha → Career Promotion, New Projects & Dhana Yoga" else "बृहस्पति/शुक्र दशा → 💼 पदोन्नति, नयाँ कार्य र आर्थिक लाभ"
                            ),
                            Triple(
                                if (lang == "en") "Age ${currentAge + 3}–${currentAge + 5}" else "उमेर ${currentAge + 3}–${currentAge + 5} वर्ष",
                                if (lang == "en") "Years ${currentAd + 3}–${currentAd + 5} AD (BS ${currentAd + 60}–${currentAd + 62})" else "सन् ${currentAd + 3}–${currentAd + 5} AD (वि.सं. ${currentAd + 60}–${currentAd + 62} BS)",
                                if (lang == "en") "Jupiter/Sun Dasha → Marriage, Family Joy & Recognition" else "बृहस्पति/सूर्य दशा → 💍 विवाह र माङ्गलिक कार्यको योग"
                            ),
                            Triple(
                                if (lang == "en") "Age ${currentAge + 6}–${currentAge + 8}" else "उमेर ${currentAge + 6}–${currentAge + 8} वर्ष",
                                if (lang == "en") "Years ${currentAd + 6}–${currentAd + 8} AD (BS ${currentAd + 63}–${currentAd + 65})" else "सन् ${currentAd + 6}–${currentAd + 8} AD (वि.सं. ${currentAd + 63}–${currentAd + 65} BS)",
                                if (lang == "en") "Saturn/Mercury Dasha → Land Purchase & Permanent Assets" else "शनि/बुध दशा → 🏠 घर-जग्गा जोडिने र स्थायी सम्पत्ति वृद्धि"
                            )
                        )

                        timelineWindows.forEach { (ageStr, dateStr, eventStr) ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = ageStr,
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = dateStr,
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = eventStr,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                                            lineHeight = 16.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 3. 7 LIFE MILESTONES DEEP-DIVE CARDS
                item {
                    Text(
                        text = if (lang == "en") "Major Life Milestones Analysis" else "जीवनका ७ प्रमुख क्षेत्रहरूको सटिक विश्लेषण",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                items(eventCategories, key = { it.name }) { cat ->
                    val prediction = remember(cat, natalProfile, transitMap) {
                        LifeEventAnalyzerEngine.analyzeLifeEvent(cat, natalProfile, transitMap, birthAdYear, lang)
                    }

                    var isExpanded by remember { mutableStateOf(false) }

                    Surface(
                        onClick = { isExpanded = !isExpanded },
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            // Header Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (lang == "en") cat.titleEnglish else cat.titleNepali,
                                    fontWeight = FontWeight.ExtraBold,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Surface(
                                    color = Color(prediction.confidenceColorHex).copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = prediction.confidenceGrade,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = Color(prediction.confidenceColorHex),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Score Progress Bar
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                LinearProgressIndicator(
                                    progress = { prediction.promiseStrengthPercent / 100f },
                                    modifier = Modifier.weight(1f).height(8.dp),
                                    color = Color(prediction.confidenceColorHex),
                                    trackColor = MaterialTheme.colorScheme.surfaceContainerHigh
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "${prediction.promiseStrengthPercent}%",
                                    fontWeight = FontWeight.ExtraBold,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color(prediction.confidenceColorHex)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Favorable Timeline
                            Text(
                                text = "सक्रिय समय: ${prediction.favorableYearsText}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            if (isExpanded) {
                                Spacer(modifier = Modifier.height(8.dp))
                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = prediction.analysisText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 20.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Surface(
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = "मार्गदर्शन तथा उपाय:",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = prediction.guidanceText,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            lineHeight = 18.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
