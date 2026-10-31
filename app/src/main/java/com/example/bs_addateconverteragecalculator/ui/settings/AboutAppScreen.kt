@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.ui.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bs_addateconverteragecalculator.BuildConfig
import com.example.bs_addateconverteragecalculator.R
import com.example.bs_addateconverteragecalculator.ui.theme.NotoSerifDevanagari

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutAppScreen(
    onBackClick: () -> Unit
) {
    val versionText = stringResource(R.string.version_format, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.about_app_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0D1323),
                    titleContentColor = Color(0xFFFFFFFF),
                    navigationIconContentColor = Color(0xFFFFFFFF)
                )
            )
        },
        containerColor = Color(0xFF0D1323) // Deep Cosmic Navy background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // HERO HEADER SECTION
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                modifier = Modifier
                    .size(88.dp)
                    .shadow(8.dp, CircleShape),
                shape = CircleShape,
                color = Color(0xFFF59E0B) // Solar Gold
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.AutoAwesome,
                        contentDescription = "Glow Date Logo",
                        modifier = Modifier.size(48.dp),
                        tint = Color(0xFF0D1323)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Glow Date (ग्लो डेट)",
                style = MaterialTheme.typography.headlineSmall.copy(fontFamily = NotoSerifDevanagari),
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFFFFFFF)
            )

            Surface(
                color = Color(0xFF1E293B),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color(0x3FF97316))
            ) {
                Text(
                    text = versionText,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFFF59E0B),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }

            Text(
                text = stringResource(R.string.app_tagline),
                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = NotoSerifDevanagari),
                color = Color(0xFF94A3B8), // Slate Silver
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // SECTION 1: WHAT IS GLOW DATE?
            AboutCard(
                icon = Icons.Outlined.Info,
                title = stringResource(R.string.section_overview_title),
                description = stringResource(R.string.section_overview_desc)
            )

            // SECTION 2: DAILY LIFE APPLICATIONS
            AboutCard(
                icon = Icons.Outlined.Schedule,
                title = stringResource(R.string.section_daily_use_title),
                description = buildString {
                    append(stringResource(R.string.daily_use_item_1_title)).append("\n")
                    append(stringResource(R.string.daily_use_item_1_desc)).append("\n\n")
                    append(stringResource(R.string.daily_use_item_2_title)).append("\n")
                    append(stringResource(R.string.daily_use_item_2_desc)).append("\n\n")
                    append(stringResource(R.string.daily_use_item_3_title)).append("\n")
                    append(stringResource(R.string.daily_use_item_3_desc)).append("\n\n")
                    append(stringResource(R.string.daily_use_item_4_title)).append("\n")
                    append(stringResource(R.string.daily_use_item_4_desc))
                }
            )

            // SECTION 3: ASTRONOMICAL PRECISION & TRUST
            AboutCard(
                icon = Icons.Outlined.Security,
                title = stringResource(R.string.section_accuracy_title),
                description = stringResource(R.string.section_accuracy_desc)
            )

            // SECTION 4: CALCULATION LOGIC SUMMARY (Attractive Simple English Sub-Cards Layout)
            CalculationLogicCard()

            Spacer(modifier = Modifier.height(8.dp))

            // FOOTER COPYRIGHT & DEVELOPER CREDIT
            Text(
                text = stringResource(R.string.about_developer_credit),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center,
                lineHeight = 18.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
    }
}

