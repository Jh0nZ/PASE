package com.example.pase

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.pase.model.FakeCardRepository
import com.example.pase.service.EmulationBuffer
import com.example.pase.view.HceView
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class HceViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysDefaultMessageWhenNoCardSelected() {
        composeTestRule.setContent {
            HceView()
        }
        composeTestRule.onNodeWithText("Selecciona una tarjeta para emular").assertExists()
    }

    @Test
    fun displaysSelectedCardMessageWhenCardIsSelected() {
        val testCard = FakeCardRepository.tarjetas.first()
        composeTestRule.setContent {
            HceView()
        }
        composeTestRule.onNodeWithText(testCard.nombre).performClick()
        composeTestRule.onNodeWithText("Emulando tarjeta de: ${testCard.nombre}").assertExists()
    }

    @Test
    fun updatesEmulationBufferWhenCardIsSelected() {
        val testCard = FakeCardRepository.tarjetas.first()
        composeTestRule.setContent {
            HceView()
        }
        composeTestRule.onNodeWithText(testCard.nombre).performClick()
        Assert.assertEquals(testCard, EmulationBuffer.cardData)
    }

    @Test
    fun handlesEmptyCardListGracefully() {
        val originalTarjetas = FakeCardRepository.tarjetas
        FakeCardRepository.tarjetas = emptyList()
        composeTestRule.setContent {
            HceView()
        }
        composeTestRule.onNodeWithText("Selecciona una tarjeta para emular").assertExists()
        FakeCardRepository.tarjetas = originalTarjetas
    }
}