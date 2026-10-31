package com.example.bs_addateconverteragecalculator

import com.example.bs_addateconverteragecalculator.calculator.AgeCalculator
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class AgeCalculatorTest {

    @Test
    fun testAgeCalculation() {
        val birthDate = LocalDate.of(2000, 1, 1)
        val refDate = LocalDate.of(2030, 1, 1)
        val result = AgeCalculator.calculateFromAd(birthDate, refDate)

        assertEquals(30, result.years)
        assertEquals(0, result.months)
        assertEquals(0, result.days)
        assertEquals(true, result.isCalculated)
    }
}
