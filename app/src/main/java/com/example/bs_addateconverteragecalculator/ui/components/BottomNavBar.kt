package com.example.bs_addateconverteragecalculator.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.bs_addateconverteragecalculator.utils.AppStrings
import com.example.bs_addateconverteragecalculator.utils.LocalAppLanguage

sealed class Screen(val route: String, val titleKey: String, val icon: ImageVector) {
    object Panchanga : Screen("panchanga", "tab_panchanga", Icons.Default.WbSunny)
    object Rashifal : Screen("rashifal", "tab_rashifal", Icons.Default.AutoAwesome)
    object Calendar : Screen("calendar", "tab_calendar", Icons.Default.DateRange)
    object Converter : Screen("converter", "tab_converter", Icons.Default.Refresh)
}

val MainScreens = listOf(
    Screen.Panchanga,
    Screen.Calendar,
    Screen.Rashifal,
    Screen.Converter
)

@Composable
fun MainNavigation(
    currentRoute: String,
    onItemClick: (Screen) -> Unit,
    windowWidthSizeClass: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val lang = LocalAppLanguage.current

    if (windowWidthSizeClass == WindowWidthSizeClass.Compact) {
        // Bottom Navigation for phones
        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = modifier
        ) {
            NavigationBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                MainScreens.forEach { screen ->
                    val isSelected = currentRoute == screen.route
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { onItemClick(screen) },
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = AppStrings.get(screen.titleKey, lang)
                            )
                        },
                        label = {
                            Text(
                                text = AppStrings.get(screen.titleKey, lang),
                                maxLines = 1
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            selectedTextColor = MaterialTheme.colorScheme.onSurface,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        alwaysShowLabel = true
                    )
                }
            }
        }
    } else {
        // Navigation Rail for tablets/foldables (Medium and Expanded)
        NavigationRail(
            modifier = modifier.fillMaxHeight(),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ) {
            Spacer(Modifier.weight(1f))
            MainScreens.forEach { screen ->
                val isSelected = currentRoute == screen.route
                NavigationRailItem(
                    selected = isSelected,
                    onClick = { onItemClick(screen) },
                    icon = {
                        Icon(
                            imageVector = screen.icon,
                            contentDescription = AppStrings.get(screen.titleKey, lang)
                        )
                    },
                    label = {
                        Text(
                            text = AppStrings.get(screen.titleKey, lang),
                            maxLines = 1
                        )
                    },
                    colors = NavigationRailItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        selectedTextColor = MaterialTheme.colorScheme.onSurface,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    alwaysShowLabel = true
                )
            }
            Spacer(Modifier.weight(1f))
        }
    }
}
