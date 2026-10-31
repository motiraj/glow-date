package com.example.bs_addateconverteragecalculator

import com.example.bs_addateconverteragecalculator.converter.DateConverterEngine
import com.example.bs_addateconverteragecalculator.data.NepaliDate
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DateConversionTest {

    @Test
    fun testReferenceDateConversion() {
        val bsDate = NepaliDate(2000, 1, 1)
        val adDate = DateConverterEngine.convertBsToAd(bsDate)
        // Global Patro standard reference for 2000 Baisakh 1 is April 14, 1943
        assertEquals(LocalDate.of(1943, 4, 14), adDate)

        val convertedBs = DateConverterEngine.convertAdToBs(adDate)
        assertEquals(bsDate, convertedBs)
    }

    @Test
    fun testSpecificDate2068Ashoj19() {
        val bsDate = NepaliDate(2068, 6, 19)
        val expectedAd = LocalDate.of(2011, 10, 6)
        val adDate = DateConverterEngine.convertBsToAd(bsDate)
        assertEquals(expectedAd, adDate)

        val convertedBs = DateConverterEngine.convertAdToBs(expectedAd)
        assertEquals(bsDate, convertedBs)
    }

    @Test
    fun testSpecificDate2039Jestha16() {
        val bsDate = NepaliDate(2039, 2, 16)
        val expectedAd = LocalDate.of(1982, 5, 30)
        val adDate = DateConverterEngine.convertBsToAd(bsDate)
        assertEquals(expectedAd, adDate)

        val convertedBs = DateConverterEngine.convertAdToBs(expectedAd)
        assertEquals(bsDate, convertedBs)
    }

    @Test
    fun test2083Ashwin10() {
        val bsDate = NepaliDate(2083, 6, 10)
        val expectedAd = LocalDate.of(2026, 9, 26) 
        val adDate = DateConverterEngine.convertBsToAd(bsDate)
        assertEquals(expectedAd, adDate)

        val convertedBs = DateConverterEngine.convertAdToBs(expectedAd)
        assertEquals(bsDate, convertedBs)
    }
}
