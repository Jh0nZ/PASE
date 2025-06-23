package com.example.pase.view

import android.app.Activity
import android.content.Intent
import android.nfc.NfcAdapter
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.pase.controller.NfcController
import com.example.pase.view.components.WaitingCard
import com.example.pase.view.components.ErrorCard
import com.example.pase.view.components.NfcCard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NfcView() {
    val context = LocalContext.current
    val activity = context as Activity
    val nfcController = remember { NfcController(activity) }

    DisposableEffect(nfcController) {
        nfcController.enableForegroundDispatch()
        onDispose {
            nfcController.disableForegroundDispatch()
        }
    }

    LaunchedEffect(Unit) {
        val intent = activity.intent
        if (isNfcIntent(intent)) {
            nfcController.processNfcIntent(intent)
        }
    }

    (context as ComponentActivity).addOnNewIntentListener { intent ->
        if (isNfcIntent(intent)) {
            nfcController.processNfcIntent(intent)
        }
    }

    val nfcState = nfcController.nfcState.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Mostrar contenido basado en el estado
            nfcState.tagInfo?.let { tagInfo ->
                if (!tagInfo.hasError && tagInfo.parsedData != null) {
                    Log.d("NfcView", "Parsed Data: ${tagInfo.parsedData}")
                    NfcCard(cardData = tagInfo.parsedData)

                    // Verificar validez de la tarjeta según fecha de expiración
                    val isValid = isCardValid(tagInfo.parsedData.fechaExpiracion)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        if (isValid) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Tarjeta válida",
                                tint = Color.Green,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Tarjeta válida",
                                color = Color.Green,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Tarjeta inválida",
                                tint = Color.Red,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Tarjeta inválida - Expirada",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                } else {
                    ErrorCard(errorMessage = tagInfo.errorMessage ?: "Error desconocido")
                }
            } ?: run {
                WaitingCard()
            }

            // Mensajes de estado del NFC
            if (!nfcState.isNfcAvailable) {
                Text(
                    "NFC no está disponible en este dispositivo",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            } else if (!nfcState.isNfcEnabled) {
                Text(
                    "Por favor habilita el NFC",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Botón para resetear
            if (nfcState.tagInfo != null) {
                Button(
                    onClick = { nfcController.resetState() },
                    modifier = Modifier.padding(top = 16.dp),
                ) {
                    Text("Leer otra tarjeta")
                }
            }
            /*
            // Simulate valid/invalid card buttons for testing
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    nfcController.nfcState.value = nfcController.nfcState.value.copy(
                        tagInfo = com.example.pase.model.NfcTagInfo(
                            id = "TEST123",
                            content = "123,Juan Perez,100.0,Estudiante,2099-12-31",
                            parsedData = com.example.pase.model.NfcCardData(
                                cardId = "123",
                                nombre = "Juan Perez",
                                saldo = "100.0",
                                tipoUsuario = "Estudiante",
                                fechaExpiracion = "2025-06-31"
                            )
                        ),
                        statusMessage = "Tarjeta simulada válida"
                    )
                }) {
                    Text("Simular válida")
                }
                Button(onClick = {
                    nfcController.nfcState.value = nfcController.nfcState.value.copy(
                        tagInfo = com.example.pase.model.NfcTagInfo(
                            id = "TEST456",
                            content = "456,Ana Ruiz,50.0,General,2000-01-01",
                            parsedData = com.example.pase.model.NfcCardData(
                                cardId = "456",
                                nombre = "Ana Ruiz",
                                saldo = "50.0",
                                tipoUsuario = "General",
                                fechaExpiracion = "2000-01-01"
                            )
                        ),
                        statusMessage = "Tarjeta simulada inválida"
                    )
                }) {
                    Text("Simular inválida")
                }
            }
            */
        }
    }
}

/**
 * Verifica si la tarjeta es válida comparando su fecha de expiración con la fecha actual
 */
private fun isCardValid(expirationDateStr: String): Boolean {
    try {
        // Asumimos que la fecha viene en formato "dd/MM/yyyy"
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val expirationDate = sdf.parse(expirationDateStr) ?: return false
        val currentDate = Date()

        // La tarjeta es válida si la fecha de expiración es posterior a la fecha actual
        return expirationDate.after(currentDate)
    } catch (e: Exception) {
        Log.e("NfcView", "Error al parsear la fecha de expiración", e)
        return false
    }
}

private fun isNfcIntent(intent: Intent): Boolean {
    return intent.action in listOf(
        NfcAdapter.ACTION_NDEF_DISCOVERED,
        NfcAdapter.ACTION_TECH_DISCOVERED,
        NfcAdapter.ACTION_TAG_DISCOVERED
    )
}