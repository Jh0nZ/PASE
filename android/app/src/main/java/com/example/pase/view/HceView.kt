package com.example.pase.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.example.pase.model.FakeCardRepository
import com.example.pase.model.NfcCardData
import com.example.pase.service.EmulationBuffer
import com.example.pase.view.components.NfcCard
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@Composable
fun HceView() {
    var selectedCard by remember { mutableStateOf<NfcCardData?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (selectedCard != null)
                "Emulando tarjeta de: ${selectedCard!!.nombre}"
            else
                "Selecciona una tarjeta para emular",
            style = MaterialTheme.typography.titleLarge
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(FakeCardRepository.tarjetas) { tarjeta ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedCard = tarjeta
                            EmulationBuffer.cardData = tarjeta
                        }
                ) {
                    NfcCard(cardData = tarjeta)
                }
            }
        }
    }
}