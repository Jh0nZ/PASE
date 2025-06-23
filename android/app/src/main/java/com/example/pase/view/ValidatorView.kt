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
import androidx.compose.material.icons.filled.Close
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

@Composable
fun NfcCard(cardData: NfcCardData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF6366F1),
                            Color(0xFF8B5CF6)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PASE Card",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "Card",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
                
                // Card ID
                Text(
                    text = cardData.cardId,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 2.sp
                )
                
                // Card Info
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    CardInfoRow(
                        icon = Icons.Default.Person,
                        label = "Nombre",
                        value = cardData.nombre
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CardInfoRow(
                            icon = Icons.Default.AccountCircle,
                            label = "Tipo",
                            value = cardData.tipoUsuario,
                            modifier = Modifier.weight(1f)
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        CardInfoRow(
                            icon = Icons.Default.DateRange,
                            label = "Vence",
                            value = cardData.fechaExpiracion,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    
                    // Saldo destacado
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.2f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Saldo Disponible",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 12.sp
                            )
                            Text(
                                text = "$${cardData.saldo}",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardInfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White.copy(alpha = 0.8f),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = label,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 10.sp
            )
            Text(
                text = value,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ErrorCard(errorMessage: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Error al leer la tarjeta",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@Composable
fun WaitingCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Face,
                contentDescription = "Waiting",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Acerca tu tarjeta NFC",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Esperando detección...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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