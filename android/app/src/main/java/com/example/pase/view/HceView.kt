package com.example.pase.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.example.pase.model.FakeCardRepository
import com.example.pase.model.NfcCardData
import com.example.pase.service.EmulationBuffer

@Composable
fun HceView() {
    var selectedCard by remember { mutableStateOf<NfcCardData?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Selecciona una tarjeta para emular", style = MaterialTheme.typography.titleLarge)

        FakeCardRepository.tarjetas.forEach { tarjeta ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectedCard = tarjeta
                        EmulationBuffer.cardData = tarjeta
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Nombre: ${tarjeta.nombre}")
                    Text("Tipo: ${tarjeta.tipoUsuario}")
                    Text("Saldo: ${tarjeta.saldo}")
                    Text("Expira: ${tarjeta.fechaExpiracion}")
                }
            }
        }

        if (selectedCard != null) {
            Text("Tarjeta seleccionada: ${selectedCard!!.nombre}", color = MaterialTheme.colorScheme.primary)
        }
    }
}
