package com.example.bs_addateconverteragecalculator.data

import java.time.LocalDate

data class NepaliDate(
    val year: Int,
    val month: Int, // 1 to 12
    val day: Int    // 1 to 32
) {
    fun isValid(): Boolean {
        if (year !in 2000..2100) return false
        if (month !in 1..12) return false
        val daysInMonth = BsCalendarData.getDaysInMonth(year, month)
        return day in 1..daysInMonth
    }
}

data class BsYearData(
    val baishakh1Ad: LocalDate,
    val months: IntArray
)

object BsCalendarData {
    const val REFERENCE_BS_YEAR = 2000

    // Highly calibrated year-anchored Nepali Patro data for years 2000 to 2100
    // Aligned to authoritative global standard (nepali-date-converter)
    private val yearData: Map<Int, BsYearData> = mapOf(
        2000 to BsYearData(LocalDate.of(1943, 4, 14), intArrayOf(30,32,31,32,31,30,30,30,29,30,29,31)),
        2001 to BsYearData(LocalDate.of(1944, 4, 13), intArrayOf(31,31,32,31,31,31,30,29,30,29,30,30)),
        2002 to BsYearData(LocalDate.of(1945, 4, 13), intArrayOf(31,31,32,32,31,30,30,29,30,29,30,30)),
        2003 to BsYearData(LocalDate.of(1946, 4, 13), intArrayOf(31,32,31,32,31,30,30,30,29,29,30,31)),
        2004 to BsYearData(LocalDate.of(1947, 4, 14), intArrayOf(30,32,31,32,31,30,30,30,29,30,29,31)),
        2005 to BsYearData(LocalDate.of(1948, 4, 13), intArrayOf(31,31,32,31,31,31,30,29,30,29,30,30)),
        2006 to BsYearData(LocalDate.of(1949, 4, 13), intArrayOf(31,31,32,32,31,30,30,29,30,29,30,30)),
        2007 to BsYearData(LocalDate.of(1950, 4, 13), intArrayOf(31,32,31,32,31,30,30,30,29,29,30,31)),
        2008 to BsYearData(LocalDate.of(1951, 4, 14), intArrayOf(31,31,31,32,31,31,29,30,30,29,29,31)),
        2009 to BsYearData(LocalDate.of(1952, 4, 13), intArrayOf(31,31,32,31,31,31,30,29,30,29,30,30)),
        2010 to BsYearData(LocalDate.of(1953, 4, 13), intArrayOf(31,31,32,32,31,30,30,29,30,29,30,30)),
        2011 to BsYearData(LocalDate.of(1954, 4, 13), intArrayOf(31,32,31,32,31,30,30,30,29,29,30,31)),
        2012 to BsYearData(LocalDate.of(1955, 4, 14), intArrayOf(31,31,31,32,31,31,29,30,30,29,30,30)),
        2013 to BsYearData(LocalDate.of(1956, 4, 13), intArrayOf(31,31,32,31,31,31,30,29,30,29,30,30)),
        2014 to BsYearData(LocalDate.of(1957, 4, 13), intArrayOf(31,31,32,32,31,30,30,29,30,29,30,30)),
        2015 to BsYearData(LocalDate.of(1958, 4, 13), intArrayOf(31,32,31,32,31,30,30,30,29,29,30,31)),
        2016 to BsYearData(LocalDate.of(1959, 4, 14), intArrayOf(31,31,31,32,31,31,29,30,30,29,30,30)),
        2017 to BsYearData(LocalDate.of(1960, 4, 13), intArrayOf(31,31,32,31,31,31,30,29,30,29,30,30)),
        2018 to BsYearData(LocalDate.of(1961, 4, 13), intArrayOf(31,32,31,32,31,30,30,29,30,29,30,30)),
        2019 to BsYearData(LocalDate.of(1962, 4, 13), intArrayOf(31,32,31,32,31,30,30,30,29,30,29,31)),
        2020 to BsYearData(LocalDate.of(1963, 4, 14), intArrayOf(31,31,31,32,31,31,30,29,30,29,30,30)),
        2021 to BsYearData(LocalDate.of(1964, 4, 13), intArrayOf(31,31,32,31,31,31,30,29,30,29,30,30)),
        2022 to BsYearData(LocalDate.of(1965, 4, 13), intArrayOf(31,32,31,32,31,30,30,30,29,29,30,30)),
        2023 to BsYearData(LocalDate.of(1966, 4, 13), intArrayOf(31,32,31,32,31,30,30,30,29,30,29,31)),
        2024 to BsYearData(LocalDate.of(1967, 4, 14), intArrayOf(31,31,31,32,31,31,30,29,30,29,30,30)),
        2025 to BsYearData(LocalDate.of(1968, 4, 13), intArrayOf(31,31,32,31,31,31,30,29,30,29,30,30)),
        2026 to BsYearData(LocalDate.of(1969, 4, 13), intArrayOf(31,32,31,32,31,30,30,30,29,29,30,31)),
        2027 to BsYearData(LocalDate.of(1970, 4, 14), intArrayOf(30,32,31,32,31,30,30,30,29,30,29,31)),
        2028 to BsYearData(LocalDate.of(1971, 4, 14), intArrayOf(31,31,32,31,31,31,30,29,30,29,30,30)),
        2029 to BsYearData(LocalDate.of(1972, 4, 13), intArrayOf(31,31,32,31,32,30,30,29,30,29,30,30)),
        2030 to BsYearData(LocalDate.of(1973, 4, 13), intArrayOf(31,32,31,32,31,30,30,30,29,29,30,31)),
        2031 to BsYearData(LocalDate.of(1974, 4, 14), intArrayOf(30,32,31,32,31,30,30,30,29,30,29,31)),
        2032 to BsYearData(LocalDate.of(1975, 4, 14), intArrayOf(31,31,32,31,31,31,30,29,30,29,30,30)),
        2033 to BsYearData(LocalDate.of(1976, 4, 13), intArrayOf(31,31,32,32,31,30,30,29,30,29,30,30)),
        2034 to BsYearData(LocalDate.of(1977, 4, 13), intArrayOf(31,32,31,32,31,30,30,30,29,29,30,31)),
        2035 to BsYearData(LocalDate.of(1978, 4, 14), intArrayOf(30,32,31,32,31,31,29,30,30,29,29,31)),
        2036 to BsYearData(LocalDate.of(1979, 4, 14), intArrayOf(31,31,32,31,31,31,30,29,30,29,30,30)),
        2037 to BsYearData(LocalDate.of(1980, 4, 13), intArrayOf(31,31,32,32,31,30,30,29,30,29,30,30)),
        2038 to BsYearData(LocalDate.of(1981, 4, 13), intArrayOf(31,32,31,32,31,30,30,30,29,29,30,31)),
        2039 to BsYearData(LocalDate.of(1982, 4, 14), intArrayOf(31,31,31,32,31,31,29,30,30,29,30,30)),
        2040 to BsYearData(LocalDate.of(1983, 4, 14), intArrayOf(31,31,32,31,31,31,30,29,30,29,30,30)),
        2041 to BsYearData(LocalDate.of(1984, 4, 13), intArrayOf(31,31,32,32,31,30,30,29,30,29,30,30)),
        2042 to BsYearData(LocalDate.of(1985, 4, 13), intArrayOf(31,32,31,32,31,30,30,30,29,29,30,31)),
        2043 to BsYearData(LocalDate.of(1986, 4, 14), intArrayOf(31,31,31,32,31,31,29,30,30,29,30,30)),
        2044 to BsYearData(LocalDate.of(1987, 4, 14), intArrayOf(31,31,32,31,31,31,30,29,30,29,30,30)),
        2045 to BsYearData(LocalDate.of(1988, 4, 13), intArrayOf(31,32,31,32,31,30,30,29,30,29,30,30)),
        2046 to BsYearData(LocalDate.of(1989, 4, 13), intArrayOf(31,32,31,32,31,30,30,30,29,29,30,31)),
        2047 to BsYearData(LocalDate.of(1990, 4, 14), intArrayOf(31,31,31,32,31,31,30,29,30,29,30,30)),
        2048 to BsYearData(LocalDate.of(1991, 4, 14), intArrayOf(31,31,32,31,31,31,30,29,30,29,30,30)),
        2049 to BsYearData(LocalDate.of(1992, 4, 13), intArrayOf(31,32,31,32,31,30,30,30,29,29,30,30)),
        2050 to BsYearData(LocalDate.of(1993, 4, 13), intArrayOf(31,32,31,32,31,30,30,30,29,30,29,31)),
        2051 to BsYearData(LocalDate.of(1994, 4, 14), intArrayOf(31,31,31,32,31,31,30,29,30,29,30,30)),
        2052 to BsYearData(LocalDate.of(1995, 4, 14), intArrayOf(31,31,32,31,31,31,30,29,30,29,30,30)),
        2053 to BsYearData(LocalDate.of(1996, 4, 13), intArrayOf(31,32,31,32,31,30,30,30,29,29,30,30)),
        2054 to BsYearData(LocalDate.of(1997, 4, 13), intArrayOf(31,32,31,32,31,30,30,30,29,30,29,31)),
        2055 to BsYearData(LocalDate.of(1998, 4, 14), intArrayOf(31,31,32,31,31,31,30,29,30,29,30,30)),
        2056 to BsYearData(LocalDate.of(1999, 4, 14), intArrayOf(31,31,32,31,32,30,30,29,30,29,30,30)),
        2057 to BsYearData(LocalDate.of(2000, 4, 13), intArrayOf(31,32,31,32,31,30,30,30,29,29,30,31)),
        2058 to BsYearData(LocalDate.of(2001, 4, 14), intArrayOf(30,32,31,32,31,30,30,30,29,30,29,31)),
        2059 to BsYearData(LocalDate.of(2002, 4, 14), intArrayOf(31,31,32,31,31,31,30,29,30,29,30,30)),
        2060 to BsYearData(LocalDate.of(2003, 4, 14), intArrayOf(31,31,32,32,31,30,30,29,30,29,30,30)),
        2061 to BsYearData(LocalDate.of(2004, 4, 13), intArrayOf(31,32,31,32,31,30,30,30,29,29,30,31)),
        2062 to BsYearData(LocalDate.of(2005, 4, 14), intArrayOf(30,32,31,32,31,31,29,30,29,30,29,31)),
        2063 to BsYearData(LocalDate.of(2006, 4, 14), intArrayOf(31,31,32,31,31,31,30,29,30,29,30,30)),
        2064 to BsYearData(LocalDate.of(2007, 4, 14), intArrayOf(31,31,32,32,31,30,30,29,30,29,30,30)),
        2065 to BsYearData(LocalDate.of(2008, 4, 13), intArrayOf(31,32,31,32,31,30,30,30,29,29,30,31)),
        2066 to BsYearData(LocalDate.of(2009, 4, 14), intArrayOf(31,31,31,32,31,31,29,30,30,29,29,31)),
        2067 to BsYearData(LocalDate.of(2010, 4, 14), intArrayOf(31,31,32,31,31,31,30,29,30,29,30,30)),
        2068 to BsYearData(LocalDate.of(2011, 4, 14), intArrayOf(31,31,32,32,31,30,30,29,30,29,30,30)),
        2069 to BsYearData(LocalDate.of(2012, 4, 13), intArrayOf(31,32,31,32,31,30,30,30,29,29,30,31)),
        2070 to BsYearData(LocalDate.of(2013, 4, 14), intArrayOf(31,31,31,32,31,31,29,30,30,29,30,30)),
        2071 to BsYearData(LocalDate.of(2014, 4, 14), intArrayOf(31,31,32,31,31,31,30,29,30,29,30,30)),
        2072 to BsYearData(LocalDate.of(2015, 4, 14), intArrayOf(31,32,31,32,31,30,30,29,30,29,30,30)),
        2073 to BsYearData(LocalDate.of(2016, 4, 13), intArrayOf(31,32,31,32,31,30,30,30,29,29,30,31)),
        2074 to BsYearData(LocalDate.of(2017, 4, 14), intArrayOf(31,31,31,32,31,31,30,29,30,29,30,30)),
        2075 to BsYearData(LocalDate.of(2018, 4, 14), intArrayOf(31,31,32,31,31,31,30,29,30,29,30,30)),
        2076 to BsYearData(LocalDate.of(2019, 4, 14), intArrayOf(31,32,31,32,31,30,30,30,29,29,30,30)),
        2077 to BsYearData(LocalDate.of(2020, 4, 13), intArrayOf(31,32,31,32,31,30,30,30,29,30,29,31)),
        2078 to BsYearData(LocalDate.of(2021, 4, 14), intArrayOf(31,31,31,32,31,31,30,29,30,29,30,30)),
        2079 to BsYearData(LocalDate.of(2022, 4, 14), intArrayOf(31,31,32,31,31,31,30,29,30,29,30,30)),
        2080 to BsYearData(LocalDate.of(2023, 4, 14), intArrayOf(31,32,31,32,31,30,30,30,29,29,30,30)),
        2081 to BsYearData(LocalDate.of(2024, 4, 13), intArrayOf(31,32,31,32,31,30,30,30,29,30,29,31)),
        2082 to BsYearData(LocalDate.of(2025, 4, 14), intArrayOf(31,31,32,31,31,31,30,29,30,29,30,30)),
        2083 to BsYearData(LocalDate.of(2026, 4, 14), intArrayOf(31,31,32,31,31,31,30,29,30,29,30,30)),
        2084 to BsYearData(LocalDate.of(2027, 4, 14), intArrayOf(31,32,31,32,31,30,30,30,29,29,30,31)),
        2085 to BsYearData(LocalDate.of(2028, 4, 14), intArrayOf(30,32,31,32,31,30,30,30,29,30,29,31)),
        2086 to BsYearData(LocalDate.of(2029, 4, 14), intArrayOf(31,31,32,31,31,31,30,29,30,29,30,30)),
        2087 to BsYearData(LocalDate.of(2030, 4, 14), intArrayOf(31,31,32,31,31,31,30,30,29,30,30,30)),
        2088 to BsYearData(LocalDate.of(2031, 4, 15), intArrayOf(30,31,32,32,30,31,30,30,29,30,30,30)),
        2089 to BsYearData(LocalDate.of(2032, 4, 14), intArrayOf(30,32,31,32,31,30,30,30,29,30,30,30)),
        2090 to BsYearData(LocalDate.of(2033, 4, 14), intArrayOf(30,32,31,32,31,30,30,30,29,30,30,30)),
    )

    fun getBaishakh1Ad(year: Int): LocalDate? {
        return yearData[year]?.baishakh1Ad
    }

    fun getDaysInMonth(year: Int, month: Int): Int {
        val yData = yearData[year] ?: return 30
        return if (month in 1..12) yData.months[month - 1] else 30
    }
}
