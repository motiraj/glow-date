@file:Suppress("NewApi", "SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.ui.panchanga

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bs_addateconverteragecalculator.data.LocationData
import com.example.bs_addateconverteragecalculator.data.PanchangaCalculator
import com.example.bs_addateconverteragecalculator.data.TimingType
import com.example.bs_addateconverteragecalculator.data.VedicTimingCalculator
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoghadiyaHoraWidget(
    adDate: LocalDate,
    locationData: LocationData
) {
    val lang = com.example.bs_addateconverteragecalculator.utils.LocalAppLanguage.current
    val isNepali = com.example.bs_addateconverteragecalculator.utils.LocalIsNepali.current

    var selectedMainTab by remember { mutableIntStateOf(0) } // 0: Choghadiya, 1: Hora
    var isDayTab by remember { mutableStateOf(true) }

    var currentTimeMins by remember { mutableIntStateOf(getCurrentMins()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(60000)
            currentTimeMins = getCurrentMins()
        }
    }

    val (sunriseMins, sunsetMins) = PanchangaCalculator.getSunriseSunsetMinutes(adDate, locationData.lat, locationData.lon, locationData.tzOffset)

    val schedule = remember(adDate, selectedMainTab, locationData) {
        if (selectedMainTab == 0) {
            VedicTimingCalculator.getChoghadiyaSchedule(adDate.dayOfWeek, sunriseMins, sunsetMins)
        } else {
            VedicTimingCalculator.getHoraSchedule(adDate.dayOfWeek, sunriseMins, sunsetMins)
        }
    }

    val filteredSchedule = schedule.filter { it.isNight != isDayTab }
    val isToday = adDate == LocalDate.now()
    val normalizedCurrentTime = if (currentTimeMins < sunriseMins) currentTimeMins + 1440 else currentTimeMins

    var isExpanded by remember { mutableStateOf(false) }

    Card(
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, MaterialTheme.shapes.large)
            .clickable { isExpanded = !isExpanded }
    ) {
        Column(modifier = Modifier.padding(16.dp).animateContentSize()) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "वैदिक समय (Choghadiya & Hora)",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))
                // Main Tabs [Choghadiya | Hora]
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    SegmentedButton(
                        selected = selectedMainTab == 0,
                        onClick = { selectedMainTab = 0 },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                        icon = {},
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            activeContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            inactiveContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Text("चौघडिया (Choghadiya)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    SegmentedButton(
                        selected = selectedMainTab == 1,
                        onClick = { selectedMainTab = 1 },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                        icon = {},
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            activeContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            inactiveContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Text("होरा (Hora)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Sub Tabs [Day | Night]
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    FilterChip(
                        selected = isDayTab,
                        onClick = { isDayTab = true },
                        label = { Text("दिन (Day)", fontWeight = FontWeight.SemiBold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isDayTab,
                            borderColor = MaterialTheme.colorScheme.outlineVariant,
                            selectedBorderColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    FilterChip(
                        selected = !isDayTab,
                        onClick = { isDayTab = false },
                        label = { Text("रात (Night)", fontWeight = FontWeight.SemiBold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = !isDayTab,
                            borderColor = MaterialTheme.colorScheme.outlineVariant,
                            selectedBorderColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                // Active Currently Running Hero Banner (if applicable for today)
                val activeSlot = filteredSchedule.firstOrNull { slot ->
                    isToday && normalizedCurrentTime >= slot.startMins && normalizedCurrentTime < slot.endMins
                }

                val nowActiveBadgeText = when (lang) {
                    "en" -> "NOW ACTIVE"
                    "hi" -> "अभी सक्रिय"
                    else -> "अहिले चलिरहेको"
                }

                val nowBadgeText = when (lang) {
                    "en" -> "NOW"
                    "hi" -> "अभी"
                    else -> "अहिले"
                }

                if (activeSlot != null) {
                    val activeAccent = when (activeSlot.type) {
                        TimingType.AUSPICIOUS -> MaterialTheme.colorScheme.primary
                        TimingType.NEUTRAL -> MaterialTheme.colorScheme.secondary
                        TimingType.INAUSPICIOUS -> MaterialTheme.colorScheme.error
                    }

                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = nowActiveBadgeText,
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (selectedMainTab == 1) "होरा: ${activeSlot.lord}" else "चौघडिया: ${activeSlot.name}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }

                                Surface(
                                    color = activeAccent.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = activeSlot.chipText,
                                        color = activeAccent,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "स्वामी ग्रह: ${activeSlot.lord}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = activeSlot.formatTimeRange(isNepali, lang),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }

                // Chronologically Sorted Slots List
                val sortedSchedule = filteredSchedule.sortedBy { it.startMins }
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    sortedSchedule.forEach { slot ->
                        val isCurrent = isToday && normalizedCurrentTime >= slot.startMins && normalizedCurrentTime < slot.endMins

                        val accentColor = when (slot.type) {
                            TimingType.AUSPICIOUS -> MaterialTheme.colorScheme.primary
                            TimingType.NEUTRAL -> MaterialTheme.colorScheme.secondary
                            TimingType.INAUSPICIOUS -> MaterialTheme.colorScheme.error
                        }

                        val displayName = if (selectedMainTab == 1) "होरा (${slot.lord})" else "${slot.name} (${slot.lord})"

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = if (isCurrent) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f) else MaterialTheme.colorScheme.surfaceContainerLow,
                            border = if (isCurrent) BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
                            shadowElevation = if (isCurrent) 2.dp else 0.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Left Color Accent Bar
                                Box(
                                    modifier = Modifier
                                        .width(4.dp)
                                        .height(36.dp)
                                        .background(accentColor, RoundedCornerShape(2.dp))
                                )
                                Spacer(modifier = Modifier.width(12.dp))

                                // Slot Details Column
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = displayName,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.5.sp
                                        )
                                        if (isCurrent) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                color = MaterialTheme.colorScheme.primary,
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = nowBadgeText,
                                                    color = MaterialTheme.colorScheme.onPrimary,
                                                    fontSize = 8.5.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Surface(
                                        color = accentColor.copy(alpha = 0.12f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = slot.chipText,
                                            color = accentColor,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                                        )
                                    }
                                }

                                // Right-aligned Time Range
                                Text(
                                    text = slot.formatTimeRange(isNepali, lang),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getCurrentMins(): Int {
    val now = LocalTime.now()
    return now.hour * 60 + now.minute
}
