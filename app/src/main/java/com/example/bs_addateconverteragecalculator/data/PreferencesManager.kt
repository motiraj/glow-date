package com.example.bs_addateconverteragecalculator.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.bs_addateconverteragecalculator.widget.NepaliDateClockReceiver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferencesManager(private val context: Context) {
    companion object {
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        val DYNAMIC_COLOR_KEY = booleanPreferencesKey("dynamic_color")
        val LANGUAGE_KEY = stringPreferencesKey("language") // "ne", "en", "hi"
        val LOCATION_NAME_KEY = stringPreferencesKey("location_name")
        val LOCATION_LAT_KEY = doublePreferencesKey("location_lat")
        val LOCATION_LON_KEY = doublePreferencesKey("location_lon")
        val LOCATION_TZ_KEY = doublePreferencesKey("location_tz")
        val IS_AUTO_GPS_KEY = booleanPreferencesKey("is_auto_gps")
        val USER_ZODIAC_KEY = stringPreferencesKey("user_zodiac_sign")
        val CALENDAR_MONTH_OFFSET_KEY = intPreferencesKey("calendar_month_offset")
        val ABHIJIT_ALERT_KEY = booleanPreferencesKey("abhijit_alert_enabled")
        val RAHU_ALERT_KEY = booleanPreferencesKey("rahu_alert_enabled")
        val AYANAMSA_MODE_KEY = stringPreferencesKey("ayanamsa_mode")

        // Birth Profile Preferences
        val BIRTH_NAME_KEY = stringPreferencesKey("birth_name")
        val BIRTH_IS_BS_KEY = booleanPreferencesKey("birth_is_bs")
        val BIRTH_BS_YEAR_KEY = intPreferencesKey("birth_bs_year")
        val BIRTH_BS_MONTH_KEY = intPreferencesKey("birth_bs_month")
        val BIRTH_BS_DAY_KEY = intPreferencesKey("birth_bs_day")
        val BIRTH_AD_DATE_KEY = stringPreferencesKey("birth_ad_date")
        val BIRTH_HOUR_KEY = intPreferencesKey("birth_hour")
        val BIRTH_MINUTE_KEY = intPreferencesKey("birth_minute")
        val BIRTH_LOCATION_NAME_KEY = stringPreferencesKey("birth_location_name")
        val BIRTH_LAT_KEY = doublePreferencesKey("birth_lat")
        val BIRTH_LON_KEY = doublePreferencesKey("birth_lon")
        val BIRTH_TZ_KEY = doublePreferencesKey("birth_tz")
        val BIRTH_IS_ENABLED_KEY = booleanPreferencesKey("birth_is_enabled")
        val BIRTH_IS_SAVED_KEY = booleanPreferencesKey("birth_is_saved")
        val SAVED_PROFILES_JSON_KEY = stringPreferencesKey("saved_birth_profiles_json")
        val ACTIVE_PROFILE_ID_KEY = stringPreferencesKey("active_birth_profile_id")
        val PERSISTENT_NOTIFICATION_KEY = booleanPreferencesKey("persistent_nepali_date_notification")
        val EKADASHI_NOTIFICATION_KEY = booleanPreferencesKey("ekadashi_special_notification_enabled")
        val FESTIVAL_NOTIFICATION_KEY = booleanPreferencesKey("festival_special_notification_enabled")
    }

    val darkModeFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[DARK_MODE_KEY] ?: false
        }

    val dynamicColorFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[DYNAMIC_COLOR_KEY] ?: false
        }

    val languageFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[LANGUAGE_KEY] ?: "ne"
        }

    val userZodiacFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_ZODIAC_KEY] ?: "aries"
        }

    val locationFlow: Flow<LocationData> = context.dataStore.data
        .map { preferences ->
            val isAutoGps = preferences[IS_AUTO_GPS_KEY] ?: false
            if (isAutoGps) {
                val lat = preferences[LOCATION_LAT_KEY] ?: 27.7172
                val lon = preferences[LOCATION_LON_KEY] ?: 85.3240
                val tz = preferences[LOCATION_TZ_KEY] ?: 5.75
                val name = preferences[LOCATION_NAME_KEY] ?: "Current Device Location"
                LocationData(name, lat, lon, tz)
            } else {
                val name = preferences[LOCATION_NAME_KEY] ?: "Kathmandu, Nepal"
                GlobalLocation.getLocationByName(name)
            }
        }

    val isAutoGpsFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_AUTO_GPS_KEY] ?: false
        }

    val abhijitAlertFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[ABHIJIT_ALERT_KEY] ?: true
        }

    val rahuAlertFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[RAHU_ALERT_KEY] ?: true
        }

    val ayanamsaModeFlow: Flow<AyanamsaMode> = context.dataStore.data
        .map { preferences ->
            val modeStr = preferences[AYANAMSA_MODE_KEY] ?: "LAHIRI_DRIK"
            try {
                AyanamsaMode.valueOf(modeStr)
            } catch (_: Exception) {
                AyanamsaMode.LAHIRI_DRIK
            }
        }

    suspend fun setAyanamsaMode(mode: AyanamsaMode) {
        context.dataStore.edit { preferences ->
            preferences[AYANAMSA_MODE_KEY] = mode.name
        }
    }

    val persistentNotificationFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PERSISTENT_NOTIFICATION_KEY] ?: true
        }

    suspend fun setPersistentNotificationEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PERSISTENT_NOTIFICATION_KEY] = enabled
        }
    }

    val festivalNotificationFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[FESTIVAL_NOTIFICATION_KEY] ?: preferences[EKADASHI_NOTIFICATION_KEY] ?: true
        }

    val ekadashiNotificationFlow: Flow<Boolean> get() = festivalNotificationFlow

    suspend fun setFestivalNotificationEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[FESTIVAL_NOTIFICATION_KEY] = enabled
            preferences[EKADASHI_NOTIFICATION_KEY] = enabled
        }
    }

    suspend fun setEkadashiNotificationEnabled(enabled: Boolean) = setFestivalNotificationEnabled(enabled)

    suspend fun setAllNotificationsEnabled(enabled: Boolean = true) {
        context.dataStore.edit { preferences ->
            preferences[PERSISTENT_NOTIFICATION_KEY] = enabled
            preferences[FESTIVAL_NOTIFICATION_KEY] = enabled
            preferences[EKADASHI_NOTIFICATION_KEY] = enabled
            preferences[ABHIJIT_ALERT_KEY] = enabled
            preferences[RAHU_ALERT_KEY] = enabled
        }
    }

    private fun parseSavedProfiles(jsonStr: String?, preferences: androidx.datastore.preferences.core.Preferences): List<BirthProfile> {
        if (!jsonStr.isNullOrEmpty()) {
            try {
                val array = org.json.JSONArray(jsonStr)
                val list = mutableListOf<BirthProfile>()
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(obj.toBirthProfile())
                }
                if (list.isNotEmpty()) return list
            } catch (_: Exception) {}
        }

        val isSaved = preferences[BIRTH_IS_SAVED_KEY] ?: false
        return if (isSaved) {
            val legacyProfile = BirthProfile(
                id = "main_user_profile",
                name = preferences[BIRTH_NAME_KEY] ?: "तपाईँ",
                isBsInput = preferences[BIRTH_IS_BS_KEY] ?: true,
                bsYear = preferences[BIRTH_BS_YEAR_KEY] ?: 2058,
                bsMonth = preferences[BIRTH_BS_MONTH_KEY] ?: 1,
                bsDay = preferences[BIRTH_BS_DAY_KEY] ?: 1,
                adBirthDate = preferences[BIRTH_AD_DATE_KEY] ?: "2001-04-14",
                birthHour = preferences[BIRTH_HOUR_KEY] ?: 7,
                birthMinute = preferences[BIRTH_MINUTE_KEY] ?: 30,
                locationName = preferences[BIRTH_LOCATION_NAME_KEY] ?: "Kathmandu, Nepal",
                latitude = preferences[BIRTH_LAT_KEY] ?: 27.7172,
                longitude = preferences[BIRTH_LON_KEY] ?: 85.3240,
                timezoneOffset = preferences[BIRTH_TZ_KEY] ?: 5.75,
                isEnabled = preferences[BIRTH_IS_ENABLED_KEY] ?: true
            )
            listOf(legacyProfile)
        } else {
            emptyList()
        }
    }

    val savedBirthProfilesFlow: Flow<List<BirthProfile>> = context.dataStore.data
        .map { preferences ->
            parseSavedProfiles(preferences[SAVED_PROFILES_JSON_KEY], preferences)
        }

    val birthProfileFlow: Flow<BirthProfile?> = context.dataStore.data
        .map { preferences ->
            val activeId = preferences[ACTIVE_PROFILE_ID_KEY]
            val savedList = parseSavedProfiles(preferences[SAVED_PROFILES_JSON_KEY], preferences)
            if (savedList.isEmpty()) {
                null
            } else {
                savedList.find { it.id == activeId } ?: savedList.first()
            }
        }

    suspend fun saveBirthProfile(profile: BirthProfile) {
        context.dataStore.edit { preferences ->
            val currentList = parseSavedProfiles(preferences[SAVED_PROFILES_JSON_KEY], preferences).toMutableList()
            val existingIdx = currentList.indexOfFirst { it.id == profile.id }

            if (existingIdx != -1) {
                currentList[existingIdx] = profile
            } else {
                currentList.add(profile)
            }

            val array = org.json.JSONArray()
            currentList.forEach { array.put(it.toJsonObject()) }

            preferences[SAVED_PROFILES_JSON_KEY] = array.toString()
            preferences[ACTIVE_PROFILE_ID_KEY] = profile.id
            preferences[BIRTH_IS_SAVED_KEY] = true

            // Legacy syncing
            preferences[BIRTH_NAME_KEY] = profile.name
            preferences[BIRTH_IS_BS_KEY] = profile.isBsInput
            preferences[BIRTH_BS_YEAR_KEY] = profile.bsYear
            preferences[BIRTH_BS_MONTH_KEY] = profile.bsMonth
            preferences[BIRTH_BS_DAY_KEY] = profile.bsDay
            preferences[BIRTH_AD_DATE_KEY] = profile.adBirthDate
            preferences[BIRTH_HOUR_KEY] = profile.birthHour
            preferences[BIRTH_MINUTE_KEY] = profile.birthMinute
            preferences[BIRTH_LOCATION_NAME_KEY] = profile.locationName
            preferences[BIRTH_LAT_KEY] = profile.latitude
            preferences[BIRTH_LON_KEY] = profile.longitude
            preferences[BIRTH_TZ_KEY] = profile.timezoneOffset
            preferences[BIRTH_IS_ENABLED_KEY] = profile.isEnabled
        }
    }

    suspend fun selectActiveBirthProfile(profileId: String) {
        context.dataStore.edit { preferences ->
            preferences[ACTIVE_PROFILE_ID_KEY] = profileId
        }
    }

    suspend fun deleteBirthProfile(profileId: String) {
        context.dataStore.edit { preferences ->
            val currentList = parseSavedProfiles(preferences[SAVED_PROFILES_JSON_KEY], preferences).filter { it.id != profileId }
            val array = org.json.JSONArray()
            currentList.forEach { array.put(it.toJsonObject()) }

            preferences[SAVED_PROFILES_JSON_KEY] = array.toString()
            if (currentList.isNotEmpty()) {
                preferences[ACTIVE_PROFILE_ID_KEY] = currentList.first().id
            } else {
                preferences[BIRTH_IS_SAVED_KEY] = false
                preferences.remove(ACTIVE_PROFILE_ID_KEY)
            }
        }
    }

    suspend fun clearBirthProfile() {
        context.dataStore.edit { preferences ->
            preferences[BIRTH_IS_SAVED_KEY] = false
            preferences.remove(SAVED_PROFILES_JSON_KEY)
            preferences.remove(ACTIVE_PROFILE_ID_KEY)
            preferences.remove(BIRTH_NAME_KEY)
            preferences.remove(BIRTH_IS_BS_KEY)
            preferences.remove(BIRTH_BS_YEAR_KEY)
            preferences.remove(BIRTH_BS_MONTH_KEY)
            preferences.remove(BIRTH_BS_DAY_KEY)
            preferences.remove(BIRTH_AD_DATE_KEY)
            preferences.remove(BIRTH_HOUR_KEY)
            preferences.remove(BIRTH_MINUTE_KEY)
            preferences.remove(BIRTH_LOCATION_NAME_KEY)
            preferences.remove(BIRTH_LAT_KEY)
            preferences.remove(BIRTH_LON_KEY)
            preferences.remove(BIRTH_TZ_KEY)
            preferences.remove(BIRTH_IS_ENABLED_KEY)
        }
    }

    suspend fun setBirthProfileEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[BIRTH_IS_ENABLED_KEY] = enabled
        }
    }

    suspend fun setDarkMode(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = isDark
        }
    }

    suspend fun setDynamicColor(isEnabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DYNAMIC_COLOR_KEY] = isEnabled
        }
    }

    suspend fun setLanguage(lang: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = lang
        }
    }

    suspend fun setUserZodiac(zodiacId: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ZODIAC_KEY] = zodiacId
        }
        NepaliDateClockReceiver.updateAllWidgets(context)
    }

    suspend fun setLocation(locationName: String) {
        context.dataStore.edit { preferences ->
            preferences[IS_AUTO_GPS_KEY] = false
            preferences[LOCATION_NAME_KEY] = locationName
        }
    }

    suspend fun setAutoGpsLocation(name: String, lat: Double, lon: Double, tzOffset: Double) {
        context.dataStore.edit { preferences ->
            preferences[IS_AUTO_GPS_KEY] = true
            preferences[LOCATION_NAME_KEY] = name
            preferences[LOCATION_LAT_KEY] = lat
            preferences[LOCATION_LON_KEY] = lon
            preferences[LOCATION_TZ_KEY] = tzOffset
        }
    }

    suspend fun setAutoGpsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_AUTO_GPS_KEY] = enabled
        }
    }

    suspend fun setAbhijitAlertEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ABHIJIT_ALERT_KEY] = enabled
        }
    }

    suspend fun setRahuAlertEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[RAHU_ALERT_KEY] = enabled
        }
    }
}

