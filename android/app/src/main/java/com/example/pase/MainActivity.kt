package com.example.pase

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.tech.Ndef
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pase.ui.theme.PASETheme

class MainActivity : ComponentActivity() {

    private var nfcAdapter: NfcAdapter? = null
    private lateinit var pendingIntent: PendingIntent
    private lateinit var nfcIntentFilters: Array<IntentFilter>

    private val nfcMessage = mutableStateOf("Esperando lectura NFC...")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // NFC setup
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            Toast.makeText(this, "Este dispositivo no soporta NFC", Toast.LENGTH_LONG).show()
            finish()
        }

        val intent = Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)

        val filter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED)
        try {
            filter.addDataType("text/plain")
            nfcIntentFilters = arrayOf(filter)
        } catch (e: IntentFilter.MalformedMimeTypeException) {
            throw RuntimeException("Error en tipo MIME", e)
        }

        setContent {
            PASETheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NFCReaderUI(
                        message = nfcMessage.value,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, pendingIntent, nfcIntentFilters, null)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {
            val rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            if (!rawMessages.isNullOrEmpty()) {
                val message = rawMessages[0] as NdefMessage
                val record = message.records[0]
                val payload = record.payload
                val text = String(payload, Charsets.UTF_8).drop(3) // Quitar código de idioma
                Toast.makeText(this, "Leído: $text", Toast.LENGTH_LONG).show()
                nfcMessage.value = "Contenido leído: $text"
            } else {
                nfcMessage.value = "No se pudo leer el contenido NFC"
            }
        }
    }
}

@Composable
fun NFCReaderUI(message: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Lectura NFC",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = message, style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewNFCReaderUI() {
    PASETheme {
        NFCReaderUI("Esperando lectura NFC...")
    }
}