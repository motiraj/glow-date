@file:Suppress("SpellCheckingInspection")
package com.example.bs_addateconverteragecalculator.data

import kotlin.math.*

enum class AyanamsaMode {
    LAHIRI_DRIK,                // चित्रपक्षीय (लहिरी) - Modern Drik Siddhanta
    SURYA_SIDDHANTA_TRADITIONAL // परम्परागत सूर्य सिद्धान्त (५४ विकला वार्षिक अयन गति)
}

/**
 * Formatted Rashi-Amsha-Kala-Vikala representation (जस्तै: ४रा १५° ३२' १०" (सिंह))
 */
data class RashiAmshaKala(
    val rashiIndex: Int,   // 0..11 (०=मेष .. ११=मीन)
    val rashiNumber: Int,  // 1..12 (१=मेष .. १२=मीन)
    val rashiName: String,
    val degrees: Int,      // अंश (0..29)
    val arcMinutes: Int,   // कला (0..59)
    val arcSeconds: Int    // विकला (0..59)
) {
    override fun toString(): String = "${rashiIndex}रा ${degrees}° ${arcMinutes}' ${arcSeconds}\" ($rashiName)"
}

data class KundaliPoint(
    val longitude: Double,
    val rashiIndex: Int,      // 1 = मेष .. 12 = मीन
    val degInRashi: Double,
    val nakshatraIndex: Int,  // 1 = अश्विनी .. 27 = रेवती
    val pada: Int             // 1 .. 4
)

/**
 * Complete Offline Planetary Snapshot for a given Moment
 */
data class PlanetarySnapshot(
    val julianDayUT: Double,
    val ayanamsaDegrees: Double,
    val sunLon: Double,
    val moonLon: Double,
    val marsLon: Double,
    val mercuryLon: Double,
    val jupiterLon: Double,
    val venusLon: Double,
    val saturnLon: Double,
    val rahuLon: Double,
    val ketuLon: Double = ((rahuLon + 180.0) % 360.0 + 360.0) % 360.0,
    // Daily speeds in degrees/day (used for Tithi, Nakshatra, Yoga, Karana Ghadi-Pala)
    val sunDailySpeed: Double = 0.9856,
    val moonDailySpeed: Double = 13.1764,
    // Retrograde flags (वक्री अवस्था)
    val isMarsRetrograde: Boolean = false,
    val isMercuryRetrograde: Boolean = false,
    val isJupiterRetrograde: Boolean = false,
    val isVenusRetrograde: Boolean = false,
    val isSaturnRetrograde: Boolean = false,
    // Combustion flags (अस्त अवस्था)
    val isMarsCombust: Boolean = false,
    val isMercuryCombust: Boolean = false,
    val isJupiterCombust: Boolean = false,
    val isVenusCombust: Boolean = false,
    val isSaturnCombust: Boolean = false
) {
    // 1-based Rashi numbers (1 = मेष ... 12 = मीन)
    val sunRashi get() = (floor(sunLon / 30.0).toInt() % 12) + 1
    val moonRashi get() = (floor(moonLon / 30.0).toInt() % 12) + 1
    val moonNakshatra get() = (floor(moonLon / (40.0 / 3.0)).toInt() % 27) + 1
    val marsRashi get() = (floor(marsLon / 30.0).toInt() % 12) + 1
    val mercuryRashi get() = (floor(mercuryLon / 30.0).toInt() % 12) + 1
    val jupiterRashi get() = (floor(jupiterLon / 30.0).toInt() % 12) + 1
    val venusRashi get() = (floor(venusLon / 30.0).toInt() % 12) + 1
    val saturnRashi get() = (floor(saturnLon / 30.0).toInt() % 12) + 1
    val rahuRashi get() = (floor(rahuLon / 30.0).toInt() % 12) + 1
    val ketuRashi get() = (floor(ketuLon / 30.0).toInt() % 12) + 1
}

object OfflineVedicEphemeris {

    private const val DEG_TO_RAD = PI / 180.0
    private const val RAD_TO_DEG = 180.0 / PI