private fun BirthProfile.toJsonObject(): org.json.JSONObject {
    val obj = org.json.JSONObject()
    obj.put("id", id)
    obj.put("name", name)
    obj.put("gender", gender.name)
    obj.put("isBsInput", isBsInput)
    obj.put("bsYear", bsYear)
    obj.put("bsMonth", bsMonth)
    obj.put("bsDay", bsDay)
    obj.put("adBirthDate", adBirthDate)
    obj.put("birthHour", birthHour)
    obj.put("birthMinute", birthMinute)
    obj.put("locationName", locationName)
    obj.put("latitude", latitude)
    obj.put("longitude", longitude)
    obj.put("timezoneOffset", timezoneOffset)
    obj.put("isEnabled", isEnabled)
    return obj
}

private fun org.json.JSONObject.toBirthProfile(): BirthProfile {
    val genderEnum = try {
        UserGender.valueOf(optString("gender", "UNSPECIFIED"))
    } catch (_: Exception) {
        UserGender.UNSPECIFIED
    }
    return BirthProfile(
        id = optString("id", java.util.UUID.randomUUID().toString()),
        name = optString("name", ""),
        gender = genderEnum,
        isBsInput = optBoolean("isBsInput", true),
        bsYear = optInt("bsYear", 2058),
        bsMonth = optInt("bsMonth", 1),
        bsDay = optInt("bsDay", 1),
        adBirthDate = optString("adBirthDate", "2001-04-14"),
        birthHour = optInt("birthHour", 7),
        birthMinute = optInt("birthMinute", 30),
        locationName = optString("locationName", "Kathmandu, Nepal"),
        latitude = optDouble("latitude", 27.7172),
        longitude = optDouble("longitude", 85.3240),
        timezoneOffset = optDouble("timezoneOffset", 5.75),
        isEnabled = optBoolean("isEnabled", true)
    )
}

