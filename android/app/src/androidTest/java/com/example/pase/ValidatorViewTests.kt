package com.example.pase

import com.example.pase.view.isCardValid
import org.junit.Assert.*
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class ValidatorViewTests {

    @Test
    fun isCardValid_returnsTrue_whenExpirationDateIsInFuture() {
        val expirationDate = "2099-12-31"
        val result = isCardValid(expirationDate)
        assertTrue(result)
    }

    @Test
    fun isCardValid_returnsFalse_whenExpirationDateIsInPast() {
        val expirationDate = "2000-01-01"
        val result = isCardValid(expirationDate)
        assertFalse(result)
    }

    @Test
    fun isCardValid_returnsFalse_whenExpirationDateIsToday() {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val expirationDate = sdf.format(Date())
        val result = isCardValid(expirationDate)
        assertFalse(result)
    }

    @Test
    fun isCardValid_returnsFalse_whenExpirationDateIsInvalidFormat() {
        val expirationDate = "31-12-2099"
        val result = isCardValid(expirationDate)
        assertFalse(result)
    }

    @Test
    fun isCardValid_returnsFalse_whenExpirationDateIsEmpty() {
        val expirationDate = ""
        val result = isCardValid(expirationDate)
        assertFalse(result)
    }

    @Test
    fun isCardValid_returnsFalse_whenExpirationDateIsNull() {
        val expirationDate: String? = null
        val result = isCardValid(expirationDate ?: "")
        assertFalse(result)
    }
}