    private val RASHI_NAMES = arrayOf(
        "मेष", "वृष", "मिथुन", "कर्कट", "सिंह", "कन्या",
        "तुला", "वृश्चिक", "धनु", "मकर", "कुम्भ", "मीन"
    )

    private fun norm360(deg: Double): Double = ((deg % 360.0) + 360.0) % 360.0
    private fun sinDeg(deg: Double): Double = sin(deg * DEG_TO_RAD)
    private fun cosDeg(deg: Double): Double = cos(deg * DEG_TO_RAD)
    private fun atan2Deg(y: Double, x: Double): Double = norm360(atan2(y, x) * RAD_TO_DEG)

    /**
     * Checks if a planet is combust (अस्त) due to proximity to the Sun
     */
    fun checkPlanetCombustion(sunLon: Double, planetLon: Double, planetName: String): Boolean {
        val diff = abs(sunLon - planetLon)
        val angularDist = if (diff > 180.0) 360.0 - diff else diff

        val combustionLimit = when (planetName.lowercase()) {
            "mars", "मङ्गल", "मंगल" -> 17.0
            "mercury", "बुध" -> 14.0
            "jupiter", "बृहस्पति" -> 11.0
            "venus", "शुक्र" -> 10.0
            "saturn", "शनि" -> 15.0
            else -> 10.0
        }

        return angularDist <= combustionLimit
    }

    /**
     * Shortest signed angular difference (target - source) in range [-180.0, +180.0]
     * Used to calculate exact daily planetary speed and retrograde motion.
     */
    private fun signedAngleDiff(targetDeg: Double, sourceDeg: Double): Double {
        var diff = (targetDeg - sourceDeg) % 360.0
        if (diff < -180.0) diff += 360.0
        if (diff > 180.0) diff -= 360.0
        return diff
    }

    /**
     * MAIN FUNCTION: Compute Sidereal PlanetarySnapshot for any AD Date & Local Time
     */
    fun getPlanetarySnapshot(
        year: Int,
        month: Int,
        day: Int,
        hour: Int = 6,
        minute: Int = 0,
        second: Int = 0,
        tzOffsetHours: Double = if (year < 1986) 5.5 else 5.75,
        ayanamsaMode: AyanamsaMode = AyanamsaMode.LAHIRI_DRIK
    ): PlanetarySnapshot {
        val jdUT = toJulianDay(year, month, day, hour, minute, second, tzOffsetHours)
        return getPlanetarySnapshotFromJD(jdUT, ayanamsaMode)
    }

    /**
     * Computes Sidereal longitudes, daily speeds, and retrograde statuses for any Julian Day (UT)
     */
    fun getPlanetarySnapshotFromJD(
        jdUT: Double,
        ayanamsaMode: AyanamsaMode = AyanamsaMode.LAHIRI_DRIK
    ): PlanetarySnapshot {
        val now = computeRawSiderealPositions(jdUT, ayanamsaMode)
        // Sample 6 hours (0.25 days) ahead to compute instantaneous daily velocities & retrograde status
        val next = computeRawSiderealPositions(jdUT + 0.25, ayanamsaMode)

        val sunSpeed  = signedAngleDiff(next.sun, now.sun) * 4.0
        val moonSpeed = signedAngleDiff(next.moon, now.moon) * 4.0

        val marsSpeed    = signedAngleDiff(next.mars, now.mars) * 4.0
        val mercurySpeed = signedAngleDiff(next.mercury, now.mercury) * 4.0
        val jupiterSpeed = signedAngleDiff(next.jupiter, now.jupiter) * 4.0
        val venusSpeed   = signedAngleDiff(next.venus, now.venus) * 4.0
        val saturnSpeed  = signedAngleDiff(next.saturn, now.saturn) * 4.0

        return PlanetarySnapshot(
            julianDayUT = jdUT,
            ayanamsaDegrees = now.ayanamsa,
            sunLon = now.sun,
            moonLon = now.moon,
            marsLon = now.mars,
            mercuryLon = now.mercury,
            jupiterLon = now.jupiter,
            venusLon = now.venus,
            saturnLon = now.saturn,
            rahuLon = now.rahu,
            ketuLon = norm360(now.rahu + 180.0),
            sunDailySpeed = sunSpeed,
            moonDailySpeed = moonSpeed,
            isMarsRetrograde = marsSpeed < 0.0,
            isMercuryRetrograde = mercurySpeed < 0.0,
            isJupiterRetrograde = jupiterSpeed < 0.0,
            isVenusRetrograde = venusSpeed < 0.0,
            isSaturnRetrograde = saturnSpeed < 0.0,
            isMarsCombust = checkPlanetCombustion(now.sun, now.mars, "mars"),
            isMercuryCombust = checkPlanetCombustion(now.sun, now.mercury, "mercury"),
            isJupiterCombust = checkPlanetCombustion(now.sun, now.jupiter, "jupiter"),
            isVenusCombust = checkPlanetCombustion(now.sun, now.venus, "venus"),
            isSaturnCombust = checkPlanetCombustion(now.sun, now.saturn, "saturn")
        )
    }

