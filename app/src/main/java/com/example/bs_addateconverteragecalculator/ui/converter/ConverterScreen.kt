package com.example.bs_addateconverteragecalculator.ui.converter

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bs_addateconverteragecalculator.ui.adtobs.AdToBsScreen
import com.example.bs_addateconverteragecalculator.ui.agecalc.AgeCalculatorScreen
import com.example.bs_addateconverteragecalculator.ui.bstoad.BsToAdScreen
import com.example.bs_addateconverteragecalculator.utils.AppStrings
import com.example.bs_addateconverteragecalculator.utils.LocalAppLanguage

@Composable
fun ConverterScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val lang = LocalAppLanguage.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 840.dp)
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Stylish Segmented Control Tabs (3 tabs: BS->AD, AD->BS, Age)
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                val tabs = listOf(
                    AppStrings.get("tab_bs_to_ad", lang),
                    AppStrings.get("tab_ad_to_bs", lang),
                    AppStrings.get("tab_age_calc", lang)
                )
                tabs.forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    SegmentedButton(
                        selected = isSelected,
                        onClick = { selectedTab = index },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = tabs.size),
                        icon = {},
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = MaterialTheme.colorScheme.primary,
                            activeContentColor = MaterialTheme.colorScheme.onPrimary,
                            activeBorderColor = MaterialTheme.colorScheme.primary,
                            inactiveContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                            inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                    ) {
                        Text(
                            text = title,
                            fontSize = 13.5.sp,
                            maxLines = 1,
                            softWrap = false,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Animated Screen Transitions using Crossfade for smoother tabs
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                Crossfade(
                    targetState = selectedTab,
                    animationSpec = tween(400),
                    label = "ConverterTabs"
                ) { tab ->
                    when (tab) {
                        0 -> BsToAdScreen()
                        1 -> AdToBsScreen()
                        2 -> AgeCalculatorScreen()
                    }
                }
            }
        }
    }
}
