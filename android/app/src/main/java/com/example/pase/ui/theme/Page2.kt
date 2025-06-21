package com.example.pase.ui.theme

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun Page2() {
    val context = LocalContext.current
    val nfcAdapter = remember { NfcAdapter.getDefaultAdapter(context) }
    val nfcContent = remember { mutableStateOf("Waiting for NFC tag...") }
    val activity = context as Activity
    val pendingIntent = remember {
        PendingIntent.getActivity(
            context, 0,
            Intent(context, context.javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            PendingIntent.FLAG_MUTABLE
        )
    }

    DisposableEffect(nfcAdapter) {
        nfcAdapter?.enableForegroundDispatch(
            activity,
            pendingIntent,
            null,
            null
        )

        onDispose {
            nfcAdapter?.disableForegroundDispatch(activity)
        }
    }

    // Process NFC intent if activity was launched by NFC
    LaunchedEffect(Unit) {
        val intent = activity.intent
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action ||
            NfcAdapter.ACTION_TECH_DISCOVERED == intent.action ||
            NfcAdapter.ACTION_TAG_DISCOVERED == intent.action) {
            processNfcIntent(intent, nfcContent)
        }
    }

    // Handle tag detection in the activity
    (context as ComponentActivity).addOnNewIntentListener { intent ->
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action ||
            NfcAdapter.ACTION_TECH_DISCOVERED == intent.action ||
            NfcAdapter.ACTION_TAG_DISCOVERED == intent.action) {
            processNfcIntent(intent, nfcContent)
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(nfcContent.value)
            if (nfcAdapter == null) {
                Text("NFC no esta disponible en este dispositivo", color = Color.Red)
            } else if (!nfcAdapter.isEnabled) {
                Text("Por favor habilita el NFC", color = Color.Red)
            }
        }
    }
}

private fun processNfcIntent(intent: Intent, nfcContent: MutableState<String>) {
    val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG, Tag::class.java)

    tag?.let {
        // First show the tag ID
        val id = it.id
        if (id == null || id.isEmpty()) {
            Log.w("NfcProcessing", "NFC Tag ID is null or empty.")
            nfcContent.value = "Error: NFC Tag ID is invalid."
            return
        }
        val hexId = id.joinToString("") { byte -> "%02X".format(byte) }

        try {
            val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            if (rawMsgs != null) {
                val messages = rawMsgs.map { it as android.nfc.NdefMessage }
                val sb = StringBuilder()

                // Display tag ID
                sb.append("NFC Tag ID: $hexId\n\n")

                // Display text content
                for (message in messages) {
                    for (record in message.records) {
                        if (record.tnf == android.nfc.NdefRecord.TNF_WELL_KNOWN &&
                            record.type.contentEquals(android.nfc.NdefRecord.RTD_TEXT)) {

                            val payload = record.payload
                            // The first byte contains status info
                            val textEncoding = if ((payload[0].toInt() and 128) == 0) "UTF-8" else "UTF-16"
                            val languageCodeLength = payload[0].toInt() and 0x3F

                            // Get the text
                            val text = String(
                                payload, languageCodeLength + 1,
                                payload.size - languageCodeLength - 1,
                                charset(textEncoding)
                            )
                            sb.append("Content: $text")
                        }
                    }
                }

                nfcContent.value = if (sb.length > hexId.length + 2) sb.toString() else "NFC Tag ID: $hexId\n(No text content found)"
            } else {
                nfcContent.value = "NFC Tag ID: $hexId\n(No NDEF messages found)"
            }
        } catch (e: Exception) {
            Log.e("NfcProcessing", "Error reading NDEF message", e)
            nfcContent.value = "NFC Tag ID: $hexId\n(Error reading content: ${e.message})"
        }
    } ?: run {
        Log.w("NfcProcessing", "NFC Tag not found in intent.")
        nfcContent.value = "Error: NFC Tag not found."
    }
}