@file:Suppress("NewApi", "SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.ui.rashifal

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bs_addateconverteragecalculator.data.RashifalData
import com.example.bs_addateconverteragecalculator.data.ZodiacSign
import com.example.bs_addateconverteragecalculator.ui.components.RashifalIconGrid
import com.example.bs_addateconverteragecalculator.utils.AppStrings
import com.example.bs_addateconverteragecalculator.utils.LocalAppLanguage

@Composable
fun RashifalScreen(
    viewModel: RashifalViewModel = viewModel(),
    onNavigateToCheena: (() -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val lang = LocalAppLanguage.current

    var isKundaliOpen by remember { mutableStateOf(true) }
    var showBirthProfileSheet by remember { mutableStateOf(false) }
    var sheetProfileToEdit by remember { mutableStateOf<com.example.bs_addateconverteragecalculator.data.BirthProfile?>(uiState.birthProfile) }
    var showRashifalDialog by remember { mutableStateOf(false) }
    var selectedSignForDialog by remember { mutableStateOf<ZodiacSign?>(null) }
    var dialogPredictionType by remember { mutableStateOf(RashifalType.DAILY) }

    if (showBirthProfileSheet) {
        BirthDetailsSettingsSheet(
            initialProfile = sheetProfileToEdit,
            onDismiss = { showBirthProfileSheet = false },
            onSaveProfile = { profile ->
                viewModel.saveBirthProfile(profile)
                isKundaliOpen = true
            },
            onClearProfile = {
                sheetProfileToEdit?.let { viewModel.deleteProfile(it.id) }
            }
        )
    }

    if (showRashifalDialog && selectedSignForDialog != null) {
        val sign = selectedSignForDialog!!
        val signName = if (lang == "en") sign.nameEnglish else sign.nameNepali
        val signDesc = if (lang == "en") sign.descriptionEnglish else sign.descriptionNepali

        val dialogPrediction = viewModel.getPredictionForSign(sign.id)
        val dialogPredText = when (dialogPredictionType) {
            RashifalType.DAILY -> dialogPrediction.daily
            RashifalType.WEEKLY -> dialogPrediction.weekly
            RashifalType.MONTHLY -> dialogPrediction.monthly
            RashifalType.YEARLY -> dialogPrediction.yearly
        }

        AlertDialog(
            onDismissRequest = { showRashifalDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = sign.symbol, fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "$signName ${AppStrings.get("tab_rashifal", lang)}",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        val dialogTabs = listOf(
                            RashifalType.DAILY to "दैनिक",
                            RashifalType.WEEKLY to "साप्ताहिक",
                            RashifalType.MONTHLY to "मासिक",
                            RashifalType.YEARLY to "वार्षिक"
                        )
                        dialogTabs.forEachIndexed { index, (type, label) ->
                            val isSelected = dialogPredictionType == type
                            SegmentedButton(
                                selected = isSelected,
                                onClick = { dialogPredictionType = type },
                                shape = SegmentedButtonDefaults.itemShape(index = index, count = dialogTabs.size),
                                icon = {},
                                colors = SegmentedButtonDefaults.colors(
                                    activeContainerColor = MaterialTheme.colorScheme.primary,
                                    activeContentColor = MaterialTheme.colorScheme.onPrimary,
                                    activeBorderColor = MaterialTheme.colorScheme.primary,
                                    inactiveContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                                    inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    maxLines = 1,
                                    softWrap = false
                                )
                            }
                        }
                    }

                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = dialogPredText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            lineHeight = 22.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    if (signDesc.isNotEmpty()) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "राशि स्वभाव एवं विशेषता",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = signDesc,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    RashifalAttributeRow("नाम अक्षर", sign.dates)
                    RashifalAttributeRow("विशेषताहरू", sign.traits)
                    RashifalAttributeRow("स्वामी ग्रह", sign.lord)
                    RashifalAttributeRow("तत्व", sign.element)
                    RashifalAttributeRow("शुभ रङ्ग", dialogPrediction.luckyColor)
                    RashifalAttributeRow("शुभ अंक", dialogPrediction.luckyNumber)
                    RashifalAttributeRow("शुभ रत्न", dialogPrediction.gemstone)
                    RashifalAttributeRow("शुभ बार", dialogPrediction.luckyDays)
                    RashifalAttributeRow("शुभ महिना", dialogPrediction.luckyMonths)
                    RashifalAttributeRow("शुभ वर्ष / उमेर", dialogPrediction.luckyAges)
                    RashifalAttributeRow("क्षेत्र", dialogPrediction.profession)
                    RashifalAttributeRow("शुभ दिशा", dialogPrediction.direction)
                }
            },
            confirmButton = {
                TextButton(onClick = { showRashifalDialog = false }) {
                    Text(AppStrings.get("close", lang))
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 840.dp)
                .verticalScroll(scrollState)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Prediction Timeframe Selector (Daily / Weekly / Monthly / Yearly)
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                val timeframes = listOf(
                    RashifalType.DAILY to "दैनिक",
                    RashifalType.WEEKLY to "साप्ताहिक",
                    RashifalType.MONTHLY to "मासिक",
                    RashifalType.YEARLY to "वार्षिक"
                )
                timeframes.forEachIndexed { index, (type, label) ->
                    val isSelected = uiState.selectedType == type
                    SegmentedButton(
                        selected = isSelected,
                        onClick = { viewModel.selectType(type) },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = timeframes.size),
                        icon = {},
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = MaterialTheme.colorScheme.primary,
                            activeContentColor = MaterialTheme.colorScheme.onPrimary,
                            activeBorderColor = MaterialTheme.colorScheme.primary,
                            inactiveContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                            inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }

            // YOUR SELECTED RASHI CARD
            Card(
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectedSignForDialog = uiState.selectedSign
                        dialogPredictionType = uiState.selectedType
                        showRashifalDialog = true
                    }
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Rashifal",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        val selectedSignName = if (lang == "en") uiState.selectedSign.nameEnglish else uiState.selectedSign.nameNepali
                        Text(
                            text = "${uiState.selectedSign.symbol} $selectedSignName",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                        if (uiState.isPersonalized && uiState.natalProfile?.janmaRashiNameNepali == uiState.selectedSign.nameNepali) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "तपाईँको राशि",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "View Details",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    val currentPredictionText = when (uiState.selectedType) {
                        RashifalType.DAILY -> uiState.prediction.daily
                        RashifalType.WEEKLY -> uiState.prediction.weekly
                        RashifalType.MONTHLY -> uiState.prediction.monthly
                        RashifalType.YEARLY -> uiState.prediction.yearly
                    }
                    Text(
                        text = currentPredictionText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        lineHeight = 22.sp
                    )
                }
            }

            // 12 Zodiac Signs Grid Component
            RashifalIconGrid(
                signs = RashifalData.signs,
                selectedSignId = uiState.selectedSign.id,
                onSignSelected = { sign ->
                    viewModel.selectSign(sign)
                    selectedSignForDialog = sign
                    dialogPredictionType = uiState.selectedType
                    showRashifalDialog = true
                },
                lang = lang,
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outlineVariant)

            // SECOND BLOCK: PERSONAL KUNDALI & MICRO RASHIFAL SECTION
            if (!uiState.isPersonalized || uiState.natalProfile == null) {
                // MODE A: ENTRY BANNER (Shown ONLY when NO profile is created yet)
                Card(
                    onClick = {
                        sheetProfileToEdit = null
                        showBirthProfileSheet = true
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                    modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = "Add Profile",
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "व्यक्तिगत कुण्डली र सूक्ष्म राशिफल भर्नुहोस्",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "आफ्नो जन्म मिति, समय र स्थान राखी ताराबल, चन्द्रबल र गोचर अनुसारको भविष्यवाणी हेर्नुहोस्।",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.9f)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        FilledTonalButton(
                            onClick = {
                                sheetProfileToEdit = null
                                showBirthProfileSheet = true
                            },
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text("भर्नुहोस्", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            } else {
                // MODE B: PERSONALIZED KUNDALI & HERO CARD (Shown when profile IS CREATED)
                var isSavedProfilesExpanded by remember { mutableStateOf(false) }

                Text(
                    text = "व्यक्तिगत कुण्डली तथा सूक्ष्म गोचर राशिफल (Personal Kundali & Micro Rashifal)",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                // Saved Profiles Expandable Card
                if (uiState.savedProfiles.isNotEmpty()) {
                    Card(
                        onClick = { isSavedProfilesExpanded = !isSavedProfilesExpanded },
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                                .animateContentSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "सुरक्षित कुण्डलीहरू (${uiState.savedProfiles.size})",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = if (isSavedProfilesExpanded) "सुरक्षित कुण्डली सूची लुकाउनुहोस् (Tap to hide)" else "सुरक्षित कुण्डली सूची देखाउनुहोस् (Tap to show)",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Icon(
                                    imageVector = if (isSavedProfilesExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Toggle Saved Profiles",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            if (isSavedProfilesExpanded) {
                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                                ) {
                                    items(uiState.savedProfiles, key = { it.id }) { p ->
                                        val isSelected = p.id == uiState.birthProfile?.id
                                        val pName = p.name.ifEmpty { "जन्म कुण्डली" }
                                        FilterChip(
                                            selected = isSelected,
                                            onClick = {
                                                viewModel.selectActiveProfile(p.id)
                                                isKundaliOpen = true
                                            },
                                            label = { Text("👤 $pName", fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                                labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                            ),
                                            border = FilterChipDefaults.filterChipBorder(
                                                enabled = true,
                                                selected = isSelected,
                                                borderColor = MaterialTheme.colorScheme.outlineVariant,
                                                selectedBorderColor = MaterialTheme.colorScheme.primary
                                            )
                                        )
                                    }

                                    item {
                                        AssistChip(
                                            onClick = {
                                                sheetProfileToEdit = null
                                                showBirthProfileSheet = true
                                            },
                                            label = { Text("➕ नयाँ थप्नुहोस्", fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                                            colors = AssistChipDefaults.assistChipColors(
                                                containerColor = MaterialTheme.colorScheme.surfaceContainer
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // MODE B: Personalized Hero Card
                val natal = uiState.natalProfile!!
                val transit = uiState.transitAnalysis
                val pred = uiState.personalizedPrediction
                val userName = uiState.birthProfile?.name?.ifEmpty { "तपाईँ" } ?: "तपाईँ"
                val tfName = when (uiState.selectedType) {
                    RashifalType.DAILY -> "दैनिक"
                    RashifalType.WEEKLY -> "साप्ताहिक"
                    RashifalType.MONTHLY -> "मासिक"
                    RashifalType.YEARLY -> "वार्षिक"
                }

                if (isKundaliOpen) {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(20.dp))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Card Top Header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f, fill = false)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "$userName को $tfName व्यक्तिगत कुण्डली राशिफल",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = {
                                            sheetProfileToEdit = uiState.birthProfile
                                            showBirthProfileSheet = true
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit Birth Profile",
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                    IconButton(
                                        onClick = { isKundaliOpen = false },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Close Kundali",
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                }
                            }

                        // Calculated Astrological Profile Badges Grid
                        Surface(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("जन्म राशि: ${natal.janmaRashiNameNepali}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                    Text("लग्न: ${natal.lagnaNameNepali}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("नक्षत्र: ${natal.nakshatraNameNepali} (पाउ ${natal.pada})", style = MaterialTheme.typography.bodySmall)
                                    Text("नामाक्षर: '${natal.naamAkshar}'", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                                }
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("नाडी: ${natal.nadi}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("गण: ${natal.gana}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("इष्टकाल: ${natal.ishtakalaText}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                    Text("भयात: ${natal.bhayatText} • भभोग: ${natal.bhabhogText}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                if (natal.bhogyaDashaText.isNotEmpty()) {
                                    Text("जन्मकालीन भोग्य दशा: ${natal.bhogyaDashaText}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.secondary)
                                }
                            }
                        }

                        // Visual Kundali Chart (Lagna Chart & Moon Chart)
                        var isMoonChart by remember { mutableStateOf(false) }
                        val natalSnapshot = remember(
                            uiState.birthProfile?.id,
                            uiState.birthProfile?.adBirthDate,
                            uiState.birthProfile?.birthHour,
                            uiState.birthProfile?.birthMinute,
                            uiState.birthProfile?.latitude,
                            uiState.birthProfile?.longitude,
                            uiState.birthProfile?.timezoneOffset
                        ) {
                            val profile = uiState.birthProfile
                            val birthAd = try { java.time.LocalDate.parse(profile?.adBirthDate ?: "2001-04-14") } catch (_: Exception) { java.time.LocalDate.of(2001, 4, 14) }
                            com.example.bs_addateconverteragecalculator.data.OfflineVedicEphemeris.getPlanetarySnapshot(
                                year = birthAd.year,
                                month = birthAd.monthValue,
                                day = birthAd.dayOfMonth,
                                hour = profile?.birthHour ?: 7,
                                minute = profile?.birthMinute ?: 30,
                                tzOffsetHours = profile?.timezoneOffset ?: 5.75
                            )
                        }

                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isMoonChart) "चन्द्र कुण्डली चक्र" else "जन्म लग्न कुण्डली चक्र",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                FilterChip(
                                    selected = isMoonChart,
                                    onClick = { isMoonChart = !isMoonChart },
                                    label = { Text(if (isMoonChart) "लग्न चक्र" else "चन्द्र चक्र", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                                )
                            }

                            com.example.bs_addateconverteragecalculator.ui.components.KundaliChart(
                                lagnaRashiIndex = natal.lagnaIndex + 1,
                                lagnaRashiName = natal.lagnaNameNepali,
                                snapshot = natalSnapshot,
                                isMoonChart = isMoonChart,
                                modifier = Modifier.fillMaxWidth()
                            )

                            if (onNavigateToCheena != null) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = onNavigateToCheena,
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "📜 विस्तृत चिना पत्रिका हेर्नुहोस् (Full Cheena)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.5.sp
                                    )
                                }
                            }
                        }

                        // Chandrashtama Alert Banner (if active in daily view)
                        if (uiState.selectedType == RashifalType.DAILY && transit?.isChandrashtama == true) {
                            Surface(
                                color = MaterialTheme.colorScheme.errorContainer,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = "Alert",
                                        tint = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "⚠️ आज चन्द्राष्टम! चन्द्रमा ८ औं भावमा रहेकाले यात्रा र हतारका निर्णयहरूमा सावधानी अपनाउनुहोला।",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }
                        }

                        // Transit Status Badges (Tara Bala, Chandra Bala & Dashas)
                        if (transit != null) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    modifier = Modifier.weight(1f),
                                    color = if (transit.taraBalaStatus.contains("शुभ")) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("ताराबल (Tara Bala)", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp)
                                        Text(transit.taraBalaName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                                    }
                                }

                                Surface(
                                    modifier = Modifier.weight(1f),
                                    color = if (transit.chandraBalaStatus.contains("शुभ")) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("चन्द्रबल (Chandra Bala)", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp)
                                        Text(transit.chandraBalaStatus, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }

                            // Active Dasha Bar
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "विंशोत्तरी महादशा: ${transit.activeMahadashaLord} | अन्तरदशा: ${transit.activeAntardashaLord}\nयोगिनी दशा: ${transit.activeYoginiDasha}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(8.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        // Timeframe Selector Tabs for Kundali Predictions
                        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                            val timeframes = listOf(
                                RashifalType.DAILY to "दैनिक",
                                RashifalType.WEEKLY to "साप्ताहिक",
                                RashifalType.MONTHLY to "मासिक",
                                RashifalType.YEARLY to "वार्षिक"
                            )
                            timeframes.forEachIndexed { index, (type, label) ->
                                val isSelected = uiState.selectedType == type
                                SegmentedButton(
                                    selected = isSelected,
                                    onClick = { viewModel.selectType(type) },
                                    shape = SegmentedButtonDefaults.itemShape(index = index, count = timeframes.size),
                                    icon = {},
                                    colors = SegmentedButtonDefaults.colors(
                                        activeContainerColor = MaterialTheme.colorScheme.primary,
                                        activeContentColor = MaterialTheme.colorScheme.onPrimary,
                                        activeBorderColor = MaterialTheme.colorScheme.primary,
                                        inactiveContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                                        inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 11.5.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        maxLines = 1,
                                        softWrap = false
                                    )
                                }
                            }
                        }

                        // Multi-Timeframe Output (Daily / Weekly / Monthly / Yearly)
                        val tfOut = uiState.multiTimeframeOutput
                        if (tfOut != null) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                            // Header Badge & Score Gauge
                            Surface(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth().wrapContentHeight()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = tfOut.headlineBadge,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        modifier = Modifier.weight(1f, fill = false)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "शुभता: ${tfOut.overallScorePercent}%",
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 11.sp,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            // 1. General Summary Sub-Card
                            OutlinedCard(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)),
                                modifier = Modifier.fillMaxWidth().wrapContentHeight()
                            ) {
                                Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "मुख्य सारांश ($tfName):",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleSmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Text(
                                        text = tfOut.generalSummary,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 19.sp
                                    )
                                }
                            }

                            // 2. Career & Finance Sub-Card
                            OutlinedCard(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)),
                                modifier = Modifier.fillMaxWidth().wrapContentHeight()
                            ) {
                                Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Outlined.Work,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "कार्यक्षेत्र तथा आर्थिक (Career & Finance):",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleSmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Text(
                                        text = tfOut.careerAndFinance,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 19.sp
                                    )
                                }
                            }

                            // 3. Health & Family Sub-Card
                            OutlinedCard(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)),
                                modifier = Modifier.fillMaxWidth().wrapContentHeight()
                            ) {
                                Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Outlined.Favorite,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "स्वास्थ्य तथा परिवार (Health & Family):",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleSmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Text(
                                        text = tfOut.healthAndFamily,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 19.sp
                                    )
                                }
                            }

                            // 4. Dasha & Remedy Sub-Card
                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth().wrapContentHeight()
                            ) {
                                Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Outlined.Psychology,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "दशा प्रभाव:",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleSmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Text(
                                        text = tfOut.dashaInfluenceNote,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        lineHeight = 18.sp
                                    )

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Outlined.Lightbulb,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "विशेष उपाय तथा सुझाव:",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleSmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Text(
                                        text = tfOut.remedyAndLuckyTip,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        lineHeight = 19.sp
                                    )
                                }
                            }

                            // 5. Life Events & Rajayoga Analysis Card (Moved right under Gochar description)
                            var showLifeEventsSheet by remember { mutableStateOf(false) }

                            Card(
                                onClick = { showLifeEventsSheet = true },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Timeline,
                                            contentDescription = "Life Events Guide",
                                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                                            modifier = Modifier.size(22.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column {
                                            Text(
                                                text = "जीवनका प्रमुख घटनाहरू र राजयोग विश्लेषण",
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = MaterialTheme.colorScheme.onTertiaryContainer
                                            )
                                            Text(
                                                text = "विवाह, करियर, धन, सन्तान, घर-जग्गा, विदेश र स्वास्थ्य (Tap for details)",
                                                style = MaterialTheme.typography.bodySmall,
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.85f)
                                            )
                                        }
                                    }
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "Open",
                                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            if (showLifeEventsSheet) {
                                val activeProfile = uiState.birthProfile
                                val birthAd = try { java.time.LocalDate.parse(activeProfile?.adBirthDate ?: "2001-04-14") } catch (_: Exception) { java.time.LocalDate.of(2001, 4, 14) }
                                val transitMap = mapOf(
                                    "sun" to natalSnapshot.sunRashi, "moon" to natalSnapshot.moonRashi, "mars" to natalSnapshot.marsRashi,
                                    "mercury" to natalSnapshot.mercuryRashi, "jupiter" to natalSnapshot.jupiterRashi, "venus" to natalSnapshot.venusRashi,
                                    "saturn" to natalSnapshot.saturnRashi, "rahu" to natalSnapshot.rahuRashi, "ketu" to natalSnapshot.ketuRashi
                                )

                                com.example.bs_addateconverteragecalculator.ui.panchanga.LifeEventsSheet(
                                    natalProfile = natal,
                                    transitMap = transitMap,
                                    birthAdYear = birthAd.year,
                                    onDismiss = { showLifeEventsSheet = false }
                                )
                            }
                        } else if (pred != null) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("स्वास्थ्य (Health):", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                                Text(pred.health, style = MaterialTheme.typography.bodyMedium, lineHeight = 20.sp)

                                Text("कार्यक्षेत्र (Career):", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                                Text(pred.career, style = MaterialTheme.typography.bodyMedium, lineHeight = 20.sp)

                                Text("आर्थिक स्थिति (Finance):", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                                Text(pred.finance, style = MaterialTheme.typography.bodyMedium, lineHeight = 20.sp)

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("शुभ रङ: ${pred.luckyColor}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                                    Text("शुभ अङ्क: ${pred.luckyNumber}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                                }

                                Surface(
                                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = pred.dailyAdvice,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 11.5.sp,
                                        lineHeight = 18.sp,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }

                        // Bottom Close Kundali Action Button
                        HorizontalDivider(
                            modifier = Modifier.padding(top = 8.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )

                        OutlinedButton(
                            onClick = { isKundaliOpen = false },
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.4f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "कुण्डली बन्द गर्नुहोस् (Close Kundali)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp
                            )
                        }
                    }
                }
            } else {
                // Compact Card shown when Kundali is closed
                Card(
                    onClick = { isKundaliOpen = true },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "$userName को व्यक्तिगत कुण्डली",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "जन्म राशि: ${natal.janmaRashiNameNepali} • लग्न: ${natal.lagnaNameNepali}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 11.5.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        FilledTonalButton(
                            onClick = { isKundaliOpen = true },
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("हेर्नुहोस्", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun RashifalAttributeRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            modifier = Modifier.widthIn(max = 200.dp)
        )
    }
}

