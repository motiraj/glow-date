@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

import android.content.Context
import kotlinx.coroutines.flow.Flow

class UserPreferencesRepository(context: Context) {
    private val preferencesManager = PreferencesManager(context)

    val birthProfileFlow: Flow<BirthProfile?> = preferencesManager.birthProfileFlow
    val userZodiacFlow: Flow<String> = preferencesManager.userZodiacFlow
    val locationFlow: Flow<LocationData> = preferencesManager.locationFlow

    suspend fun saveBirthProfile(profile: BirthProfile) {
        preferencesManager.saveBirthProfile(profile)
    }

    suspend fun clearBirthProfile() {
        preferencesManager.clearBirthProfile()
    }

    suspend fun setBirthProfileEnabled(enabled: Boolean) {
        preferencesManager.setBirthProfileEnabled(enabled)
    }

    suspend fun setUserZodiac(zodiacId: String) {
        preferencesManager.setUserZodiac(zodiacId)
    }
}
