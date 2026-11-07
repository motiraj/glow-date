package com.example.bs_addateconverteragecalculator.ui.calendar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bs_addateconverteragecalculator.converter.DateConverterEngine
import com.example.bs_addateconverteragecalculator.data.HolidayData
import com.example.bs_addateconverteragecalculator.data.MuhuratData
import com.example.bs_addateconverteragecalculator.data.NepaliDate
import com.example.bs_addateconverteragecalculator.data.NepaliMonths
import com.example.bs_addateconverteragecalculator.data.NepaliNumerals
import com.example.bs_addateconverteragecalculator.data.TithiCalculator
import com.example.bs_addateconverteragecalculator.utils.LocalIsNepali
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isNepali = LocalIsNepali.current

    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                viewModel.checkAndRefreshIfDateChanged()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    var showMonthYearPicker by remember { mutableStateOf(false) }
    var showMuhuratDialog by remember { mutableStateOf(false) }
    var showDayDetailsDialog by remember { mutableStateOf(false) }

    if (showMonthYearPicker) {
        MonthYearPickerDialog(
            initialYear = uiState.currentBsYear,
            initialMonth = uiState.currentBsMonth,
            onDismiss = { showMonthYearPicker = false },
            onDateSelected = { year, month ->
                viewModel.updateMonth(year, month)
                showMonthYearPicker = false
            }
        )
    }

    if (showMuhuratDialog) {
        MuhuratDialog(
            year = uiState.currentBsYear,
            onDismiss = { showMuhuratDialog = false }
        )
    }

    if (showDayDetailsDialog) {
        DayDetailsDialog(
            uiState = uiState,
            onDismiss = { showDayDetailsDialog = false }
        )
    }

    // Dynamic AD month & year range calculation (e.g., Oct/Nov, 2026)
    val adMonthRangeText = remember(uiState.currentBsYear, uiState.currentBsMonth, uiState.daysInMonth) {
        try {
            val firstAd = DateConverterEngine.convertBsToAd(NepaliDate(uiState.currentBsYear, uiState.currentBsMonth, 1))
            val lastAd = DateConverterEngine.convertBsToAd(NepaliDate(uiState.currentBsYear, uiState.currentBsMonth, uiState.daysInMonth))
            
            val fMonth = firstAd.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
            val lMonth = lastAd.month.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
            
            if (firstAd.year == lastAd.year) {
                "$fMonth/$lMonth, ${firstAd.year}"
            } else {
                "$fMonth ${firstAd.year} / $lMonth ${lastAd.year}"
            }
        } catch (_: Exception) {
            ""
        }
    }

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 840.dp)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        item {
            Text(
                text = "नेपाली पात्रो (Nepali Calendar)",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(bottom = 20.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Calendar Month Card Container
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 2.dp, shape = MaterialTheme.shapes.extraLarge),
                shape = MaterialTheme.shapes.extraLarge,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Month Selector Header with 'Today' Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            var newMonth = uiState.currentBsMonth - 1
                            var newYear = uiState.currentBsYear
                            if (newMonth < 1) {
                                newMonth = 12
                                newYear--
                            }
                            viewModel.updateMonth(newYear, newMonth)
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "अघिल्लो महिना", tint = MaterialTheme.colorScheme.primary)
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = MaterialTheme.shapes.medium,
                                modifier = Modifier
                                    .clip(MaterialTheme.shapes.medium)
                                    .clickable { showMonthYearPicker = true }
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "${NepaliMonths.getMonthName(uiState.currentBsMonth, isNepali)} ${NepaliNumerals.formatDigits(uiState.currentBsYear, isNepali)} ▼",
                                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp),
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        maxLines = 1
                                    )
                                    if (adMonthRangeText.isNotEmpty()) {
                                        Text(
                                            text = adMonthRangeText,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            FilledTonalButton(
                                onClick = {
                                    try {
                                        val todayBs = DateConverterEngine.convertAdToBs(LocalDate.now())
                                        viewModel.updateMonth(todayBs.year, todayBs.month, todayBs.day)
                                    } catch (_: Exception) {}
                                },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Icon(Icons.Default.Today, contentDescription = "Today", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("आज", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }

                        IconButton(onClick = {
                            var newMonth = uiState.currentBsMonth + 1
                            var newYear = uiState.currentBsYear
                            if (newMonth > 12) {
                                newMonth = 1
                                newYear++
                            }
                            viewModel.updateMonth(newYear, newMonth)
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "अर्को महिना", tint = MaterialTheme.colorScheme.primary)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Days Grid Header (Nepali Weekdays Short - Saturday 'श' in Red)
                    val weekDays = remember { listOf(
                        Pair("आइत", false), Pair("सोम", false), Pair("मङ्गल", false), Pair("बुध", false),
                        Pair("बिही", false), Pair("शुक्र", false), Pair("शनि", true)
                    ) }
                    
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            weekDays.forEach { (day, isSat) ->
                                val semanticColors = com.example.bs_addateconverteragecalculator.ui.theme.LocalSemanticColors.current
                                Text(
                                    text = day,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.width(40.dp),
                                    textAlign = TextAlign.Center,
                                    color = if (isSat) semanticColors.holiday else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Highly Optimized Manual Grid Rendering for Instant Touch Response
                    val offset = uiState.firstDayOfWeekOffset
                    val totalSlots = offset + uiState.daysInMonth
                    val rowCount = (totalSlots + 6) / 7

                    val todayBs = remember {
                        try {
                            DateConverterEngine.convertAdToBs(LocalDate.now())
                        } catch (_: Exception) {
                            null
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        for (rowIndex in 0 until rowCount) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                for (colIndex in 0..6) {
                                    val index = rowIndex * 7 + colIndex
                                    if (index < offset || index >= totalSlots) {
                                        Box(modifier = Modifier.weight(1f).aspectRatio(0.67f))
                                    } else {
                                        val dayNum = index - offset + 1
                                        val dayInfo = uiState.daysInfo.find { it.bsDay == dayNum }
                                        
                                        if (dayInfo == null) {
                                            Box(modifier = Modifier.weight(1f).aspectRatio(0.67f))
                                            continue
                                        }

                                        val isSelected = uiState.selectedDay == dayNum
                                        val isToday = todayBs?.let {
                                            it.year == uiState.currentBsYear && it.month == uiState.currentBsMonth && it.day == dayNum
                                        } ?: false
                                        val isSpecialTithi = dayInfo.tithi.contains("एकादशी") ||
                                                dayInfo.tithi.contains("पूर्णिमा") ||
                                                dayInfo.tithi.contains("औंसी")

                                        val semanticColors = com.example.bs_addateconverteragecalculator.ui.theme.LocalSemanticColors.current

                                        Surface(
                                            shape = MaterialTheme.shapes.small,
                                            color = when {
                                                isSelected -> MaterialTheme.colorScheme.primary
                                                isToday -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                                                dayInfo.isHoliday -> semanticColors.holiday.copy(alpha = 0.12f)
                                                dayInfo.isSaturday -> semanticColors.holiday.copy(alpha = 0.06f)
                                                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
                                            },
                                            border = when {
                                                isSelected -> null
                                                isToday -> BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
                                                dayInfo.isHoliday -> BorderStroke(0.5.dp, semanticColors.holiday.copy(alpha = 0.35f))
                                                else -> BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                                            },
                                            modifier = Modifier
                                                .weight(1f)
                                                .aspectRatio(0.67f)
                                                .clip(MaterialTheme.shapes.small)
                                                .clickable(
                                                    onClickLabel = "${NepaliNumerals.formatDigits(dayNum, isNepali)} गते, ${if (dayInfo.isHoliday) "सार्वजनिक बिदा" else ""}"
                                                ) { 
                                                    viewModel.selectDay(dayNum)
                                                    showDayDetailsDialog = true
                                                }
                                        ) {
                                            Box(modifier = Modifier.fillMaxSize()) {
                                                Column(
                                                    modifier = Modifier
                                                        .fillMaxSize()
                                                        .padding(horizontal = 1.dp, vertical = 2.dp),
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.Center
                                                ) {
                                                    // Nepali Day Number
                                                    Text(
                                                        text = NepaliNumerals.formatDigits(dayNum, isNepali),
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 15.sp,
                                                        lineHeight = 16.sp,
                                                        color = when {
                                                            isSelected -> MaterialTheme.colorScheme.onPrimary
                                                            dayInfo.isSaturday || dayInfo.isHoliday -> semanticColors.holiday
                                                            else -> MaterialTheme.colorScheme.onSurface
                                                        }
                                                    )
                                                    
                                                    // English Date
                                                    if (dayInfo.adDay.isNotEmpty()) {
                                                        Text(
                                                            text = dayInfo.adDay,
                                                            fontSize = 9.sp,
                                                            lineHeight = 10.sp,
                                                            fontWeight = FontWeight.Medium,
                                                            color = when {
                                                                isSelected -> MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                                                                dayInfo.isSaturday || dayInfo.isHoliday -> semanticColors.holiday.copy(alpha = 0.85f)
                                                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                                                            }
                                                        )
                                                    }

                                                    Spacer(modifier = Modifier.height(2.dp))

                                                    // Tithi in visible text
                                                    if (dayInfo.tithi.isNotEmpty()) {
                                                        Text(
                                                            text = dayInfo.tithi,
                                                            fontSize = 8.sp,
                                                            lineHeight = 9.5.sp,
                                                            letterSpacing = (-0.2).sp,
                                                            fontWeight = if (isSpecialTithi || isSelected) FontWeight.Bold else FontWeight.Medium,
                                                            maxLines = 1,
                                                            softWrap = false,
                                                            overflow = TextOverflow.Ellipsis,
                                                            textAlign = TextAlign.Center,
                                                            color = when {
                                                                isSelected -> MaterialTheme.colorScheme.onPrimary
                                                                dayInfo.isSaturday || dayInfo.isHoliday -> semanticColors.holiday
                                                                isSpecialTithi -> MaterialTheme.colorScheme.primary
                                                                else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.9f)
                                                            }
                                                        )
                                                    }
                                                }
                                                
                                                // Holiday top-right dot indicator
                                                if (dayInfo.isHoliday) {
                                                    Box(
                                                        modifier = Modifier
                                                            .padding(3.dp)
                                                            .size(5.dp)
                                                            .clip(CircleShape)
                                                            .background(if (isSelected) MaterialTheme.colorScheme.onPrimary else semanticColors.holiday)
                                                            .align(Alignment.TopEnd)
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
        }

        // Muhurat Card Action
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Card(
                onClick = { showMuhuratDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(2.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.85f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("✨", fontSize = 20.sp)
                        }
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${NepaliNumerals.formatDigits(uiState.currentBsYear, isNepali)} सालका शुभ साइत / मुहूर्तहरू",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "विवाह, व्रतबन्ध, पास्नी, गृहप्रवेश आदि लगनहरू",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = "हेर्नुहोस्",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(22.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(4.dp, 20.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(MaterialTheme.colorScheme.primary)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${NepaliMonths.getMonthName(uiState.currentBsMonth, isNepali)} महिनाका बिदा तथा पर्वहरू",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                if (uiState.holidaysInMonth.isNotEmpty()) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "${NepaliNumerals.formatDigits(uiState.holidaysInMonth.size, isNepali)} पर्व",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        if (uiState.holidaysInMonth.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "यस महिनामा कुनै सार्वजनिक बिदा छैन।",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(20.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            items(uiState.holidaysInMonth, key = { "${it.bsYear ?: 0}_${it.bsMonth}_${it.bsDay}_${it.titleNepali}" }) { holiday ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .shadow(2.dp, RoundedCornerShape(16.dp))
                        .clickable {
                            viewModel.selectDay(holiday.bsDay)
                            showDayDetailsDialog = true
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.width(52.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 2.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = NepaliNumerals.formatDigits(holiday.bsDay, isNepali),
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 17.sp,
                                    lineHeight = 19.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "गते",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 12.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = holiday.titleNepali,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (holiday.title.isNotEmpty() && holiday.title != holiday.titleNepali) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = holiday.title,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayDetailsDialog(
    uiState: CalendarUiState,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "वि.सं.: ${uiState.selectedDayBsText}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "इ.सं.: ${uiState.selectedDayAdText}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (uiState.selectedDayNsText.isNotEmpty()) {
                        Text(
                            text = uiState.selectedDayNsText,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = uiState.selectedDayDayText,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // Holiday / Special Day Banner
            if (uiState.selectedDayHoliday != null || uiState.sorhaShraddhaInfo != null) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (uiState.selectedDayHoliday != null) {
                            Text(
                                text = uiState.selectedDayHoliday.titleNepali,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                        if (uiState.sorhaShraddhaInfo != null) {
                            if (uiState.selectedDayHoliday != null) Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = uiState.sorhaShraddhaInfo,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }

            // Panchanga Details
            Column {
                Text(
                    text = "☀️ पञ्चाङ्ग विवरण",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("पक्ष: ${uiState.paksha}", fontWeight = FontWeight.Medium)
                        Text("तिथि: ${uiState.tithiFullDetails}", fontWeight = FontWeight.Medium)
                        Text("नक्षत्र: ${uiState.nakshatraFullDetails}", fontWeight = FontWeight.Medium)
                        Text("योग: ${uiState.yogaFullDetails}", fontWeight = FontWeight.Medium)
                        Text("करण: ${uiState.karanaFullDetails}", fontWeight = FontWeight.Medium)
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        Text("🌅 सूर्योदय: ${uiState.panchangaSunriseText} | 🌇 सूर्यास्त: ${uiState.panchangaSunsetText}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            // Close Action Button
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("बन्द गर्नुहोस् (Close)", fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun MuhuratDialog(
    year: Int,
    onDismiss: () -> Unit
) {
    val isNepali = LocalIsNepali.current
    val muhurats = MuhuratData.getMuhuratsForYear(year)
    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("✨", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${NepaliNumerals.formatDigits(year, isNepali)} सालका शुभ साइत तथा मुहूर्तहरू",
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 17.sp)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                muhurats.forEach { muhurat ->
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = muhurat.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        muhurat.datesNepali.forEach { dateStr ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = dateStr,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(12.dp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("बन्द गर्नुहोस्", fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = MaterialTheme.colorScheme.surface
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthYearPickerDialog(
    initialYear: Int,
    initialMonth: Int,
    onDismiss: () -> Unit,
    onDateSelected: (Int, Int) -> Unit
) {
    val isNepali = LocalIsNepali.current
    var selectedYear by remember { mutableStateOf(initialYear) }
    var selectedMonth by remember { mutableStateOf(initialMonth) }
    
    var yearExpanded by remember { mutableStateOf(false) }
    var monthExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("महिना र वर्ष छान्नुहोस्", fontWeight = FontWeight.ExtraBold) },
        text = {
            Column {
                ExposedDropdownMenuBox(
                    expanded = yearExpanded,
                    onExpandedChange = { yearExpanded = !yearExpanded },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    OutlinedTextField(
                        value = "${NepaliNumerals.formatDigits(selectedYear, isNepali)} साल",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("वर्ष") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = yearExpanded) },
                        modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, enabled = true).fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = yearExpanded,
                        onDismissRequest = { yearExpanded = false }
                    ) {
                        (2000..2100).forEach { y ->
                            DropdownMenuItem(
                                text = { Text("${NepaliNumerals.formatDigits(y, isNepali)} साल", fontWeight = FontWeight.Medium) },
                                onClick = {
                                    selectedYear = y
                                    yearExpanded = false
                                }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = monthExpanded,
                    onExpandedChange = { monthExpanded = !monthExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = NepaliMonths.getMonthName(selectedMonth, isNepali),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("महिना") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = monthExpanded) },
                        modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, enabled = true).fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = monthExpanded,
                        onDismissRequest = { monthExpanded = false }
                    ) {
                        (1..12).forEach { m ->
                            DropdownMenuItem(
                                text = { Text(NepaliMonths.getMonthName(m, isNepali), fontWeight = FontWeight.Medium) },
                                onClick = {
                                    selectedMonth = m
                                    monthExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onDateSelected(selectedYear, selectedMonth) },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("खोज्नुहोस्", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("रद्द", fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}