    /**
     * Converts Decimal Degrees (0..360) into traditional Nepali Rashi-Amsha-Kala-Vikala
     */
    fun toRashiAmshaKala(longitude: Double): RashiAmshaKala {
        val norm = norm360(longitude)
        val rashiIdx = floor(norm / 30.0).toInt().coerceIn(0, 11)
        val remInRashi = norm % 30.0
        val deg = floor(remInRashi).toInt()
        val remMin = (remInRashi - deg) * 60.0
        val min = floor(remMin).toInt()
        val sec = ((remMin - min) * 60.0).roundToInt().coerceIn(0, 59)

        return RashiAmshaKala(
            rashiIndex = rashiIdx,
            rashiNumber = rashiIdx + 1,
            rashiName = RASHI_NAMES[rashiIdx],
            degrees = deg,
            arcMinutes = min,
            arcSeconds = sec
        )
    }

    /**
     * Converts Nirayana Longitude to KundaliPoint
     */
    fun toKundaliPoint(longitude: Double): KundaliPoint {
        val norm = norm360(longitude)
        val rashi = floor(norm / 30.0).toInt() + 1
        val degInRashi = norm % 30.0
        val nakSpan = 360.0 / 27.0 // 13.333333°
        val nakshatra = floor(norm / nakSpan).toInt() + 1
        val pada = floor((norm % nakSpan) / (nakSpan / 4.0)).toInt() + 1
        return KundaliPoint(norm, rashi, degInRashi, nakshatra, pada)
    }

