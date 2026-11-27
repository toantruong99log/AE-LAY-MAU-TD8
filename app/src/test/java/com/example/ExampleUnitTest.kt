package com.example

import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testEmptyAndZeroInputs() {
        val emptyResult = calculateSampleResult("", ConcreteType.BE_TONG)
        assertEquals(SampleResult(0, 0, 0, 0), emptyResult)

        val zeroResult = calculateSampleResult("0", ConcreteType.BE_TONG_CO_THAM)
        assertEquals(SampleResult(0, 0, 0, 0), zeroResult)

        val negativeResult = calculateSampleResult("-15.5", ConcreteType.BE_TONG_CO_R7_CO_THAM)
        assertEquals(SampleResult(0, 0, 0, 0), negativeResult)
    }

    @Test
    fun testType1Vua() {
        // P = 600 -> r28 = ceil(600/500) = 2, luu = 2, r7 = 0, tham = 0
        val res = calculateSampleResult("600", ConcreteType.VUA)
        assertEquals(SampleResult(r7 = 0, r28 = 2, luu = 2, tham = 0), res)
    }

    @Test
    fun testType2BeTong() {
        // P = 45 -> r28 = ceil(45/20) = 3, luu = ceil(3/3)*2 = 2, r7 = 0, tham = 0
        val res = calculateSampleResult("45", ConcreteType.BE_TONG)
        assertEquals(SampleResult(r7 = 0, r28 = 3, luu = 2, tham = 0), res)
    }

    @Test
    fun testType3BeTongCoTham() {
        // P = 65 -> r28 = ceil(65/20) = 4, luu = ceil(4/3)*2 = 4, r7 = 0, tham = ceil(65/500) = 1
        val res = calculateSampleResult("65", ConcreteType.BE_TONG_CO_THAM)
        assertEquals(SampleResult(r7 = 0, r28 = 4, luu = 4, tham = 1), res)
        assertEquals(9, res.total)

        // P = 500 -> tham = 1
        assertEquals(1, calculateSampleResult("500", ConcreteType.BE_TONG_CO_THAM).tham)
        // P = 501 -> tham = 2
        assertEquals(2, calculateSampleResult("501", ConcreteType.BE_TONG_CO_THAM).tham)
        // P = 1001 -> tham = 3
        assertEquals(3, calculateSampleResult("1001", ConcreteType.BE_TONG_CO_THAM).tham)
    }

    @Test
    fun testType4BeTongCoR7() {
        // P = 15 -> r7 = 1, r28 = 1, luu = 1, tham = 0
        val res = calculateSampleResult("15", ConcreteType.BE_TONG_CO_R7)
        assertEquals(SampleResult(r7 = 1, r28 = 1, luu = 1, tham = 0), res)
        assertEquals(3, res.total)
    }

    @Test
    fun testType5BeTongCoR7CoTham() {
        // P = 15 -> r7 = 1, r28 = 1, luu = 1, tham = 1
        val res = calculateSampleResult("15", ConcreteType.BE_TONG_CO_R7_CO_THAM)
        assertEquals(SampleResult(r7 = 1, r28 = 1, luu = 1, tham = 1), res)
        assertEquals(4, res.total)

        // P = 501 -> tham = 2
        val res501 = calculateSampleResult("501", ConcreteType.BE_TONG_CO_R7_CO_THAM)
        assertEquals(2, res501.tham)
        assertEquals(res501.r7 + res501.r28 + res501.luu + res501.tham, res501.total)
    }

    @Test
    fun testCommaDecimalAndPrecisionRounding() {
        // 20.000000001 rounded to 20.00 -> ceil(20.00/20) = 1 (not 2)
        val resPrecision = calculateSampleResult("20,000000001", ConcreteType.BE_TONG)
        assertEquals(1, resPrecision.r28)

        // 12,5 with comma
        val resComma = calculateSampleResult("12,5", ConcreteType.BE_TONG)
        assertEquals(1, resComma.r28)
    }
}
