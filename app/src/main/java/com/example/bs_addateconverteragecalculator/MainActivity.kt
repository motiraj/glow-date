@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bs_addateconverteragecalculator.data.PreferencesManager
import com.example.bs_addateconverteragecalculator.ui.calendar.CalendarScreen
import com.example.bs_addateconverteragecalculator.ui.cheena.CheenaPatrikaScreen
import com.example.bs_addateconverteragecalculator.ui.components.AdMobBanner
import com.example.bs_addateconverteragecalculator.ui.components.MainNavigation
import com.example.bs_addateconverteragecalculator.ui.components.GlowAppBar
import com.example.bs_addateconverteragecalculator.ui.components.Screen
import com.example.bs_addateconverteragecalculator.ui.converter.ConverterScreen
import com.example.bs_addateconverteragecalculator.ui.panchanga.PanchangaScreen
import com.example.bs_addateconverteragecalculator.ui.rashifal.RashifalScreen
import com.example.bs_addateconverteragecalculator.ui.settings.AboutAppScreen
import com.example.bs_addateconverteragecalculator.ui.settings.LanguageSettingsScreen
import com.example.bs_addateconverteragecalculator.ui.settings.SettingsScreen
import com.example.bs_addateconverteragecalculator.ui.theme.BSADDateConverterAgeCalculatorTheme
import com.example.bs_addateconverteragecalculator.utils.LocalAppLanguage
import com.example.bs_addateconverteragecalculator.utils.LocalIsNepali
import com.example.bs_addateconverteragecalculator.widget.NepaliDateClockReceiver
import com.example.bs_addateconverteragecalculator.worker.FestivalNotificationHelper
import com.example.bs_addateconverteragecalculator.worker.NotificationHelper
import com.example.bs_addateconverteragecalculator.worker.NotificationScheduler
import com.example.bs_addateconverteragecalculator.worker.VedicTimingNotificationScheduler
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            initializeAndScheduleAllNotifications()
            NotificationHelper.showPersistentDateNotification(applicationContext)
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                initializeAndScheduleAllNotifications()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            initializeAndScheduleAllNotifications()
        }
    }

    private fun initializeAndScheduleAllNotifications() {
        lifecycleScope.launch(Dispatchers.IO) {
            val appCtx = applicationContext
            // 1. Create all notification channels
            NotificationHelper.createNotificationChannel(appCtx)
            FestivalNotificationHelper.createNotificationChannel(appCtx)
            com.example.bs_addateconverteragecalculator.worker.VedicTimingNotificationWorker.createNotificationChannels(appCtx)

            // 2. Schedule all notification alarms and background workers
            NotificationHelper.scheduleDailyNotification(appCtx)
            NotificationScheduler.scheduleDailyVedicNotification(appCtx)
            VedicTimingNotificationScheduler.scheduleDailyTimingAlarms(appCtx)
            FestivalNotificationHelper.scheduleFestivalAlarms(appCtx)
            FestivalNotificationHelper.checkAndNotifyFestival(appCtx)
        }
    }

    override fun onResume() {
        super.onResume()
        // Synchronize all home screen widgets with current app running date, time, and user-selected Rashi
        NepaliDateClockReceiver.updateAllWidgets(applicationContext)
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        // Officially handles Android 12+ OS-level splash screen natively
        val splashScreen = installSplashScreen()
        
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val routeFromIntent = intent.getStringExtra("EXTRA_DESTINATION") ?: intent.getStringExtra("route")
        val preferencesManager = PreferencesManager(applicationContext)

        // Initialize all notification channels and schedules
        initializeAndScheduleAllNotifications()

        // Initialize AdMob asynchronously
        lifecycleScope.launch(Dispatchers.IO) {
            MobileAds.initialize(applicationContext) {}
        }

        askNotificationPermission()

        setContent {
            val systemDark = isSystemInDarkTheme()
            
            // Collect flow state synchronously
            val darkModeState by preferencesManager.darkModeFlow.collectAsState(initial = systemDark)
            val dynamicColorState by preferencesManager.dynamicColorFlow.collectAsState(initial = false)
            val languageState by preferencesManager.languageFlow.collectAsState(initial = "ne")
            
            val locationData by preferencesManager.locationFlow.collectAsState(initial = null)
            val locationName = locationData?.name ?: "Kathmandu, Nepal"

            var isSplashVisible by remember { mutableStateOf(true) }
            var isReady by remember { mutableStateOf(false) }

            // Hold native splash screen until Compose frame 1 is ready
            splashScreen.setKeepOnScreenCondition { !isReady }

            LaunchedEffect(Unit) {
                isReady = true
                val prefs = PreferencesManager(applicationContext)
                prefs.persistentNotificationFlow.collect { enabled ->
                    if (enabled) {
                        NotificationHelper.showPersistentDateNotification(applicationContext)
                    } else {
                        NotificationHelper.cancelPersistentDateNotification(applicationContext)
                    }
                }
            }

            // Show animated Compose splash screen
            if (isSplashVisible) {
                com.example.bs_addateconverteragecalculator.ui.components.GlowDateSplashScreen(
                    onTimeout = { isSplashVisible = false }
                )
                return@setContent
            }

            val currentLang = languageState
            val isNepali = currentLang == "ne" || currentLang == "hi"

            CompositionLocalProvider(
                LocalAppLanguage provides currentLang,
                LocalIsNepali provides isNepali
            ) {
                BSADDateConverterAgeCalculatorTheme(
                    darkTheme = darkModeState,
                    dynamicColor = dynamicColorState
                ) {
                    val windowSizeClass = calculateWindowSizeClass(this@MainActivity)
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Panchanga.route

                    // Handle deep link routing from widgets and notifications
                    LaunchedEffect(routeFromIntent) {
                        if (routeFromIntent != null) {
                            val targetRoute = when (routeFromIntent) {
                                "rashifal" -> Screen.Rashifal.route
                                "calendar" -> Screen.Calendar.route
                                "converter" -> Screen.Converter.route
                                else -> Screen.Panchanga.route
                            }
                            navController.navigate(targetRoute) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }

                    val isSubScreen = currentRoute == "settings" || currentRoute == "language_settings" || currentRoute == "about_app" || currentRoute == "cheena_patrika"

                    val title = when (currentRoute) {
                        Screen.Panchanga.route -> if (isNepali) "ग्लोडेट — आजको पञ्चाङ्ग" else "GlowDate — Panchanga"
                        Screen.Converter.route -> if (isNepali) "ग्लोडेट — रूपान्तरण तथा उमेर" else "GlowDate — Converter & Age"
                        Screen.Calendar.route -> if (isNepali) "ग्लोडेट — नेपाली पात्रो" else "GlowDate — Nepali Calendar"
                        Screen.Rashifal.route -> if (isNepali) "ग्लोडेट — राशिफल" else "GlowDate — Rashifal"
                        "cheena_patrika" -> if (isNepali) "ग्लोडेट — चिना पत्रिका" else "GlowDate — Cheena Patrika"
                        "settings" -> if (isNepali) "सेटिङहरू" else "Settings"
                        else -> "GlowDate"
                    }

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            if (!isSubScreen) {
                                GlowAppBar(
                                    title = title,
                                    locationName = locationName,
                                    onSettingsClick = { navController.navigate("settings") }
                                )
                            }
                        },
                        bottomBar = {
                            if (!isSubScreen && windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                                Column {
                                    AdMobBanner()
                                    MainNavigation(
                                        currentRoute = currentRoute,
                                        onItemClick = { screen ->
                                            if (currentRoute != screen.route) {
                                                navController.navigate(screen.route) {
                                                    popUpTo(navController.graph.startDestinationId) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                        },
                                        windowWidthSizeClass = windowSizeClass.widthSizeClass
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        Row(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                            if (!isSubScreen && windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact) {
                                Column {
                                    MainNavigation(
                                        currentRoute = currentRoute,
                                        onItemClick = { screen ->
                                            if (currentRoute != screen.route) {
                                                navController.navigate(screen.route) {
                                                    popUpTo(navController.graph.startDestinationId) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                        },
                                        windowWidthSizeClass = windowSizeClass.widthSizeClass
                                    )
                                }
                            }
                            
                            NavHost(
                                navController = navController,
                                startDestination = Screen.Panchanga.route,
                                modifier = Modifier.weight(1f)
                            ) {
                            composable(Screen.Panchanga.route) { 
                                PanchangaScreen(
                                    onNavigateToRashifal = {
                                        navController.navigate(Screen.Rashifal.route) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                ) 
                            }
                            composable(Screen.Converter.route) { ConverterScreen() }
                            composable(Screen.Calendar.route) { CalendarScreen() }
                            composable(Screen.Rashifal.route) {
                                RashifalScreen(
                                    onNavigateToCheena = {
                                        navController.navigate("cheena_patrika")
                                    }
                                )
                            }
                            composable("cheena_patrika") {
                                CheenaPatrikaScreen(
                                    onBackClick = { navController.popBackStack() }
                                )
                            }
                            composable("settings") {
                                SettingsScreen(
                                    onBackClick = { navController.popBackStack() },
                                    onLanguageClick = { navController.navigate("language_settings") },
                                    onAboutClick = { navController.navigate("about_app") }
                                )
                            }
                            composable("language_settings") {
                                LanguageSettingsScreen(
                                    onBackClick = { navController.popBackStack() }
                                )
                            }
                            composable("about_app") {
                                AboutAppScreen(
                                    onBackClick = { navController.popBackStack() }
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
