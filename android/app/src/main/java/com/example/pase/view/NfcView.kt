package com.example.pase.view

import android.app.Activity
import android.content.Intent
import android.nfc.NfcAdapter
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.pase.controller.NfcController

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

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(nfcState.statusMessage)
            
            if (!nfcState.isNfcAvailable) {
                Text("NFC no esta disponible en este dispositivo", color = Color.Red)
            } else if (!nfcState.isNfcEnabled) {
                Text("Por favor habilita el NFC", color = Color.Red)
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