@Composable
fun AboutCard(
    icon: ImageVector,
    title: String,
    description: String
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)), // Midnight Slate
        border = BorderStroke(1.dp, Color(0x3FF97316)), // Subtle 1.dp orange stroke (rgba(249, 115, 22, 0.25))
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF97316).copy(alpha = 0.15f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            tint = Color(0xFFF97316),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontFamily = NotoSerifDevanagari),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF59E0B) // Solar Amber
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = NotoSerifDevanagari),
                color = Color(0xFFE2E8F0),
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
fun CalculationLogicCard() {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)), // Midnight Slate
        border = BorderStroke(1.dp, Color(0x3FF97316)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Section Title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF97316).copy(alpha = 0.15f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.Calculate,
                            contentDescription = "Calculations",
                            tint = Color(0xFFF97316),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Calculation Logic & Formulas",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF59E0B) // Solar Amber
                    )
                    Text(
                        text = "High-Precision Astronomical & Vedic Math Engine",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            HorizontalDivider(color = Color(0xFF334155))

            // Sub Card 1: Panchanga Logic
            LogicSubCard(
                badgeText = "1. PANCHANGA LOGIC",
                badgeColor = Color(0xFF38BDF8), // Cyan
                items = listOf(
                    "Nirayana Longitudes: Sun and Moon positions computed via NASA VSOP87 & Jean Meeus high-precision models.",
                    "Tithi: Moon-Sun longitude difference. Exact end times and Ghadi-Pala computed using 4-step Newton-Raphson root finding.",
                    "Nakshatra & 108 Namakshars: Derived from Moon longitude (13°20' per Nakshatra, 3°20' per Pada).",
                    "Yoga & Karana: Calculated from Sun-Moon angle sums (27 Yogas) and half-tithi divisions (11 Karanas)."
                )
            )

            // Sub Card 2: Birth Kundali Chart
            LogicSubCard(
                badgeText = "2. KUNDALI CHART LOGIC",
                badgeColor = Color(0xFFF43F5E), // Rose
                items = listOf(
                    "Julian Day & Lahiri Ayanamsha: UTC time converted to Julian Day (JD) to compute Chitrapaksha (Lahiri) Ayanamsha.",
                    "Trigonometric Ascendant (Lagna): Sidereal LST, latitude, and obliquity processed via atan2(y, x) formula.",
                    "12 Houses & 9 Planets: Places all 9 planets (Sun..Ketu) into the 12 houses of the North-Indian Diamond Kundali Chart.",
                    "Ishtakala & Dashas: Computes Ishtakala in Ghadi-Pala from sunrise, Bhayat, Bhabhog, and active Vimshottari/Yogini Dashas."
                )
            )

            // Sub Card 3: Rashifal & Transit Analysis
            LogicSubCard(
                badgeText = "3. RASHIFAL & TRANSIT LOGIC",
                badgeColor = Color(0xFF10B981), // Emerald
                items = listOf(
                    "Moon Transit (Gochar): Tracks Moon movement across 12 houses (1..12) relative to your Natal Moon.",
                    "Tara Bala & Chandra Bala: 9-fold Nakshatra harmony (Tara) and Moon house favorability (3, 6, 7, 10, 11 auspicious; 8 Chandrashtama).",
                    "Jupiter & Saturn Transits: Evaluates Jupiter's grace and Saturn's Sade Sati / Dhaiya for Daily, Weekly, Monthly, and Annual forecasts."
                )
            )

            // Sub Card 4: Nepal Sambat History & Calendar Rules
            LogicSubCard(
                badgeText = "4. NEPAL SAMBAT (नेपाल संवत्)",
                badgeColor = Color(0xFFA855F7), // Purple
                items = listOf(
                    "Historic Origin: Started on AD 879 October 20 (Kartik Shukla 1) by Shankhadhar Sakhwa.",
                    "New Year (Nhudhan / Mha Puja): Advances every year on Kartik Shukla Pratipada (Mha Puja / Nhudhan).",
                    "Amanta Lunar Month: 1 Month = One Aunsi (Aamasa) to the next Aunsi. Shukla Paksha (Thwa 'थ्व') comes first, followed by Krishna Paksha (Ga 'गा').",
                    "Lunar Year Length: Varies from 354 to 378 days per year (including Anala / Adhik Maas intercalation)."
                )
            )
        }
    }
}

@Composable
private fun LogicSubCard(
    badgeText: String,
    badgeColor: Color,
    items: List<String>
) {
    Surface(
        color = Color(0xFF1E293B), // Dark Slate
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, badgeColor.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                color = badgeColor.copy(alpha = 0.15f),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = badgeText,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = badgeColor,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }

            items.forEach { itemText ->
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "• ",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = badgeColor
                    )
                    Text(
                        text = itemText,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFE2E8F0),
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
