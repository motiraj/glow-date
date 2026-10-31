package com.example.bs_addateconverteragecalculator.ui.settings

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bs_addateconverteragecalculator.data.GlobalLocation
import com.example.bs_addateconverteragecalculator.data.PreferencesManager
import com.example.bs_addateconverteragecalculator.data.RashifalData
import com.example.bs_addateconverteragecalculator.utils.LocationHelper
import com.example.bs_addateconverteragecalculator.widget.NepaliDateClockReceiver
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Info
import com.example.bs_addateconverteragecalculator.utils.AppStrings
import com.example.bs_addateconverteragecalculator.worker.VedicTimingNotificationScheduler
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onLanguageClick: () -> Unit = {},
    onAboutClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val coroutineScope = rememberCoroutineScope()

    val isDarkMode by preferencesManager.darkModeFlow.collectAsState(initial = false)
    val language by preferencesManager.languageFlow.collectAsState(initial = "en")
    val lang = language
    val userZodiac by preferencesManager.userZodiacFlow.collectAsState(initial = "aries")
    
    val locationData by preferencesManager.locationFlow.collectAsState(initial = null)
    val isAutoGps by preferencesManager.isAutoGpsFlow.collectAsState(initial = false)
    val isAbhijitEnabled by preferencesManager.abhijitAlertFlow.collectAsState(initial = true)
    val isRahuEnabled by preferencesManager.rahuAlertFlow.collectAsState(initial = true)
    val isPersistentNotificationEnabled by preferencesManager.persistentNotificationFlow.collectAsState(initial = true)
    val isFestivalNotificationEnabled by preferencesManager.festivalNotificationFlow.collectAsState(initial = true)
    val ayanamsaMode by preferencesManager.ayanamsaModeFlow.collectAsState(initial = com.example.bs_addateconverteragecalculator.data.AyanamsaMode.LAHIRI_DRIK)

    var showLocationDialog by remember { mutableStateOf(false) }
    var showZodiacDialog by remember { mutableStateOf(false) }
    var showAyanamsaDialog by remember { mutableStateOf(false) }
    var locationSearchQuery by remember { mutableStateOf("") }

    val currentZodiacSign = remember(userZodiac) {
        RashifalData.signs.find { it.id == userZodiac } ?: RashifalData.signs.first()
    }

    // Launcher for location permissions
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || 
                      permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            LocationHelper.fetchAndSaveDeviceLocation(context, preferencesManager, coroutineScope)
        } else {
            coroutineScope.launch { preferencesManager.setAutoGpsEnabled(false) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(AppStrings.get("settings_title", lang), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 840.dp)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
            item {
                Text(
                    text = AppStrings.get("app_preferences", lang),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
                
                Card(
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Theme Setting
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(AppStrings.get("dark_mode", lang), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                Text(AppStrings.get("dark_mode_desc", lang), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Switch(
                                checked = isDarkMode,
                                onCheckedChange = { checked ->
                                    coroutineScope.launch {
                                        preferencesManager.setDarkMode(checked)
                                    }
                                }
                            )
                        }
                        
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)

                        // Dynamic Color Setting (Android 12+)
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                            val isDynamicColor by preferencesManager.dynamicColorFlow.collectAsState(initial = false)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(AppStrings.get("dynamic_colors", lang), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                    Text(AppStrings.get("dynamic_colors_desc", lang), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                Switch(
                                    checked = isDynamicColor,
                                    onCheckedChange = { checked ->
                                        coroutineScope.launch {
                                            preferencesManager.setDynamicColor(checked)
                                        }
                                    }
                                )
                            }
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)
                        }

                        // Language Setting (Clickable to open LanguageSettingsScreen)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onLanguageClick() },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = AppStrings.get("language_numerals", lang),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                val currentLangDisplay = when (language) {
                                    "en" -> "English"
                                    "hi" -> "हिन्दी"
                                    else -> "नेपाली"
                                }
                                Text(
                                    text = AppStrings.get("current_lang_label", lang) + currentLangDisplay,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Open Language Settings",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)

                        // Master "All Notifications" Row
                        val allNotificationsEnabled = isPersistentNotificationEnabled && isAbhijitEnabled && isRahuEnabled && isFestivalNotificationEnabled
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("सबै सूचनाहरू (All Notifications)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("दैनिक मिति, चाडपर्व, अभिजित तथा राहुकालका सबै सूचनाहरू", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                            }
                            Switch(
                                checked = allNotificationsEnabled,
                                onCheckedChange = { checked ->
                                    coroutineScope.launch {
                                        preferencesManager.setAllNotificationsEnabled(checked)
                                        if (checked) {
                                            com.example.bs_addateconverteragecalculator.worker.NotificationHelper.showPersistentDateNotification(context)
                                            com.example.bs_addateconverteragecalculator.worker.VedicTimingNotificationScheduler.scheduleDailyTimingAlarms(context)
                                            com.example.bs_addateconverteragecalculator.worker.FestivalNotificationHelper.scheduleFestivalAlarms(context)
                                            com.example.bs_addateconverteragecalculator.worker.FestivalNotificationHelper.checkAndNotifyFestival(context)
                                        } else {
                                            com.example.bs_addateconverteragecalculator.worker.NotificationHelper.cancelPersistentDateNotification(context)
                                        }
                                    }
                                }
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)

                        // Persistent Nepali Date Notification Bar Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("नेपाली मिति नोटिफिकेशन बार (Status Bar Date)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                Text("मोबाइल नोटिफिकेशन बारमा निरन्तर दैनिक मिति र नेपाल संवत् देखाइराख्नुहोस्", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Switch(
                                checked = isPersistentNotificationEnabled,
                                onCheckedChange = { checked ->
                                    coroutineScope.launch {
                                        preferencesManager.setPersistentNotificationEnabled(checked)
                                        if (checked) {
                                            com.example.bs_addateconverteragecalculator.worker.NotificationHelper.showPersistentDateNotification(context)
                                        } else {
                                            com.example.bs_addateconverteragecalculator.worker.NotificationHelper.cancelPersistentDateNotification(context)
                                        }
                                    }
                                }
                            )
                        }

                        // Abhijit Alert Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("अभिजित मुहूर्त सूचना", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                Text("10 mins before Abhijit Muhurta starts", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Switch(
                                checked = isAbhijitEnabled,
                                onCheckedChange = { checked ->
                                    coroutineScope.launch {
                                        preferencesManager.setAbhijitAlertEnabled(checked)
                                        if (checked) VedicTimingNotificationScheduler.scheduleDailyTimingAlarms(context)
                                    }
                                }
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)

                        // Rahu Kaal Alert Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("राहुकाल सतर्कता सूचना", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                Text("10 mins before Rahu Kalam starts", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Switch(
                                checked = isRahuEnabled,
                                onCheckedChange = { checked ->
                                    coroutineScope.launch {
                                        preferencesManager.setRahuAlertEnabled(checked)
                                        if (checked) VedicTimingNotificationScheduler.scheduleDailyTimingAlarms(context)
                                    }
                                }
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)

                        // Festival & Parva Special Alert Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("चाडपर्व तथा एकादशी विशेष सूचना (Festivals & Parva Alerts)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                Text("दशैं, तिहार, छठ, शिवरात्रि, तीज, एकादशी आदि मुख्य चाडपर्व सुरु हुनु अगावै विशेषता, महत्व र विधिको सूचना", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Switch(
                                checked = isFestivalNotificationEnabled,
                                onCheckedChange = { checked ->
                                    coroutineScope.launch {
                                        preferencesManager.setFestivalNotificationEnabled(checked)
                                        if (checked) {
                                            com.example.bs_addateconverteragecalculator.worker.FestivalNotificationHelper.scheduleFestivalAlarms(context)
                                            com.example.bs_addateconverteragecalculator.worker.FestivalNotificationHelper.checkAndNotifyFestival(context)
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = AppStrings.get("astrology_preferences", lang),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Card(
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showZodiacDialog = true },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(AppStrings.get("default_zodiac", lang), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                Text(AppStrings.get("selected_label", lang) + "${currentZodiacSign.symbol} ${currentZodiacSign.nameNepali} (${currentZodiacSign.nameEnglish})", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)
                            }
                            TextButton(onClick = { showZodiacDialog = true }) {
                                Text(AppStrings.get("change", lang))
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showAyanamsaDialog = true },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("अयनांश गणना पद्दति (Ayanamsa System)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                val modeDisplay = if (ayanamsaMode == com.example.bs_addateconverteragecalculator.data.AyanamsaMode.LAHIRI_DRIK) "चित्रपक्षीय (लहिरी) - Modern Drik" else "सूर्य सिद्धान्त परम्परागत"
                                Text(AppStrings.get("current_lang_label", lang) + modeDisplay, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)
                            }
                            TextButton(onClick = { showAyanamsaDialog = true }) {
                                Text(AppStrings.get("change", lang))
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = AppStrings.get("location_data", lang),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Card(
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Auto GPS Location Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(AppStrings.get("auto_gps", lang), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                Text(AppStrings.get("auto_gps_desc", lang), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Switch(
                                checked = isAutoGps,
                                onCheckedChange = { checked ->
                                    if (checked) {
                                        if (LocationHelper.hasLocationPermission(context)) {
                                            if (!LocationHelper.isGpsEnabled(context)) {
                                                LocationHelper.promptEnableGps(context)
                                            } else {
                                                LocationHelper.fetchAndSaveDeviceLocation(context, preferencesManager, coroutineScope)
                                            }
                                        } else {
                                            locationPermissionLauncher.launch(
                                                arrayOf(
                                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                                )
                                            )
                                        }
                                    } else {
                                        coroutineScope.launch {
                                            preferencesManager.setAutoGpsEnabled(false)
                                            preferencesManager.setLocation("Kathmandu, Nepal")
                                        }
                                    }
                                }
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)

                        // Manual TimeZone / Location Selection (Disabled if Auto GPS is on)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = !isAutoGps) { showLocationDialog = true },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    AppStrings.get("district_selection", lang), 
                                    style = MaterialTheme.typography.titleMedium, 
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isAutoGps) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f) else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = AppStrings.get("current_lang_label", lang) + (locationData?.name ?: "Loading..."), 
                                    style = MaterialTheme.typography.bodyMedium, 
                                    color = if (isAutoGps) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Medium
                                )
                                if (isAutoGps) {
                                    Text(
                                        text = AppStrings.get("managed_gps", lang), 
                                        style = MaterialTheme.typography.bodySmall, 
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                    )
                                }
                            }
                            
                            if (isAutoGps) {
                                IconButton(
                                    onClick = { LocationHelper.fetchAndSaveDeviceLocation(context, preferencesManager, coroutineScope) }
                                ) {
                                    Icon(Icons.Default.GpsFixed, contentDescription = "Refresh GPS", tint = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = AppStrings.get("about", lang),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Card(
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAboutClick() }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "About",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(AppStrings.get("about_app_guide", lang), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                Text(AppStrings.get("version_info", lang), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Navigate",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text(
                    text = AppStrings.get("developer_info", lang),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Card(
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            modifier = Modifier.size(80.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Developer",
                                    modifier = Modifier.size(40.dp),
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Moti Raj Gautam",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "(MotiKrishna)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Academician & Software Engineer",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = "Assistant Campus Chief, IQAC/SAT Coordinator & Full-Stack WordPress Engineer.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { 
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://motiraj.github.io/motiraj/"))
                                context.startActivity(intent)
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Language, contentDescription = "Website", modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(AppStrings.get("visit_website", lang))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
    }

    if (showAyanamsaDialog) {
        AlertDialog(
            onDismissRequest = { showAyanamsaDialog = false },
            title = { Text("अयनांश पद्दति छान्नुहोस् (Ayanamsa Mode)") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                coroutineScope.launch {
                                    preferencesManager.setAyanamsaMode(com.example.bs_addateconverteragecalculator.data.AyanamsaMode.LAHIRI_DRIK)
                                    showAyanamsaDialog = false
                                }
                            }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = ayanamsaMode == com.example.bs_addateconverteragecalculator.data.AyanamsaMode.LAHIRI_DRIK,
                            onClick = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("चित्रपक्षीय (लहिरी) - Modern Drik", fontWeight = FontWeight.Bold)
                            Text("आधुनिक दृक सिद्धान्त तथा नासा/VSOP87 मोडलमा आधारित", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                coroutineScope.launch {
                                    preferencesManager.setAyanamsaMode(com.example.bs_addateconverteragecalculator.data.AyanamsaMode.SURYA_SIDDHANTA_TRADITIONAL)
                                    showAyanamsaDialog = false
                                }
                            }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = ayanamsaMode == com.example.bs_addateconverteragecalculator.data.AyanamsaMode.SURYA_SIDDHANTA_TRADITIONAL,
                            onClick = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("सूर्य सिद्धान्त - Traditional", fontWeight = FontWeight.Bold)
                            Text("परम्परागत ५४ विकला प्रतिवर्ष अयन गतिको आधारमा", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAyanamsaDialog = false }) {
                    Text(AppStrings.get("close", lang))
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    if (showZodiacDialog) {
        AlertDialog(
            onDismissRequest = { showZodiacDialog = false },
            title = { Text(AppStrings.get("select_default_rashi", lang)) },
            text = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 350.dp)
                ) {
                    items(RashifalData.signs, key = { it.id }) { sign ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    coroutineScope.launch {
                                        preferencesManager.setUserZodiac(sign.id)
                                        NepaliDateClockReceiver.updateAllWidgets(context)
                                        showZodiacDialog = false
                                    }
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = sign.id == currentZodiacSign.id,
                                onClick = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${sign.symbol}  ${sign.nameNepali} (${sign.nameEnglish})",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showZodiacDialog = false }) {
                    Text(AppStrings.get("close", lang))
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    if (showLocationDialog) {
        val filteredLocations = remember(locationSearchQuery) {
            if (locationSearchQuery.isBlank()) {
                GlobalLocation.locations
            } else {
                GlobalLocation.locations.filter { 
                    it.name.contains(locationSearchQuery, ignoreCase = true) 
                }
            }
        }

        AlertDialog(
            onDismissRequest = { 
                showLocationDialog = false
                locationSearchQuery = ""
            },
            title = { Text(AppStrings.get("select_nepal_district", lang)) },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = locationSearchQuery,
                        onValueChange = { locationSearchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        placeholder = { Text(AppStrings.get("search_city", lang)) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 350.dp)
                    ) {
                        items(filteredLocations, key = { it.name }) { loc ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        coroutineScope.launch {
                                            preferencesManager.setLocation(loc.name)
                                            showLocationDialog = false
                                            locationSearchQuery = ""
                                        }
                                    }
                                    .padding(vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = loc.name == locationData?.name,
                                    onClick = null
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(loc.name, fontWeight = FontWeight.Bold)
                                    Text(
                                        text = "Lat: ${loc.lat}, Lon: ${loc.lon} (UTC ${if (loc.tzOffset >= 0) "+" else ""}${loc.tzOffset})",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { 
                        showLocationDialog = false
                        locationSearchQuery = ""
                    }
                ) {
                    Text(AppStrings.get("close", lang))
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}
