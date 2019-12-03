package com.fastival.unittestex.util

import com.fastival.unittestex.util.DateUtil.Companion.GET_MONTH_ERROR
import com.fastival.unittestex.util.DateUtil.Companion.getMonthFromNumber
import com.fastival.unittestex.util.DateUtil.Companion.monthNumbers
import com.fastival.unittestex.util.DateUtil.Companion.months
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.TestReporter
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.util.*

class DateUtilTest {

    companion object {
        const val TODAY = "11-2019"
    }

    @Test
    internal fun testGetCurrentTimestamp_returnedTimestamp(){
        assertDoesNotThrow {
            assertEquals(TODAY, DateUtil.getCurrentTimeStamp())
            println("Timestamp is generated correctly")
        }
    }

    @ParameterizedTest
    @ValueSource(ints = [0,1,2,3,4,5,6,7,8,9,10,11])
    internal fun getMonthFromNumber_returnSuccess(monthNumber: Int, testInfo: TestInfo, testReporter: TestReporter){
        assertEquals(months[monthNumber], getMonthFromNumber(monthNumbers[monthNumber]))
        println("${monthNumbers[monthNumber]} : ${months[monthNumber]}")
    }

    @ParameterizedTest
    @ValueSource(ints = [1,2,3,4,5,6,7,8,9,10,11])
    internal fun testGetMonthFromNumber_returnError(monthNumber: Int, testInfo: TestInfo, testReporter: TestReporter) {
        val randomInt = Random().nextInt(90) + 13
        assertEquals(getMonthFromNumber((monthNumber * randomInt).toString()), GET_MONTH_ERROR)
        println(monthNumbers[monthNumber] + " : " + GET_MONTH_ERROR)
    }

}