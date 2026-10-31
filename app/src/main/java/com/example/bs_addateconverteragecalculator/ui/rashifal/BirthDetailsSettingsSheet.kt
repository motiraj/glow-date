@file:Suppress("NewApi", "SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.ui.rashifal

import android.app.TimePickerDialog
import android.content.Intent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.core.net.toUri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AddLocationAlt
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bs_addateconverteragecalculator.converter.DateConverterEngine
import com.example.bs_addateconverteragecalculator.data.BirthProfile
import com.example.bs_addateconverteragecalculator.data.GlobalLocation
import com.example.bs_addateconverteragecalculator.data.NepaliDate
import com.example.bs_addateconverteragecalculator.data.NepaliNumerals
import com.example.bs_addateconverteragecalculator.ui.components.DatePickerBs
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthDetailsSettingsSheet(
    initialProfile: BirthProfile?,
    onDismiss: () -> Unit,
    onSaveProfile: (BirthProfile) -> Unit,
    onClearProfile: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val profile = initialProfile ?: BirthProfile()

    var name by remember(initialProfile) { mutableStateOf(profile.name) }
    var gender by remember(initialProfile) { mutableStateOf(profile.gender) }
    var isBsInput by remember(initialProfile) { mutableStateOf(profile.isBsInput) }
    var bsYear by remember(initialProfile) { mutableIntStateOf(profile.bsYear) }
    var bsMonth by remember(initialProfile) { mutableIntStateOf(profile.bsMonth) }
    var bsDay by remember(initialProfile) { mutableIntStateOf(profile.bsDay) }

    var adBirthDateStr by remember(initialProfile) { mutableStateOf(profile.adBirthDate) }
    var birthHour by remember(initialProfile) { mutableIntStateOf(profile.birthHour) }
    var birthMinute by remember(initialProfile) { mutableIntStateOf(profile.birthMinute) }

    var selectedLocationName by remember(initialProfile) { mutableStateOf(profile.locationName) }
    var latitude by remember(initialProfile) { mutableDoubleStateOf(profile.latitude) }
    var longitude by remember(initialProfile) { mutableDoubleStateOf(profile.longitude) }
    var timezoneOffset by remember(initialProfile) { mutableDoubleStateOf(profile.timezoneOffset) }
    var isEnabled by remember(initialProfile) { mutableStateOf(profile.isEnabled) }

    var isCustomLocationMode by remember(initialProfile) { mutableStateOf(false) }
    var latitudeStr by remember(initialProfile) { mutableStateOf(latitude.toString()) }
    var longitudeStr by remember(initialProfile) { mutableStateOf(longitude.toString()) }
    var timezoneOffsetStr by remember(initialProfile) { mutableStateOf(timezoneOffset.toString()) }
    var pastedMapInput by remember(initialProfile) { mutableStateOf("") }

    var showLocationSearchDialog by remember { mutableStateOf(false) }
    var locationSearchQuery by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Text(
                text = "जन्म विवरण र कुण्डली सेटिङ (Birth Details & Profile)",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "व्यक्तिगत कुण्डली, ताराबल, चन्द्रबल र विंशोत्तरी दशा अनुसारको सूक्ष्म राशिफल हेर्न आफ्नो जन्म विवरण भर्नुहोस्।",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // Name Input
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("तपाईँको नाम (Name - Optional)") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // Gender Selector
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "लिङ्ग (Gender):",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    val genderOptions = listOf(
                        com.example.bs_addateconverteragecalculator.data.UserGender.MALE to "पुरुष",
                        com.example.bs_addateconverteragecalculator.data.UserGender.FEMALE to "महिला",
                        com.example.bs_addateconverteragecalculator.data.UserGender.UNSPECIFIED to "नखुलाउने"
                    )
                    genderOptions.forEachIndexed { index, (gOption, label) ->
                        val isSelected = gender == gOption
                        SegmentedButton(
                            selected = isSelected,
                            onClick = { gender = gOption },
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = genderOptions.size),
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
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.5.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }
            }

            // BS <-> AD Calendar Type Selector
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "मिति प्रणाली (Calendar Type):",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    val calOptions = listOf(
                        true to "वि.सं. (BS)",
                        false to "इ.सं. (AD)"
                    )
                    calOptions.forEachIndexed { index, (isBs, label) ->
                        val isSelected = isBsInput == isBs
                        SegmentedButton(
                            selected = isSelected,
                            onClick = { isBsInput = isBs },
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = calOptions.size),
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
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }
            }

            // Birth Date Input
            if (isBsInput) {
                Text("जन्म मिति (Bikram Sambat - BS):", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                DatePickerBs(
                    selectedYear = bsYear,
                    selectedMonth = bsMonth,
                    selectedDay = bsDay,
                    onDateSelected = { y, m, d ->
                        bsYear = y
                        bsMonth = m
                        bsDay = d
                        try {
                            val ad = DateConverterEngine.convertBsToAd(NepaliDate(y, m, d))
                            adBirthDateStr = ad.format(DateTimeFormatter.ISO_LOCAL_DATE)
                        } catch (_: Exception) {}
                    }
                )
            } else {
                Text("जन्म मिति (Gregorian - AD):", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                val currentAd = try { LocalDate.parse(adBirthDateStr) } catch (_: Exception) { LocalDate.of(2001, 4, 14) }
                OutlinedButton(
                    onClick = {
                        val dpd = android.app.DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                val ad = LocalDate.of(year, month + 1, dayOfMonth)
                                adBirthDateStr = ad.format(DateTimeFormatter.ISO_LOCAL_DATE)
                                try {
                                    val bs = DateConverterEngine.convertAdToBs(ad)
                                    bsYear = bs.year
                                    bsMonth = bs.month
                                    bsDay = bs.day
                                } catch (_: Exception) {}
                            },
                            currentAd.year,
                            currentAd.monthValue - 1,
                            currentAd.dayOfMonth
                        )
                        dpd.show()
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = currentAd.format(DateTimeFormatter.ofPattern("dd MMMM yyyy (EEEE)", Locale.ENGLISH)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }

            // Birth Time Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("जन्म समय (Birth Time):", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    val formattedTime = String.format(Locale.US, "%02d:%02d", birthHour, birthMinute)
                    Text(
                        text = "समय: ${NepaliNumerals.toNepaliDigits(formattedTime)} (${if (birthHour >= 12) "बेलुकी/दिउँसो" else "बिहान"})",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                OutlinedButton(
                    onClick = {
                        val tpd = TimePickerDialog(
                            context,
                            { _, hourOfDay, minute ->
                                birthHour = hourOfDay
                                birthMinute = minute
                            },
                            birthHour,
                            birthMinute,
                            false
                        )
                        tpd.show()
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.AccessTime, contentDescription = "Birth Time", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("समय छान्नुहोस्", fontWeight = FontWeight.Bold)
                }
            }

            // Birthplace Selector Section (Place Search First, Custom Map Pin-Point Last)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "जन्म स्थान छनोट (Birthplace & Location):",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // 1. PRIMARY LOCATION SEARCH (SEARCH BY PLACE FIRST)
                OutlinedButton(
                    onClick = { showLocationSearchDialog = true },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = selectedLocationName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(Icons.Default.Search, contentDescription = "Search Locations")
                }

                Text(
                    text = "अक्षांश: $latitude° • देशान्तर: $longitude° • समय क्षेत्र: UTC+${timezoneOffset} घण्टा",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // 2. SECONDARY: CUSTOM / MANUAL MAP PIN-POINT CARD (PLACED AFTER / LAST)
                Card(
                    onClick = { isCustomLocationMode = !isCustomLocationMode },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp).animateContentSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AddLocationAlt,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "कस्टम स्थान / म्यानुअल Pin-Point",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Icon(
                                imageVector = if (isCustomLocationMode) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Toggle Custom Map Pin",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        if (isCustomLocationMode) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                            Text(
                                text = "गूगल म्याप्स लिङ्क वा कोअर्डिनेट्सबाट स्वतः भर्नुहोस्:",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            OutlinedTextField(
                                value = pastedMapInput,
                                onValueChange = { pastedMapInput = it },
                                label = { Text("Paste Google Maps Link / Lat,Lon (e.g. 27.7172, 85.3240)") },
                                leadingIcon = { Icon(Icons.Default.Map, contentDescription = null) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        val parsed = parseCoordinatesFromInput(pastedMapInput)
                                        if (parsed != null) {
                                            val (pLat, pLon) = parsed
                                            latitude = pLat
                                            longitude = pLon
                                            latitudeStr = pLat.toString()
                                            longitudeStr = pLon.toString()

                                            val estTz = estimateTimezoneOffset(pLat, pLon)
                                            timezoneOffset = estTz
                                            timezoneOffsetStr = estTz.toString()

                                            val addr = lookupAddressFromLatLon(context, pLat, pLon)
                                            selectedLocationName = addr
                                            android.widget.Toast.makeText(context, "स्थान प्राप्त भयो: $addr", android.widget.Toast.LENGTH_SHORT).show()
                                        } else {
                                            android.widget.Toast.makeText(context, "कोअर्डिनेट्स फेला परेन। कृपया लिङ्क वा २७.७१७२, ८५.३२४० राख्नुहोस्।", android.widget.Toast.LENGTH_LONG).show()
                                        }
                                    },
                                    modifier = Modifier.weight(1f).height(44.dp),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("स्वचालित तान्नुहोस् (Auto-Fetch)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = {
                                        val uri = "https://www.google.com/maps".toUri()
                                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                                    },
                                    modifier = Modifier.height(44.dp),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Map खोल्नुहोस्", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                            OutlinedTextField(
                                value = selectedLocationName,
                                onValueChange = { selectedLocationName = it },
                                label = { Text("स्थानको नाम (Location Name)") },
                                leadingIcon = { Icon(Icons.Default.AddLocationAlt, contentDescription = null) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = latitudeStr,
                                    onValueChange = {
                                        latitudeStr = it
                                        latitude = it.toDoubleOrNull() ?: latitude
                                    },
                                    label = { Text("अक्षांश (Latitude)") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                )

                                OutlinedTextField(
                                    value = longitudeStr,
                                    onValueChange = {
                                        longitudeStr = it
                                        longitude = it.toDoubleOrNull() ?: longitude
                                    },
                                    label = { Text("देशान्तर (Longitude)") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }

                            OutlinedTextField(
                                value = timezoneOffsetStr,
                                onValueChange = {
                                    timezoneOffsetStr = it
                                    timezoneOffset = it.toDoubleOrNull() ?: timezoneOffset
                                },
                                label = { Text("समय क्षेत्र (UTC Offset Hours e.g. 5.75 for Nepal)") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }
                    }
                }
            }

            // Enable Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("व्यक्तिगत राशिफल सक्रिय गर्नुहोस् (Enable Personalized Rashifal)", fontWeight = FontWeight.Bold)
                    Text("बन्द गर्दा साधारण १२ राशिको राशिफल मात्र देखिनेछ।", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = isEnabled,
                    onCheckedChange = { isEnabled = it }
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // Save & Clear Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (initialProfile != null) {
                    OutlinedButton(
                        onClick = {
                            onClearProfile()
                            onDismiss()
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("पुनःसेट (Reset)", fontWeight = FontWeight.Bold)
                    }
                }

                Button(
                    onClick = {
                        val computedAd = if (isBsInput) {
                            try {
                                DateConverterEngine.convertBsToAd(NepaliDate(bsYear, bsMonth, bsDay)).format(DateTimeFormatter.ISO_LOCAL_DATE)
                            } catch (_: Exception) { adBirthDateStr }
                        } else {
                            adBirthDateStr
                        }

                        val updatedProfile = BirthProfile(
                            id = initialProfile?.id ?: java.util.UUID.randomUUID().toString(),
                            name = name.trim(),
                            gender = gender,
                            isBsInput = isBsInput,
                            bsYear = bsYear,
                            bsMonth = bsMonth,
                            bsDay = bsDay,
                            adBirthDate = computedAd,
                            birthHour = birthHour,
                            birthMinute = birthMinute,
                            locationName = selectedLocationName,
                            latitude = latitude,
                            longitude = longitude,
                            timezoneOffset = timezoneOffset,
                            isEnabled = isEnabled
                        )

                        onSaveProfile(updatedProfile)
                        onDismiss()
                    },
                    modifier = Modifier.weight(1.5f).height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("सुरक्षित गर्नुहोस् (Save Profile)", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showLocationSearchDialog) {
            val filteredList = remember(locationSearchQuery) {
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
                    showLocationSearchDialog = false
                    locationSearchQuery = ""
                },
                title = { Text("जन्म स्थान छान्नुहोस् (Search Location)") },
                text = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = locationSearchQuery,
                            onValueChange = { locationSearchQuery = it },
                            placeholder = { Text("नेपालका जिल्ला, शहर वा विश्वका स्थानहरू...") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            shape = RoundedCornerShape(12.dp)
                        )

                        androidx.compose.foundation.lazy.LazyColumn(
                            modifier = Modifier.fillMaxWidth().heightIn(max = 350.dp)
                        ) {
                            items(filteredList, key = { it.name }) { loc ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedLocationName = loc.name
                                            latitude = loc.lat
                                            longitude = loc.lon
                                            timezoneOffset = loc.tzOffset
                                            latitudeStr = loc.lat.toString()
                                            longitudeStr = loc.lon.toString()
                                            timezoneOffsetStr = loc.tzOffset.toString()
                                            showLocationSearchDialog = false
                                            locationSearchQuery = ""
                                        }
                                        .padding(vertical = 10.dp, horizontal = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(selected = loc.name == selectedLocationName, onClick = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(loc.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                        Text("Lat: ${loc.lat}°, Lon: ${loc.lon}°, UTC+${loc.tzOffset}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        showLocationSearchDialog = false
                        locationSearchQuery = ""
                    }) {
                        Text("बन्द गर्नुहोस् (Close)")
                    }
                },
                shape = RoundedCornerShape(20.dp)
            )
        }
    }

private fun parseCoordinatesFromInput(input: String): Pair<Double, Double>? {
    val trimmed = input.trim()
    if (trimmed.isEmpty()) return null

    // Pattern 1: Google Maps URL @lat,lon
    val atMatch = Regex("""@(-?\d+\.\d+),(-?\d+\.\d+)""").find(trimmed)
    if (atMatch != null) {
        val lat = atMatch.groupValues[1].toDoubleOrNull()
        val lon = atMatch.groupValues[2].toDoubleOrNull()
        if (lat != null && lon != null) return Pair(lat, lon)
    }

    // Pattern 2: Google Maps URL q=lat,lon or ll=lat,lon
    val qMatch = Regex("""[?&](?:q|ll)=(-?\d+\.\d+),(-?\d+\.\d+)""").find(trimmed)
    if (qMatch != null) {
        val lat = qMatch.groupValues[1].toDoubleOrNull()
        val lon = qMatch.groupValues[2].toDoubleOrNull()
        if (lat != null && lon != null) return Pair(lat, lon)
    }

    // Pattern 3: Simple decimal lat, lon pair e.g. "27.7172, 85.3240" or "27.7172 85.3240"
    val pairMatch = Regex("""(-?\d+\.\d+)\s*[,;\s]\s*(-?\d+\.\d+)""").find(trimmed)
    if (pairMatch != null) {
        val lat = pairMatch.groupValues[1].toDoubleOrNull()
        val lon = pairMatch.groupValues[2].toDoubleOrNull()
        if (lat != null && lon != null) return Pair(lat, lon)
    }

    // Pattern 4: DMS format e.g. 27°43'02"N 85°19'26"E
    val dmsMatch = Regex("""(\d+)°(\d+)'?([\d.]*)"?\s*([NSns])\s*(\d+)°(\d+)'?([\d.]*)"?\s*([EWew])""").find(trimmed)
    if (dmsMatch != null) {
        val latDeg = dmsMatch.groupValues[1].toDoubleOrNull() ?: 0.0
        val latMin = dmsMatch.groupValues[2].toDoubleOrNull() ?: 0.0
        val latSec = dmsMatch.groupValues[3].toDoubleOrNull() ?: 0.0
        val latDir = dmsMatch.groupValues[4].uppercase()
        var lat = latDeg + (latMin / 60.0) + (latSec / 3600.0)
        if (latDir == "S") lat = -lat

        val lonDeg = dmsMatch.groupValues[5].toDoubleOrNull() ?: 0.0
        val lonMin = dmsMatch.groupValues[6].toDoubleOrNull() ?: 0.0
        val lonSec = dmsMatch.groupValues[7].toDoubleOrNull() ?: 0.0
        val lonDir = dmsMatch.groupValues[8].uppercase()
        var lon = lonDeg + (lonMin / 60.0) + (lonSec / 3600.0)
        if (lonDir == "W") lon = -lon

        return Pair(lat, lon)
    }

    return null
}

@Suppress("DEPRECATION")
private fun lookupAddressFromLatLon(context: android.content.Context, lat: Double, lon: Double): String {
    try {
        val geocoder = android.location.Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, lon, 1)
        if (!addresses.isNullOrEmpty()) {
            val addr = addresses[0]
            val city = addr.locality ?: addr.subAdminArea ?: addr.subLocality ?: addr.adminArea
            val country = addr.countryName
            return if (city != null && country != null) {
                "$city, $country"
            } else country ?: "Location ($lat, $lon)"
        }
    } catch (_: Exception) {}
    return "Location ($lat, $lon)"
}

private fun estimateTimezoneOffset(lat: Double, lon: Double): Double {
    if (lat in 26.0..31.0 && lon in 80.0..89.0) {
        return 5.75
    }
    if (lat in 6.0..36.0 && lon in 68.0..97.0) {
        return 5.5
    }
    var tz = (lon / 15.0)
    tz = (tz * 2.0).let { kotlin.math.round(it) / 2.0 }
    return tz
}
