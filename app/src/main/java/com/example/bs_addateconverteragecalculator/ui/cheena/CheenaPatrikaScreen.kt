@file:Suppress("NewApi", "SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.ui.cheena

import android.content.Context
import android.content.Intent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bs_addateconverteragecalculator.data.*
import com.example.bs_addateconverteragecalculator.ui.components.KundaliChart
import com.example.bs_addateconverteragecalculator.ui.components.KundaliChartType
import com.example.bs_addateconverteragecalculator.ui.rashifal.BirthDetailsSettingsSheet
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheenaPatrikaScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val preferencesManager = remember { PreferencesManager(context) }

    val activeProfile by preferencesManager.birthProfileFlow.collectAsState(initial = null)
    val savedProfiles by preferencesManager.savedBirthProfilesFlow.collectAsState(initial = emptyList())

    var showEditSheet by remember { mutableStateOf(false) }
    var profileToEdit by remember { mutableStateOf<BirthProfile?>(null) }
    var selectedSectionTab by remember { mutableIntStateOf(0) } // 0=चक्र, 1=पञ्चाङ्ग, 2=ग्रह स्पष्ट, 3=दशा, 4=शुभ तत्व

    if (showEditSheet) {
        BirthDetailsSettingsSheet(
            initialProfile = profileToEdit,
            onDismiss = { showEditSheet = false },
            onSaveProfile = { profile ->
                coroutineScope.launch {
                    preferencesManager.saveBirthProfile(profile)
                    showEditSheet = false
                }
            },
            onClearProfile = {
                profileToEdit?.let {
                    coroutineScope.launch {
                        preferencesManager.deleteBirthProfile(it.id)
                        showEditSheet = false
                    }
                }
            }
        )
    }

    val cheenaData = remember(activeProfile) {
        activeProfile?.let { CheenaCalculatorEngine.calculateCheenaPatrika(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "चिना पत्रिका (जन्म कुण्डली)",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (cheenaData != null) {
                            Text(
                                text = "${cheenaData.name} — ${cheenaData.janmaRashiName} राशि, ${cheenaData.lagnaName} लग्न",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (cheenaData != null) {
                        IconButton(
                            onClick = {
                                val shareText = CheenaCalculatorEngine.generateCheenaShareText(cheenaData)
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, shareText)
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, "चिना पत्रिका सेयर गर्नुहोस्")
                                context.startActivity(shareIntent)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share Cheena",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        IconButton(
                            onClick = {
                                profileToEdit = activeProfile
                                showEditSheet = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            if (activeProfile == null || cheenaData == null) {
                // Empty State: Prompt user to create profile
                EmptyCheenaState(
                    onAddProfile = {
                        profileToEdit = null
                        showEditSheet = true
                    }
                )
            } else {
                // Cheena Patrika Content
                CheenaPatrikaContent(
                    data = cheenaData,
                    activeProfile = activeProfile!!,
                    savedProfiles = savedProfiles,
                    selectedSection = selectedSectionTab,
                    onSectionSelect = { selectedSectionTab = it },
                    onSelectProfile = { id ->
                        coroutineScope.launch {
                            preferencesManager.selectActiveBirthProfile(id)
                        }
                    },
                    onAddNewProfile = {
                        profileToEdit = null
                        showEditSheet = true
                    }
                )
            }
        }
    }
}

@Composable
private fun EmptyCheenaState(
    onAddProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(90.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(46.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "आफ्नो चिना पत्रिका बनाउनुहोस्",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "आफ्नो वा परिवारको जन्म मिति (वि.सं./इ.सं.), समय र स्थान भरेर विस्तृत लग्न कुण्डली, नवमांश कुण्डली, पञ्चाङ्ग साधन र १२० वर्षे विंशोत्तरी महादशा तयार पार्नुहोस्।",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onAddProfile,
            shape = RoundedCornerShape(14.dp),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp)
        ) {
            Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("जन्म विवरण भर्नुहोस्", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
private fun CheenaPatrikaContent(
    data: CheenaPatrikaData,
    activeProfile: BirthProfile,
    savedProfiles: List<BirthProfile>,
    selectedSection: Int,
    onSectionSelect: (Int) -> Unit,
    onSelectProfile: (String) -> Unit,
    onAddNewProfile: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .widthIn(max = 840.dp)
            .verticalScroll(scrollState)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Saved Profiles Selector Row (if multiple exist)
        if (savedProfiles.size > 1) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(savedProfiles, key = { it.id }) { p ->
                    val isSelected = p.id == activeProfile.id
                    val pName = p.name.ifEmpty { "जातक" }
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectProfile(p.id) },
                        label = { Text("👤 $pName", fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
                item {
                    AssistChip(
                        onClick = onAddNewProfile,
                        label = { Text("➕ नयाँ चिना", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                    )
                }
            }
        }

        // 2. Traditional Sacred Invocation Header Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(3.dp, RoundedCornerShape(18.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "॥ श्री गणेशाय नमः ॥",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    text = "📜 जन्म चिना पत्रिका (वैदिक कुण्डली विवरण)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "जातक: ${data.name} (${data.genderText})",
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "जन्म स्थान: ${data.birthPlaceText}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "जन्म मिति: ${data.bsBirthDateText}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "समय: ${data.birthTimeStringNepali}",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "इ.सं.: ${data.adBirthDateText}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f)
                    )
                    Text(
                        text = "सूर्योदय: ${data.sunriseText}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f)
                    )
                }
            }
        }

        // 3. Cheena Sections Navigation Tabs
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            val tabs = listOf("चक्र", "फलादेश", "उपाय", "दशा", "पञ्चाङ्ग", "ग्रह")
            tabs.forEachIndexed { index, label ->
                val isSelected = selectedSection == index
                SegmentedButton(
                    selected = isSelected,
                    onClick = { onSectionSelect(index) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = tabs.size),
                    icon = {},
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.primary,
                        activeContentColor = MaterialTheme.colorScheme.onPrimary,
                        activeBorderColor = MaterialTheme.colorScheme.primary,
                        inactiveContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    contentPadding = PaddingValues(horizontal = 2.dp, vertical = 0.dp)
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

        // 4. Section Specific Content Views
        Crossfade(targetState = selectedSection, label = "CheenaTabs") { section ->
            when (section) {
                0 -> CheenaChartsSection(data = data, onExplorePredictions = { onSectionSelect(1) })
                1 -> CheenaLifePredictionsSection(data = data)
                2 -> CheenaRemediesSection(data = data)
                3 -> CheenaDashaTimelineSection(data = data)
                4 -> CheenaPanchangaSection(data = data)
                5 -> CheenaPlanetaryTableSection(data = data)
            }
        }
    }
}

@Composable
private fun CheenaChartsSection(
    data: CheenaPatrikaData,
    onExplorePredictions: () -> Unit
) {
    var chartType by remember { mutableStateOf(KundaliChartType.LAGNA) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Chart Selector Segment
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            val chartOptions = listOf(
                KundaliChartType.LAGNA to "लग्न कुण्डली (D-1)",
                KundaliChartType.NAVAMSHA to "नवमांश चक्र (D-9)",
                KundaliChartType.CHANDRA to "चन्द्र कुण्डली"
            )
            chartOptions.forEachIndexed { index, (type, label) ->
                val isSelected = chartType == type
                SegmentedButton(
                    selected = isSelected,
                    onClick = { chartType = type },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = chartOptions.size),
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
                    Text(text = label, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium)
                }
            }
        }

        // Visual Kundali Canvas
        KundaliChart(
            lagnaRashiIndex = data.lagnaIndex,
            lagnaRashiName = data.lagnaName,
            snapshot = data.snapshot,
            chartType = chartType,
            navamshaLagnaIndex = data.navamshaLagnaIndex,
            navamshaLagnaName = data.navamshaLagnaName,
            modifier = Modifier.fillMaxWidth()
        )

        // Lagna and Rashi Nature Cards
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "लग्न तथा राशि फल विचार",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = data.lagnaNatureDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 22.sp
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                Text(
                    text = data.rashiNatureDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Button(
                    onClick = onExplorePredictions,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "विस्तृत जीवन फलादेश हेर्नुहोस् (९ क्षेत्र)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun CheenaLifePredictionsSection(data: CheenaPatrikaData) {
    val preds = data.lifePredictions
    val topics = remember(preds) {
        listOf(
            preds.personalityTraits,
            preds.wealthAndFinances,
            preds.educationAndLearning,
            preds.careerAndProfession,
            preds.familyAndHome,
            preds.healthAndLongevity,
            preds.marriageAndLove,
            preds.childrenAndProgeny,
            preds.parentsRelationship
        )
    }

    var selectedFilterIndex by remember { mutableIntStateOf(0) }

    val displayedTopics = if (selectedFilterIndex == 0) {
        topics
    } else {
        listOf(topics[selectedFilterIndex - 1])
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Overview Summary Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "सम्पूर्ण जीवन फलादेश (वैदिक विश्लेषण)",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Text(
                    text = "तपाईंको जन्म कुण्डलीको ${data.lagnaName} लग्न, ${data.janmaRashiName} राशि र ९ भावका अधिपतिहरूको सूक्ष्म ज्योतिषीय गणना अनुसार तयार पारिएको विस्तृत फलादेश:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                    lineHeight = 18.sp
                )
            }
        }

        // Horizontal Category Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                FilterChip(
                    selected = selectedFilterIndex == 0,
                    onClick = { selectedFilterIndex = 0 },
                    label = { Text("सबै (९ क्षेत्र)", fontSize = 11.5.sp, fontWeight = FontWeight.Bold) }
                )
            }
            items(topics.size) { idx ->
                val topic = topics[idx]
                FilterChip(
                    selected = selectedFilterIndex == idx + 1,
                    onClick = { selectedFilterIndex = idx + 1 },
                    label = { Text("${topic.iconEmoji} ${topic.title.split(" ")[0]}", fontSize = 11.5.sp) }
                )
            }
        }

        // Topic Prediction Cards
        displayedTopics.forEachIndexed { index, topic ->
            LifePredictionCard(
                topic = topic,
                isInitiallyExpanded = (selectedFilterIndex != 0 || index < 2)
            )
        }
    }
}

@Composable
private fun LifePredictionCard(
    topic: LifePredictionTopic,
    isInitiallyExpanded: Boolean
) {
    var isExpanded by remember(topic.title, isInitiallyExpanded) { mutableStateOf(isInitiallyExpanded) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Header Row (Clickable)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = topic.iconEmoji, fontSize = 18.sp)
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = topic.title,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = topic.primaryHouseInfo,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Always visible concise preview or full content when expanded
            if (!isExpanded) {
                Text(
                    text = topic.analysisSummary,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    lineHeight = 18.sp
                )
            } else {
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                // Full Analysis Summary
                Text(
                    text = topic.analysisSummary,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Strengths
                if (topic.favorableStrengths.isNotEmpty()) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "सकारात्मक पक्ष तथा शक्ति:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.5.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            topic.favorableStrengths.forEach { str ->
                                Text(
                                    text = "• $str",
                                    fontSize = 11.5.sp,
                                    lineHeight = 17.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // Precautions & Challenges
                if (topic.precautionsAndChallenges.isNotEmpty()) {
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "सावधानी तथा ध्यान दिनुपर्ने पक्ष:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.5.sp,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            topic.precautionsAndChallenges.forEach { chal ->
                                Text(
                                    text = "• $chal",
                                    fontSize = 11.5.sp,
                                    lineHeight = 17.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                // Guidance
                Surface(
                    color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onTertiaryContainer,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "शुभ सल्लाह र मार्गदर्शन:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                        Text(
                            text = topic.auspiciousGuidance,
                            fontSize = 11.5.sp,
                            lineHeight = 17.sp,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CheenaPanchangaSection(data: CheenaPatrikaData) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Birth Panchanga Details Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "पञ्चाङ्ग साधन (Panchanga at Birth)",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                CheenaDetailRow("संवत्सर विवरण", "${data.vikramSambatText} | ${data.nepalSambatText}")
                CheenaDetailRow("शक संवत्", data.shakaSambatText)
                CheenaDetailRow("अयन / ऋतु", "${data.ayanName} / ${data.rituName} ऋतु")
                CheenaDetailRow("मास / पक्ष", "${data.maasName} मास, ${data.pakshaName}")
                CheenaDetailRow("जन्म तिथि", data.tithiName)
                CheenaDetailRow("जन्म बार", "${data.vaarName} (वार स्वामी: ${data.vaarLord})")
                CheenaDetailRow("जन्म नक्षत्र", "${data.nakshatraName} (पाउ ${data.pada})")
                CheenaDetailRow("नक्षत्र स्वामी", data.nakshatraLord)
                CheenaDetailRow("योग / करण", "${data.yogaName} / ${data.karanaName}")
                CheenaDetailRow("सूर्योदय / सूर्यास्त", "${data.sunriseText} / ${data.sunsetText}")
                CheenaDetailRow("लाहिरी अयनांश", data.ayanamsaDegreesText)
            }
        }

        // Astakoota / Birth Essentials Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "जन्म अङ्ग तथा अष्टकूट आधार",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                CheenaDetailRow("जन्म राशि", "${data.janmaRashiName} (राशीश: ${data.rashiLord})")
                CheenaDetailRow("जन्म लग्न", "${data.lagnaName} (${data.lagnaDegreeFormatted})")
                CheenaDetailRow("लग्नेश (Lagna Lord)", data.lagnaLord)
                CheenaDetailRow("नवमांश लग्न", data.navamshaLagnaName)
                CheenaDetailRow("नामाक्षर (Baby Name Alphabet)", "'${data.naamAkshar}'")
                CheenaDetailRow("वर्ण (Varna)", data.varna)
                CheenaDetailRow("वश्य (Vashya)", data.vashya)
                CheenaDetailRow("योनि (Yoni)", data.yoni)
                CheenaDetailRow("गण (Gana)", data.gana)
                CheenaDetailRow("नाडी (Nadi)", data.nadi)
            }
        }

        // Time Measures (Ishtakala, Bhayat, Bhabhog) Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "काल साधन (इष्टकाल / भयात / भभोग)",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )

                CheenaDetailRow("इष्टकाल", data.ishtakalaGhadiPalaText, textColor = MaterialTheme.colorScheme.onTertiaryContainer)
                CheenaDetailRow("भयात", data.bhayatGhadiPalaText, textColor = MaterialTheme.colorScheme.onTertiaryContainer)
                CheenaDetailRow("भभोग", data.bhabhogGhadiPalaText, textColor = MaterialTheme.colorScheme.onTertiaryContainer)
                HorizontalDivider(color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.3f))
                CheenaDetailRow("जन्मकालीन भुक्त दशा", data.bhuktaDashaText, textColor = MaterialTheme.colorScheme.onTertiaryContainer)
                CheenaDetailRow("जन्मकालीन भोग्य दशा", data.bhogyaDashaText, textColor = MaterialTheme.colorScheme.onTertiaryContainer)
            }
        }
    }
}

@Composable
private fun CheenaPlanetaryTableSection(data: CheenaPatrikaData) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "सम्पूर्ण ग्रह स्पष्ट तालिका (Nirayana Planetary Positions)",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "जन्म समयमा आकाशमा रहेका ९ ग्रह र लग्नको वास्तविक वैदिक स्थिति:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // Table Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("ग्रह", fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.weight(1.2f))
                Text("राशि / अंश", fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.weight(1.5f))
                Text("नक्षत्र (पाउ)", fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.weight(1.3f))
                Text("अवस्था", fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.weight(1.1f), textAlign = TextAlign.End)
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // Planet Rows
            data.planetaryPositions.forEach { p ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Planet Name & Retrograde / Combust badge
                    Column(modifier = Modifier.weight(1.2f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = p.nameDevanagari,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (p.id == "lagna") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        if (p.isRetrograde || p.isCombust) {
                            val statusTag = buildString {
                                if (p.isRetrograde) append("वक्री ")
                                if (p.isCombust) append("अस्त")
                            }.trim()
                            Text(
                                text = "($statusTag)",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    // Rashi & Degrees
                    Column(modifier = Modifier.weight(1.5f)) {
                        Text(
                            text = p.rashiName,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                        Text(
                            text = p.formattedDegrees,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Nakshatra & Pada
                    Column(modifier = Modifier.weight(1.3f)) {
                        Text(
                            text = p.nakshatraName,
                            fontSize = 11.5.sp
                        )
                        Text(
                            text = "पाउ ${p.pada} (D9: ${p.navamshaRashiName})",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Dignity / Status
                    Surface(
                        color = when {
                            p.dignityText.contains("उच्च") -> MaterialTheme.colorScheme.primaryContainer
                            p.dignityText.contains("नीच") -> MaterialTheme.colorScheme.errorContainer
                            p.dignityText.contains("स्वगृह") -> MaterialTheme.colorScheme.secondaryContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.weight(1.1f)
                    ) {
                        Text(
                            text = p.dignityText.split(" ")[0],
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))
            }
        }
    }
}

@Composable
private fun CheenaDashaTimelineSection(data: CheenaPatrikaData) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "विंशोत्तरी महादशा जीवनचक्र (१२० वर्ष)",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "जन्म नक्षत्र स्वामीबाट सुरु हुने सम्पूर्ण १२० वर्षे महादशा तालिका र मितिहरू:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Timeline Cards
        data.vimshottariDashas.forEach { dasha ->
            val isActive = dasha.isCurrentlyActive

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh
                ),
                border = if (isActive) BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Dasha Lord Symbol & Name
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = dasha.lordSymbol,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 16.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${dasha.lordName} महादशा",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isActive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                )
                                if (isActive) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "वर्तमान दशा",
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "अवधि: ${NepaliNumerals.toNepaliDigits(String.format(java.util.Locale.US, "%.1f", dasha.durationYears))} वर्ष (उमेर ${NepaliNumerals.toNepaliDigits(dasha.startAgeYears.toInt())} देखि ${NepaliNumerals.toNepaliDigits(dasha.endAgeYears.toInt())} सम्म)",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.5.sp,
                                color = if (isActive) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Dates
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${dasha.startBsDateText} — ${dasha.endBsDateText}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp,
                            color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${dasha.startAdDate.year} - ${dasha.endAdDate.year} AD",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CheenaRemediesSection(data: CheenaPatrikaData) {
    val rem = data.remedies
    val compRem = data.lifePredictions.comprehensiveRemedies

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Auspicious Elements Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "शुभ तत्व तथा रत्न/रुद्राक्ष मार्गदर्शन",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                CheenaDetailRow("शुभ रत्न", rem.luckyStoneNepali)
                CheenaDetailRow("शुभ रुद्राक्ष", rem.luckyRudraksha)
                CheenaDetailRow("शुभ बार", rem.luckyDays)
                CheenaDetailRow("शुभ रङ्ग", rem.luckyColor)
                CheenaDetailRow("शुभ अंक", rem.luckyNumber)
                CheenaDetailRow("शुभ दिशा", rem.luckyDirection)
            }
        }

        // Ishta Devata & Mantras Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "इष्ट देवता तथा दैनिक जप मन्त्र",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    text = "• इष्ट देवता: ${rem.ishtaDevata}",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Surface(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "दैनिक ग्रह मन्त्र:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = rem.grahaMantra,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Surface(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "महा गायत्री मन्त्र:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = rem.gayatriMantra,
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // 1. Education Remedies
        RemedyCategoryCard(
            title = "शिक्षा तथा विद्या वृद्धिका वैदिक उपायहरू",
            iconEmoji = "🎓",
            remedies = compRem.educationRemedies,
            accentColor = MaterialTheme.colorScheme.primary
        )

        // 2. Health Remedies
        RemedyCategoryCard(
            title = "स्वास्थ्य तथा आरोग्य रक्षाका वैदिक उपायहरू",
            iconEmoji = "🌿",
            remedies = compRem.healthRemedies,
            accentColor = MaterialTheme.colorScheme.secondary
        )

        // 3. Wealth Remedies
        RemedyCategoryCard(
            title = "धन, सम्पत्ति तथा समृद्धि वृद्धिका उपायहरू",
            iconEmoji = "💰",
            remedies = compRem.wealthRemedies,
            accentColor = MaterialTheme.colorScheme.tertiary
        )

        // 4. Fortune / Bhagyodaya Remedies
        RemedyCategoryCard(
            title = "भाग्योदय तथा कार्य सिद्धिका वैदिक उपायहरू",
            iconEmoji = "🌟",
            remedies = compRem.fortuneRemedies,
            accentColor = MaterialTheme.colorScheme.primary
        )

        // 5. Daily Vedic Rituals
        RemedyCategoryCard(
            title = "नित्य वैदिक अनुष्ठान तथा कल्याणकारी नियमहरू",
            iconEmoji = "🕉️",
            remedies = compRem.dailyVedicRituals,
            accentColor = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun RemedyCategoryCard(
    title: String,
    iconEmoji: String,
    remedies: List<String>,
    accentColor: Color
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = iconEmoji, fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    color = accentColor
                )
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
            remedies.forEach { remedy ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "• ",
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = remedy,
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 19.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun CheenaDetailRow(
    title: String,
    value: String,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = textColor.copy(alpha = 0.75f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = textColor,
            textAlign = TextAlign.End
        )
    }
}
