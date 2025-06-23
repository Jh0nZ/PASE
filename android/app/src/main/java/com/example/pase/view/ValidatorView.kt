package com.example.pase.view

import android.app.Activity
import android.content.Intent
import android.nfc.NfcAdapter
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pase.controller.NfcController
import com.example.pase.model.NfcCardData
import com.example.pase.view.components.WaitingCard
import com.example.pase.view.components.ErrorCard
import com.example.pase.view.components.NfcCard

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
                    NfcCard(cardData = tagInfo.parsedData)
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
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Leer otra tarjeta")
                }
            }
        }
    }
}

private fun isNfcIntent(intent: Intent): Boolean {
    return intent.action in listOf(
        NfcAdapter.ACTION_NDEF_DISCOVERED,
        NfcAdapter.ACTION_TECH_DISCOVERED,
        NfcAdapter.ACTION_TAG_DISCOVERED
    )
}