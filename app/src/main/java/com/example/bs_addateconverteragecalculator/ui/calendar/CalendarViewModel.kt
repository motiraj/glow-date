@file:Suppress("NewApi", "SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.ui.calendar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bs_addateconverteragecalculator.converter.DateConverterEngine
import com.example.bs_addateconverteragecalculator.data.AstroCalculator
import com.example.bs_addateconverteragecalculator.data.AyanamsaMode
import com.example.bs_addateconverteragecalculator.data.BarvelaCalculator
import com.example.bs_addateconverteragecalculator.data.BarvelaSlot
import com.example.bs_addateconverteragecalculator.data.BsCalendarData
import com.example.bs_addateconverteragecalculator.data.DetailedGrahaGochar
import com.example.bs_addateconverteragecalculator.data.GhadiPalaCalculator
import com.example.bs_addateconverteragecalculator.data.GlobalLocation
import com.example.bs_addateconverteragecalculator.data.Holiday
import com.example.bs_addateconverteragecalculator.data.HolidayData
import com.example.bs_addateconverteragecalculator.data.HoraCalculator
import com.example.bs_addateconverteragecalculator.data.LocationData
import com.example.bs_addateconverteragecalculator.data.MuhuratTiming
import com.example.bs_addateconverteragecalculator.data.NakshatraYogaCalculator
import com.example.bs_addateconverteragecalculator.data.NepalSambatCalculator
import com.example.bs_addateconverteragecalculator.data.NepaliDate
import com.example.bs_addateconverteragecalculator.data.NepaliDays
import com.example.bs_addateconverteragecalculator.data.NepaliMonths
import com.example.bs_addateconverteragecalculator.data.NepaliNumerals
import com.example.bs_addateconverteragecalculator.data.PanchangaCalculator
import com.example.bs_addateconverteragecalculator.data.PreferencesManager
import com.example.bs_addateconverteragecalculator.data.GrahaTransit
import com.example.bs_addateconverteragecalculator.data.PlanetaryTransitUiState
import kotlinx.collections.immutable.toPersistentList
import com.example.bs_addateconverteragecalculator.data.ShraddhaCalculator
import com.example.bs_addateconverteragecalculator.data.TithiCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

data class CalendarDayInfo(
    val bsDay: Int,
    val isHoliday: Boolean,
    val isSaturday: Boolean,
    val tithi: String,
    val adDay: String
)

private val defaultInitialBs: NepaliDate by lazy {
    try {
        DateConverterEngine.convertAdToBs(LocalDate.now())
    } catch (_: Exception) {
        NepaliDate(LocalDate.now().year + 57, 1, 1)
    }
}

data class CalendarUiState(
    val currentBsYear: Int = defaultInitialBs.year,
    val currentBsMonth: Int = defaultInitialBs.month,
    val daysInMonth: Int = 31,
    val firstDayOfWeekOffset: Int = 0,
    val holidaysInMonth: List<Holiday> = emptyList(),
    val daysInfo: List<CalendarDayInfo> = emptyList(),
    val selectedDay: Int? = null,
    val selectedAdDateObj: LocalDate? = null,
    val selectedDayBsText: String = "",
    val selectedDayAdText: String = "",
    val selectedDayNsText: String = "",
    val selectedDayDayText: String = "",
    val selectedDayAdDate: String? = null,
    val selectedDayHoliday: Holiday? = null,
    val selectedDayGhadiPalaText: String = "० घडी ० पला",
    val panchangaTimeText: String = "",
    val panchangaBarvelaText: String = "",
    val panchangaSunriseText: String = "",
    val panchangaSunsetText: String = "",
    val barvelaSchedule: List<BarvelaSlot> = emptyList(),
    val horaSchedule: List<BarvelaSlot> = emptyList(),
    val sorhaShraddhaInfo: String? = null,
    
    // Dynamic Panchanga Transition Fields
    val paksha: String = "",
    val tithiFullDetails: String = "",
    val nakshatraFullDetails: String = "",
    val yogaFullDetails: String = "",
    val karanaFullDetails: String = "",
    
    // Planetary Transits (Gochar)
    val gochar: List<Pair<String, String>> = emptyList(),
    val detailedGochar: List<DetailedGrahaGochar> = emptyList(),

    @Suppress("SpellCheckingInspection")
    val rahuAndAbhijit: List<MuhuratTiming> = emptyList(),

    // Status
    val isLoading: Boolean = true
)

class CalendarViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    private val _planetaryTransitUiState = MutableStateFlow(PlanetaryTransitUiState())
    val planetaryTransitUiState: StateFlow<PlanetaryTransitUiState> = _planetaryTransitUiState.asStateFlow()

    private val preferencesManager = PreferencesManager(application)
    private var currentLocation: LocationData = GlobalLocation.locations.first()
    private var currentAyanamsaMode: AyanamsaMode = AyanamsaMode.LAHIRI_DRIK

    private var lastTrackedDate = LocalDate.now()
    private var isFirstLoad = true

    private fun updatePlanetaryTransits(adDate: LocalDate) {
        val tz = currentLocation.tzOffset
        val detailed = AstroCalculator.getDetailedGochar(adDate, LocalTime.now(), tz, currentAyanamsaMode)
        val list = detailed.map { g ->
            GrahaTransit(
                id = g.id,
                nameDevanagari = g.grahaName,
                rashiName = g.rashiName,
                formattedDegree = g.degreesText,
                isRetrograde = g.isRetrograde,
                isCombust = g.isCombust
            )
        }.toPersistentList()

        val ayanamsaNameStr = when (currentAyanamsaMode) {
            AyanamsaMode.LAHIRI_DRIK -> "चित्रापक्ष (लहिरी) - Modern Drik अयनांश"
            AyanamsaMode.SURYA_SIDDHANTA_TRADITIONAL -> "सूर्य सिद्धान्त - Traditional अयनांश"
        }

        _planetaryTransitUiState.value = PlanetaryTransitUiState(
            isLoading = false,
            ayanamshaName = ayanamsaNameStr,
            planets = list
        )
    }

    init {
        viewModelScope.launch(Dispatchers.Default) {
            preferencesManager.ayanamsaModeFlow.collect { mode ->
                currentAyanamsaMode = mode
                if (!isFirstLoad) {
                    val day = _uiState.value.selectedDay
                    if (day != null) {
                        selectDay(day)
                    }
                }
            }
        }

        viewModelScope.launch(Dispatchers.Default) {
            preferencesManager.locationFlow.collect { locationData ->
                currentLocation = locationData
                if (!isFirstLoad) {
                    val day = _uiState.value.selectedDay
                    if (day != null) {
                        selectDay(day)
                    }
                }
            }
        }

        viewModelScope.launch(Dispatchers.Default) {
            selectToday()
            isFirstLoad = false
        }

        viewModelScope.launch(Dispatchers.Default) {
            flow {
                while (true) {
                    emit(Unit)
                    delay(30.seconds)
                }
            }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = Unit
                )
                .collect {
                    tickRealTimeData()
                }
        }
    }

    fun selectToday() {
        viewModelScope.launch(Dispatchers.Default) {
            val todayAd = LocalDate.now()
            val todayBs = try {
                DateConverterEngine.convertAdToBs(todayAd)
            } catch (_: Exception) {
                NepaliDate(todayAd.year + 57, 1, 1)
            }
            updateMonth(todayBs.year, todayBs.month, todayBs.day)
        }
    }

    fun checkAndRefreshIfDateChanged() {
        val today = LocalDate.now()
        val selectedAd = _uiState.value.selectedAdDateObj

        if (today != lastTrackedDate || selectedAd == null) {
            lastTrackedDate = today
            selectToday()
        }
    }

    private fun tickRealTimeData() {
        val today = LocalDate.now()

        if (today != lastTrackedDate) {
            checkAndRefreshIfDateChanged()
            return
        }

        val adDate = _uiState.value.selectedAdDateObj ?: return
        val now = LocalTime.now()
        val lat = currentLocation.lat
        val lon = currentLocation.lon
        val tz = currentLocation.tzOffset

        val timeText = PanchangaCalculator.getCurrentTimeNepali()
        val ghadiPala = GhadiPalaCalculator.getCurrentGhadiPala()

        val (sunriseMins, sunsetMins) = PanchangaCalculator.getSunriseSunsetMinutes(adDate, lat, lon, tz)

        val barvelaSchedule = BarvelaCalculator.getBarvelaSchedule(adDate.dayOfWeek, now.hour, now.minute, sunriseMins, sunsetMins)
        val horaSchedule = HoraCalculator.getHoraSchedule(adDate.dayOfWeek, now.hour, now.minute, sunriseMins, sunsetMins)
        val currentBarvela = barvelaSchedule.firstOrNull { it.isCurrent }?.nameNepali ?: ""

        _uiState.value = _uiState.value.copy(
            panchangaTimeText = timeText,
            selectedDayGhadiPalaText = ghadiPala,
            barvelaSchedule = barvelaSchedule,
            horaSchedule = horaSchedule,
            panchangaBarvelaText = currentBarvela
        )
    }

    fun updateMonth(year: Int, month: Int, dayToSelect: Int? = null) {
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val days = BsCalendarData.getDaysInMonth(year, month)
            val holidays = HolidayData.getHolidaysForMonth(year, month)

            val firstDayAd = try {
                DateConverterEngine.convertBsToAd(NepaliDate(year, month, 1))
            } catch (_: Exception) {
                LocalDate.now()
            }
            val dowValue = firstDayAd.dayOfWeek.value
            val offset = if (dowValue == 7) 0 else dowValue

            // Pre-calculate calendar days info on background thread
            val daysInfoList = mutableListOf<CalendarDayInfo>()
            val tz = currentLocation.tzOffset
            val lat = currentLocation.lat
            val lon = currentLocation.lon

            for (d in 1..days) {
                val adDate = try {
                    DateConverterEngine.convertBsToAd(NepaliDate(year, month, d))
                } catch (_: Exception) { LocalDate.now() }
                
                val isSaturday = adDate.dayOfWeek == DayOfWeek.SATURDAY
                val adDayStr = adDate.dayOfMonth.toString()
                
                // Get Tithi
                val (sunriseMins, _) = PanchangaCalculator.getSunriseSunsetMinutes(adDate, lat, lon, tz)
                val tValue = TithiCalculator.getTithiValue(adDate, sunriseMins / 60.0, tz)
                val idx = tValue.toInt() % 30
                val fullTithi = TithiCalculator.tithiNames[idx]
                val shortTithi = fullTithi.replace("शुक्ल ", "").replace("कृष्ण ", "")

                val isHoliday = holidays.any { it.bsDay == d }

                daysInfoList.add(
                    CalendarDayInfo(
                        bsDay = d,
                        isHoliday = isHoliday,
                        isSaturday = isSaturday,
                        tithi = shortTithi,
                        adDay = adDayStr
                    )
                )
            }

            val targetDay = dayToSelect ?: if (_uiState.value.selectedDay != null && _uiState.value.selectedDay!! <= days) {
                _uiState.value.selectedDay
            } else {
                1
            }

            _uiState.value = _uiState.value.copy(
                currentBsYear = year,
                currentBsMonth = month,
                daysInMonth = days,
                firstDayOfWeekOffset = offset,
                holidaysInMonth = holidays,
                daysInfo = daysInfoList,
                isLoading = false
            )
            
            if (targetDay != null) {
                selectDay(targetDay, year, month)
            }
        }
    }

    fun selectDay(day: Int, bsYear: Int = _uiState.value.currentBsYear, bsMonth: Int = _uiState.value.currentBsMonth) {
        viewModelScope.launch(Dispatchers.Default) {
            val year = bsYear
            val month = bsMonth
            val holiday = HolidayData.getHoliday(year, month, day)

            val bsDate = NepaliDate(year, month, day)
            val adDate = try {
                DateConverterEngine.convertBsToAd(bsDate)
            } catch (_: Exception) {
                LocalDate.now()
            }

            updatePlanetaryTransits(adDate)

            val monthNe = NepaliMonths.getMonthName(month, true)
            val dayNe = NepaliNumerals.toNepaliDigits(day)
            val yearNe = NepaliNumerals.toNepaliDigits(year)
            val dayOfWeekNe = NepaliDays.getDayName(adDate.dayOfWeek.value % 7, true)
            val adTextFormatted = adDate.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.ENGLISH))
            
            val lat = currentLocation.lat
            val lon = currentLocation.lon
            val tz = currentLocation.tzOffset

            val (sunriseMins, sunsetMins) = PanchangaCalculator.getSunriseSunsetMinutes(adDate, lat, lon, tz)
            val sunriseHour = sunriseMins / 60.0
            val (sunrise, sunset) = PanchangaCalculator.getSunriseSunset(adDate, lat, lon, tz)
            
            // TITHI & PAKSHA
            val exactTithi = TithiCalculator.getExactTithiEnd(adDate, sunriseHour, tz)
            val currentTithi = exactTithi.tithiName
            val ghadiPalaText = GhadiPalaCalculator.formatGhadiPala(exactTithi.totalGhadi)
            val tDurStr = AstroCalculator.formatTime24Short(exactTithi.endHourLocal)

            val isShukla = exactTithi.tithiIndex < 15
            val paksha = if (isShukla) "शुक्ल पक्ष (पूर्णिमा पक्ष)" else "कृष्ण पक्ष (औंसी पक्ष)"

            fun formatTithiWithPaksha(idx: Int): String {
                val rawName = TithiCalculator.tithiNames[idx % 30]
                val cleanName = rawName.replace("शुक्ल ", "").replace("कृष्ण ", "")
                return if (idx % 30 < 15) {
                    if (cleanName == "पूर्णिमा") "शुक्ल पक्ष (पूर्णिमा पक्ष) - पूर्णिमा"
                    else "शुक्ल पक्ष (पूर्णिमा पक्ष) $cleanName"
                } else {
                    if (cleanName == "औंसी" || cleanName == "औँसी") "कृष्ण पक्ष (औंसी पक्ष) - औंसी"
                    else "कृष्ण पक्ष (औंसी पक्ष) $cleanName"
                }
            }

            val currentTithiDisplay = formatTithiWithPaksha(exactTithi.tithiIndex)
            val nextTithiDisplay = formatTithiWithPaksha((exactTithi.tithiIndex + 1) % 30)

            val tFull = if (exactTithi.isAhoratra) {
                "$currentTithiDisplay (अहोरात्र / ६० घडी)"
            } else {
                "$currentTithiDisplay, $tDurStr बजेसम्म ($ghadiPalaText) त्यसपछि $nextTithiDisplay"
            }

            val nsText = NepalSambatCalculator.getNepalSambatDetails(year, month, day, if (isShukla) "शुक्ल" else "कृष्ण", currentTithi)

            // NAKSHATRA & PADA / NAMAKSHAR
            val exactNakshatra = NakshatraYogaCalculator.getExactNakshatraEnd(adDate, sunriseHour, tz)
            val currentNakshatra = exactNakshatra.name
            val nextNakshatra = NakshatraYogaCalculator.nakshatras[(exactNakshatra.index + 1) % 27]
            val nGhadiPalaText = GhadiPalaCalculator.formatGhadiPala(exactNakshatra.totalGhadi)
            val nDurStr = AstroCalculator.formatTime24Short(exactNakshatra.endHourLocal)
            val nFull = "$currentNakshatra (${exactNakshatra.extraInfo}), $nDurStr बजेसम्म ($nGhadiPalaText) त्यसपछि $nextNakshatra"

            // YOGA
            val exactYoga = NakshatraYogaCalculator.getExactYogaEnd(adDate, sunriseHour, tz)
            val currentYoga = exactYoga.name
            val nextYoga = NakshatraYogaCalculator.yogas[(exactYoga.index + 1) % 27]
            val yGhadiPalaText = GhadiPalaCalculator.formatGhadiPala(exactYoga.totalGhadi)
            val yDurStr = AstroCalculator.formatTime24Short(exactYoga.endHourLocal)
            val yFull = "$currentYoga, $yDurStr बजेसम्म ($yGhadiPalaText) त्यसपछि $nextYoga"

            // KARANA
            val kCross1 = TithiCalculator.findKaranaCrossingHour(adDate, sunriseHour, tz)
            val karana1 = TithiCalculator.getKarana(TithiCalculator.getTithiValue(adDate, kCross1 - 0.1, tz))
            val kDurStr1 = AstroCalculator.formatTime24Short(kCross1)
            
            val karana2 = TithiCalculator.getKarana(TithiCalculator.getTithiValue(adDate, kCross1 + 0.1, tz))
            val kCross2 = TithiCalculator.findKaranaCrossingHour(adDate, kCross1 + 0.1, tz)
            val kDurStr2 = AstroCalculator.formatTime24Short(kCross2)
            
            val karana3 = TithiCalculator.getKarana(TithiCalculator.getTithiValue(adDate, kCross2 + 0.1, tz))
            val bhadraInfo1 = if (karana1.contains("भद्रा")) " [भद्रा व्याप्त]" else ""
            val bhadraInfo2 = if (karana2.contains("भद्रा")) " [भद्रा व्याप्त]" else ""
            
            val kFull = if (kCross2 < sunriseHour + 24.0) {
                "$karana1$bhadraInfo1, $kDurStr1 बजेसम्म, त्यसपछि $karana2$bhadraInfo2, $kDurStr2 बजेसम्म $karana3"
            } else {
                "$karana1$bhadraInfo1, $kDurStr1 बजेसम्म त्यसपछि $karana2$bhadraInfo2"
            }

            // Planetary Transits (Gochar)
            val detailedGochar = AstroCalculator.getDetailedGochar(adDate, LocalTime.of(6, 0), tz)
            val currentGochar = detailedGochar.map { it.grahaName to "${it.rashiName} ${it.degreesText}" }

            @Suppress("SpellCheckingInspection")
            val rahuAbhijit = PanchangaCalculator.getRahuKaalAndAbhijit(adDate, lat, lon, tz)

            val ghadiPala = GhadiPalaCalculator.getCurrentGhadiPala()
            val timeText = PanchangaCalculator.getCurrentTimeNepali()
            val now = LocalTime.now()
            
            val barvelaSchedule = BarvelaCalculator.getBarvelaSchedule(adDate.dayOfWeek, now.hour, now.minute, sunriseMins, sunsetMins)
            val horaSchedule = HoraCalculator.getHoraSchedule(adDate.dayOfWeek, now.hour, now.minute, sunriseMins, sunsetMins)
            val currentBarvela = barvelaSchedule.firstOrNull { it.isCurrent }?.nameNepali ?: ""
            
            val sorhaShraddha = ShraddhaCalculator.getSorhaShraddhaInfo(month, currentTithi)

            _uiState.value = _uiState.value.copy(
                selectedDay = day,
                selectedAdDateObj = adDate,
                selectedDayBsText = "$yearNe, $monthNe $dayNe गते",
                selectedDayAdText = adTextFormatted,
                selectedDayNsText = nsText,
                selectedDayDayText = dayOfWeekNe,
                selectedDayAdDate = adTextFormatted,
                selectedDayHoliday = holiday,
                selectedDayGhadiPalaText = ghadiPala,
                panchangaTimeText = timeText,
                panchangaBarvelaText = currentBarvela,
                panchangaSunriseText = sunrise,
                panchangaSunsetText = sunset,
                barvelaSchedule = barvelaSchedule,
                horaSchedule = horaSchedule,
                sorhaShraddhaInfo = sorhaShraddha,
                paksha = paksha,
                tithiFullDetails = tFull,
                nakshatraFullDetails = nFull,
                yogaFullDetails = yFull,
                karanaFullDetails = kFull,
                gochar = currentGochar,
                detailedGochar = detailedGochar,
                rahuAndAbhijit = rahuAbhijit
            )
        }
    }
}