    /**
     * Converts AD Date + Time to Julian Day (UT) with exact UT day shifting
     */
    fun toJulianDay(
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int,
        second: Int = 0,
        tzOffsetHours: Double = 5.75
    ): Double {
        var utHours = hour + (minute / 60.0) + (second / 3600.0) - tzOffsetHours
        var y = year
        var m = month
        var d = day.toDouble()

        while (utHours < 0.0) {
            utHours += 24.0
            d -= 1.0
        }
        while (utHours >= 24.0) {
            utHours -= 24.0
            d += 1.0
        }

        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0)
        val b = 2.0 - a + floor(a / 4.0)
        return floor(365.25 * (y + 4716)) +
                floor(30.6001 * (m + 1)) +
                d + (utHours / 24.0) + b - 1524.5
    }

    // --- INTERNAL ASTRONOMICAL COMPUTATION ---

    private data class RawPositions(
        val ayanamsa: Double,
        val sun: Double,
        val moon: Double,
        val mars: Double,
        val mercury: Double,
        val jupiter: Double,
        val venus: Double,
        val saturn: Double,
        val rahu: Double
    )

    private fun computeRawSiderealPositions(jdUT: Double, mode: AyanamsaMode): RawPositions {
        val d = jdUT - 2451545.0
        val t = d / 36525.0

        val ayanamsa = when (mode) {
            AyanamsaMode.LAHIRI_DRIK -> 23.857092 + (1.396971 * t) + (0.000308 * t * t)
            AyanamsaMode.SURYA_SIDDHANTA_TRADITIONAL -> {
                val yearsSince499CE = (jdUT - 1903396.5) / 365.258756
                (yearsSince499CE * 54.0) / 3600.0
            }
        }

        val (sunTrop, earthR, earthHelioLon) = calculateSunAndEarth(d)
        val moonTrop = calculateMoonTropical(d)
        val rahuTrop = norm360(125.044555 - 0.0529537628 * d)

        val mercTrop = calculatePlanetGeocentric(Planet.MERCURY, d, earthHelioLon, earthR)
        val venTrop  = calculatePlanetGeocentric(Planet.VENUS,   d, earthHelioLon, earthR)
        val marsTrop = calculatePlanetGeocentric(Planet.MARS,    d, earthHelioLon, earthR)
        val jupTrop  = calculatePlanetGeocentric(Planet.JUPITER, d, earthHelioLon, earthR)
        val satTrop  = calculatePlanetGeocentric(Planet.SATURN,  d, earthHelioLon, earthR)

        return RawPositions(
            ayanamsa = ayanamsa,
            sun = norm360(sunTrop - ayanamsa),
            moon = norm360(moonTrop - ayanamsa),
            mars = norm360(marsTrop - ayanamsa),
            mercury = norm360(mercTrop - ayanamsa),
            jupiter = norm360(jupTrop - ayanamsa),
            venus = norm360(venTrop - ayanamsa),
            saturn = norm360(satTrop - ayanamsa),
            rahu = norm360(rahuTrop - ayanamsa)
        )
    }

    private fun calculateSunAndEarth(d: Double): Triple<Double, Double, Double> {
        val w = norm360(282.9404 + 4.70935E-5 * d)
        val e = 0.016709 - 1.151E-9 * d
        val m = norm360(356.0470 + 0.9856002585 * d)

        val eAnom = m + (e * RAD_TO_DEG) * sinDeg(m) * (1.0 + e * cosDeg(m))
        val xv = cosDeg(eAnom) - e
        val yv = sqrt(1.0 - e * e) * sinDeg(eAnom)

        val r = sqrt(xv * xv + yv * yv)
        val v = atan2Deg(yv, xv)

        val sunTropLon = norm360(v + w)
        val earthHelioLon = norm360(sunTropLon + 180.0)
        return Triple(sunTropLon, r, earthHelioLon)
    }

    private fun calculateMoonTropical(d: Double): Double {
        val t = d / 36525.0
        val lPrime = norm360(218.3164477 + 481267.88123421 * t)
        val dElong = norm360(297.8501921 + 445267.1114034 * t)
        val mSun   = norm360(357.5291092 + 35999.0502909 * t)
        val mMoon  = norm360(134.9633964 + 477198.8675055 * t)
        val fArg   = norm360(93.2720950 + 483202.0175233 * t)

        var corr = 0.0
        corr += 6.288774 * sinDeg(mMoon)
        corr += 1.274027 * sinDeg(2 * dElong - mMoon)
        corr += 0.658314 * sinDeg(2 * dElong)
        corr += 0.213618 * sinDeg(2 * mMoon)
        corr -= 0.185116 * sinDeg(mSun)
        corr -= 0.114332 * sinDeg(2 * fArg)
        corr += 0.058793 * sinDeg(2 * dElong - 2 * mMoon)
        corr += 0.057066 * sinDeg(2 * dElong - mSun - mMoon)
        corr += 0.053322 * sinDeg(2 * dElong + mMoon)
        corr += 0.045758 * sinDeg(2 * dElong - mSun)
        corr -= 0.040923 * sinDeg(mMoon - mSun)
        corr -= 0.034720 * sinDeg(dElong)
        corr -= 0.030383 * sinDeg(mMoon + mSun)

        return norm360(lPrime + corr)
    }

    private enum class Planet(
        val n0: Double, val nDot: Double,
        val i0: Double, val iDot: Double,
        val w0: Double, val wDot: Double,
        val a: Double,
        val e0: Double, val eDot: Double,
        val m0: Double, val mDot: Double
    ) {
        MERCURY(48.3313, 3.24587E-5, 7.0047, 5.00E-8, 29.1241, 1.01444E-5, 0.387098, 0.205635, 5.59E-10, 168.6562, 4.0923344368),
        VENUS(76.6799, 2.46590E-5, 3.3946, 2.75E-8, 54.8910, 1.38374E-5, 0.723330, 0.006773, -1.302E-9, 48.0052, 1.6021302244),
        MARS(49.5574, 2.11081E-5, 1.8497, -1.78E-8, 286.5016, 2.92961E-5, 1.523688, 0.093405, 2.516E-9, 18.6021, 0.5240207766),
        JUPITER(100.4542, 2.76854E-5, 1.3030, -1.557E-7, 273.8777, 1.64505E-5, 5.20256, 0.048498, 4.469E-9, 19.8950, 0.0830853001),
        SATURN(113.6634, 2.38980E-5, 2.4886, -1.081E-7, 339.3939, 2.97661E-5, 9.55475, 0.055546, -9.499E-9, 316.9670, 0.0334442282)
    }

    private fun calculatePlanetGeocentric(
        p: Planet,
        d: Double,
        earthHelioLon: Double,
        earthRadius: Double
    ): Double {
        val n = norm360(p.n0 + p.nDot * d)
        val inc = p.i0 + p.iDot * d
        val w = norm360(p.w0 + p.wDot * d)
        val a = p.a
        val e = p.e0 + p.eDot * d
        val m = norm360(p.m0 + p.mDot * d)

        var eAnom = m + (e * RAD_TO_DEG) * sinDeg(m) * (1.0 + e * cosDeg(m))
        repeat(4) {
            val deltaE = (eAnom - (e * RAD_TO_DEG) * sinDeg(eAnom) - m) / (1.0 - e * cosDeg(eAnom))
            eAnom -= deltaE
        }

        val xv = a * (cosDeg(eAnom) - e)
        val yv = a * sqrt(1.0 - e * e) * sinDeg(eAnom)
        val r = sqrt(xv * xv + yv * yv)
        val v = atan2Deg(yv, xv)

        val u = norm360(v + w)
        val xh = r * (cosDeg(n) * cosDeg(u) - sinDeg(n) * sinDeg(u) * cosDeg(inc))
        val yh = r * (sinDeg(n) * cosDeg(u) + cosDeg(n) * sinDeg(u) * cosDeg(inc))
        var helioLon = atan2Deg(yh, xh)

        if (p == Planet.JUPITER || p == Planet.SATURN) {
            val mJup = norm360(Planet.JUPITER.m0 + Planet.JUPITER.mDot * d)
            val mSat = norm360(Planet.SATURN.m0 + Planet.SATURN.mDot * d)
            if (p == Planet.JUPITER) {
                helioLon -= 0.332 * sinDeg(2 * mJup - 5 * mSat - 67.6)
                helioLon -= 0.056 * sinDeg(2 * mJup - 2 * mSat + 21.0)
                helioLon += 0.042 * sinDeg(3 * mJup - 5 * mSat + 21.0)
            } else {
                helioLon += 0.812 * sinDeg(2 * mJup - 5 * mSat - 67.6)
                helioLon -= 0.229 * sinDeg(2 * mJup - 4 * mSat - 2.0)
                helioLon += 0.119 * sinDeg(mJup - 2 * mSat - 3.0)
            }
        }

        val xPlanet = r * cosDeg(helioLon)
        val yPlanet = r * sinDeg(helioLon)
        val xEarth = earthRadius * cosDeg(earthHelioLon)
        val yEarth = earthRadius * sinDeg(earthHelioLon)

        return atan2Deg(yPlanet - yEarth, xPlanet - xEarth)
    }
}
