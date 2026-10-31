@file:Suppress("NewApi", "SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.ui.rashifal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bs_addateconverteragecalculator.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

enum class RashifalType {
    DAILY, WEEKLY, MONTHLY, YEARLY
}

data class RashifalUiState(
    val isPersonalized: Boolean = false,
    val savedProfiles: List<BirthProfile> = emptyList(),
    val birthProfile: BirthProfile? = null,
    val natalProfile: NatalProfile? = null,
    val transitAnalysis: TransitAnalysis? = null,
    val personalizedPrediction: PersonalizedPrediction? = null,
    val multiTimeframeOutput: PersonalizedRashifalOutput? = null,
    val selectedSign: ZodiacSign = RashifalData.signs.first(),
    val selectedType: RashifalType = RashifalType.DAILY,
    val selectedMonth: Int = 6, // Ashwin
    val selectedYear: Int = 2083,
    val prediction: RashifalPrediction = RashifalData.getPrediction(RashifalData.signs.first().id),
    val currentLang: String = "ne"
)

class RashifalViewModel(application: Application) : AndroidViewModel(application) {
    private val preferencesManager = PreferencesManager(application)
    private val _uiState = MutableStateFlow(RashifalUiState())
    val uiState: StateFlow<RashifalUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesManager.birthProfileFlow.collect { profile ->
                if (profile != null && profile.isEnabled) {
                    val birthAdDate = try {
                        LocalDate.parse(profile.adBirthDate)
                    } catch (_: Exception) {
                        LocalDate.of(2001, 4, 14)
                    }
                    val birthTime = LocalTime.of(profile.birthHour, profile.birthMinute)

                    val natal = JyotishAstroEngine.calculateNatalProfile(
                        birthAdDate = birthAdDate,
                        birthTime = birthTime,
                        lat = profile.latitude,
                        lon = profile.longitude,
                        tzOffset = profile.timezoneOffset
                    )

                    val today = LocalDate.now()
                    val transit = JyotishAstroEngine.analyzeTransit(
                        natalProfile = natal,
                        todayAdDate = today,
                        todayTime = LocalTime.of(6, 0),
                        lat = profile.latitude,
                        lon = profile.longitude,
                        tzOffset = profile.timezoneOffset
                    )

                    val personalizedPred = PersonalizedRashifalGenerator.generatePersonalizedPrediction(
                        natalProfile = natal,
                        transitAnalysis = transit
                    )

                    val timeframeOut = computeTimeframeOutput(_uiState.value.selectedType, profile, natal)

                    val sign = RashifalData.signs.getOrNull(natal.janmaRashiIndex) ?: RashifalData.signs.first()
                    val dynamicPred = getPredictionForSign(sign.id)

                    _uiState.value = _uiState.value.copy(
                        isPersonalized = true,
                        birthProfile = profile,
                        natalProfile = natal,
                        transitAnalysis = transit,
                        personalizedPrediction = personalizedPred,
                        multiTimeframeOutput = timeframeOut,
                        selectedSign = sign,
                        prediction = dynamicPred
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isPersonalized = false,
                        birthProfile = profile,
                        natalProfile = null,
                        transitAnalysis = null,
                        personalizedPrediction = null,
                        multiTimeframeOutput = null
                    )
                }
            }
        }

        viewModelScope.launch {
            preferencesManager.userZodiacFlow.collect { zodiacId ->
                if (!_uiState.value.isPersonalized) {
                    val sign = RashifalData.signs.find { it.id == zodiacId } ?: RashifalData.signs.first()
                    val dynamicPred = getPredictionForSign(sign.id)
                    _uiState.value = _uiState.value.copy(
                        selectedSign = sign,
                        prediction = dynamicPred
                    )
                }
            }
        }

        viewModelScope.launch {
            preferencesManager.languageFlow.collect { lang ->
                refreshAllForLanguage(lang)
            }
        }

        viewModelScope.launch {
            preferencesManager.savedBirthProfilesFlow.collect { savedList ->
                _uiState.value = _uiState.value.copy(savedProfiles = savedList)
            }
        }
    }

    private fun refreshAllForLanguage(lang: String) {
        val currentSign = _uiState.value.selectedSign
        val profile = _uiState.value.birthProfile
        val natal = _uiState.value.natalProfile

        val dynamicPred = getPredictionForSign(currentSign.id, lang)

        if (profile != null && profile.isEnabled && natal != null) {
            val today = LocalDate.now()
            val transit = JyotishAstroEngine.analyzeTransit(
                natalProfile = natal,
                todayAdDate = today,
                todayTime = LocalTime.of(6, 0),
                lat = profile.latitude,
                lon = profile.longitude,
                tzOffset = profile.timezoneOffset
            )

            val personalizedPred = PersonalizedRashifalGenerator.generatePersonalizedPrediction(
                natalProfile = natal,
                transitAnalysis = transit,
                lang = lang
            )

            val timeframeOut = computeTimeframeOutput(_uiState.value.selectedType, profile, natal)

            _uiState.value = _uiState.value.copy(
                prediction = dynamicPred,
                personalizedPrediction = personalizedPred,
                multiTimeframeOutput = timeframeOut
            )
        } else {
            _uiState.value = _uiState.value.copy(
                prediction = dynamicPred
            )
        }
    }

    private fun computeTimeframeOutput(
        type: RashifalType,
        profile: BirthProfile?,
        natal: NatalProfile?
    ): PersonalizedRashifalOutput? {
        if (profile == null || natal == null) return null
        val birthAdDate = try { LocalDate.parse(profile.adBirthDate) } catch (_: Exception) { LocalDate.of(2001, 4, 14) }
        val birthTime = LocalTime.of(profile.birthHour, profile.birthMinute)
        val birthPos = AstroCalculator.getPositions(birthAdDate, birthTime, profile.timezoneOffset)
        val yearsElapsed = (ChronoUnit.DAYS.between(birthAdDate, LocalDate.now()) / 365.25).coerceAtLeast(0.1)

        val birthInput = BirthInput(
            adYear = birthAdDate.year,
            adMonth = birthAdDate.monthValue,
            adDay = birthAdDate.dayOfMonth,
            hour = profile.birthHour,
            minute = profile.birthMinute,
            latitude = profile.latitude,
            longitude = profile.longitude,
            timezoneOffsetHours = profile.timezoneOffset
        )

        val natalChart = MultiTimeframeRashifalEngine.computeNatalProfile(
            birth = birthInput,
            birthMoonLon = birthPos.moonLongitude,
            birthSunLon = birthPos.sunLongitude,
            yearsElapsedSinceBirth = yearsElapsed
        )

        return when (type) {
            RashifalType.DAILY -> {
                val todaySnapshot = MultiTimeframeRashifalEngine.getPlanetarySnapshot(LocalDate.now(), profile.timezoneOffset)
                MultiTimeframeRashifalEngine.generateDailyRashifal(natalChart, todaySnapshot)
            }
            RashifalType.WEEKLY -> {
                val today = LocalDate.now()
                val day1 = MultiTimeframeRashifalEngine.getPlanetarySnapshot(today, profile.timezoneOffset)
                val day4 = MultiTimeframeRashifalEngine.getPlanetarySnapshot(today.plusDays(3), profile.timezoneOffset)
                val day7 = MultiTimeframeRashifalEngine.getPlanetarySnapshot(today.plusDays(6), profile.timezoneOffset)
                MultiTimeframeRashifalEngine.generateWeeklyRashifal(natalChart, day1, day4, day7)
            }
            RashifalType.MONTHLY -> {
                val today = LocalDate.now()
                val monthSnap = MultiTimeframeRashifalEngine.getPlanetarySnapshot(today, profile.timezoneOffset)
                MultiTimeframeRashifalEngine.generateMonthlyRashifal(natalChart, monthSnap)
            }
            RashifalType.YEARLY -> {
                val today = LocalDate.now()
                val yearSnap = MultiTimeframeRashifalEngine.getPlanetarySnapshot(today, profile.timezoneOffset)
                MultiTimeframeRashifalEngine.generateYearlyRashifal(natalChart, yearSnap)
            }
        }
    }

    fun saveBirthProfile(profile: BirthProfile) {
        viewModelScope.launch {
            preferencesManager.saveBirthProfile(profile)
        }
    }

    fun selectActiveProfile(profileId: String) {
        viewModelScope.launch {
            preferencesManager.selectActiveBirthProfile(profileId)
        }
    }

    fun deleteProfile(profileId: String) {
        viewModelScope.launch {
            preferencesManager.deleteBirthProfile(profileId)
        }
    }

    fun clearBirthProfile() {
        viewModelScope.launch {
            preferencesManager.clearBirthProfile()
        }
    }

    fun togglePersonalized(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setBirthProfileEnabled(enabled)
        }
    }

    fun selectSign(sign: ZodiacSign) {
        val dynamicPred = getPredictionForSign(sign.id)
        _uiState.value = _uiState.value.copy(
            selectedSign = sign,
            prediction = dynamicPred
        )
        viewModelScope.launch {
            preferencesManager.setUserZodiac(sign.id)
        }
    }

    fun selectType(type: RashifalType) {
        val timeframeOut = computeTimeframeOutput(type, _uiState.value.birthProfile, _uiState.value.natalProfile)
        _uiState.value = _uiState.value.copy(
            selectedType = type,
            multiTimeframeOutput = timeframeOut
        )
    }

    fun selectMonth(month: Int) {
        val dynamicPred = getPredictionForSign(_uiState.value.selectedSign.id)
        _uiState.value = _uiState.value.copy(
            selectedMonth = month,
            prediction = dynamicPred
        )
    }

    fun selectYear(year: Int) {
        val dynamicPred = getPredictionForSign(_uiState.value.selectedSign.id)
        _uiState.value = _uiState.value.copy(
            selectedYear = year,
            prediction = dynamicPred
        )
    }

    fun getPredictionForSign(signId: String, lang: String = "ne"): RashifalPrediction {
        val today = LocalDate.now()
        val base = RashifalData.getPrediction(signId, lang)
        val daily = RealTimeRashifalGenerator.generateDailyPrediction(signId, today, lang)
        val weekly = RealTimeRashifalGenerator.generateWeeklyPrediction(signId, today, lang)
        val monthly = RealTimeRashifalGenerator.generateMonthlyPrediction(signId, _uiState.value.selectedYear, _uiState.value.selectedMonth, lang)
        val yearly = RealTimeRashifalGenerator.generateYearlyPrediction(signId, _uiState.value.selectedYear, lang)

        return base.copy(
            daily = daily,
            weekly = weekly,
            monthly = monthly,
            yearly = yearly
        )
    }
}
