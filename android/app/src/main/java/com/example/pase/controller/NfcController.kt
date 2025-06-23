package com.example.pase.controller

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.pase.model.NfcCardData
import com.example.pase.model.NfcState
import com.example.pase.model.NfcTagInfo

class NfcController(private val activity: Activity) {
    private val nfcAdapter = NfcAdapter.getDefaultAdapter(activity)
    private val _nfcState = mutableStateOf(
        NfcState(
            isNfcAvailable = nfcAdapter != null,
            isNfcEnabled = nfcAdapter?.isEnabled == true,
            statusMessage = "Esperando tarjeta NFC..."
        )
    )
    val nfcState: MutableState<NfcState> = _nfcState

    private val SELECT_APDU = byteArrayOf(
        0x00.toByte(), 0xA4.toByte(), 0x04.toByte(), 0x00.toByte(), 0x05.toByte(),
        0xF2.toByte(), 0x22.toByte(), 0x22.toByte(), 0x22.toByte(), 0x22.toByte()
    )

    private val pendingIntent = PendingIntent.getActivity(
        activity, 0,
        Intent(activity, activity.javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
        PendingIntent.FLAG_MUTABLE
    )

    fun enableForegroundDispatch() {
        nfcAdapter?.enableForegroundDispatch(
            activity,
            pendingIntent,
            null,
            null
        )
    }

    fun disableForegroundDispatch() {
        nfcAdapter?.disableForegroundDispatch(activity)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun processNfcIntent(intent: Intent) {
        if (intent.action in listOf(
                NfcAdapter.ACTION_TECH_DISCOVERED,
                NfcAdapter.ACTION_TAG_DISCOVERED
            )
        ) {
            processTag(intent)
        }
    }

    private fun processTag(intent: Intent) {
        val tag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(NfcAdapter.EXTRA_TAG, Tag::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        }

        if (tag == null) {
            updateNfcState("Error: No se detectó ninguna etiqueta NFC", hasError = true)
            return
        }

        val hexId = tag.id.joinToString("") { byte -> "%02X".format(byte) }
        val isoDep = IsoDep.get(tag)
        if (isoDep != null) {
            try {
                isoDep.connect()
                val response = isoDep.transceive(SELECT_APDU)
                if (response != null && response.size > 2) {
                    val data = response.copyOfRange(0, response.size - 2)
                    val content = String(data)
                    Log.d("NfcController", "Contenido de la tarjeta: $content")
                    val parts = content.split(",")
                    if (parts.size == 5) {
                        val (cardId, nombre, saldoRaw, tipoUsuario, fechaExpiracion) = parts.map { it.trim() }
                        // Remove non-numeric characters (except dot and minus)
                        val saldo = saldoRaw.replace(Regex("[^\\d.-]"), "")
                        try {
                            saldo.toDouble()
                            val cardData = NfcCardData(cardId, nombre, saldo, tipoUsuario, fechaExpiracion)
                            val tagInfo = NfcTagInfo(
                                id = hexId,
                                content = content,
                                parsedData = cardData
                            )
                            updateNfcState("Tarjeta leída correctamente", tagInfo = tagInfo)
                        } catch (e: NumberFormatException) {
                            updateNfcState("Error: saldo inválido", hasError = true)
                        }
                    } else {
                        updateNfcState("Error: Formato inválido, se esperaban 5 campos", hasError = true)
                    }
                } else {
                    updateNfcState("Error: No se recibieron datos", hasError = true)
                }
            } catch (e: Exception) {
                updateNfcState("Error leyendo tarjeta: ${e.message}", hasError = true)
            } finally {
                try { isoDep.close() } catch (_: Exception) {}
            }
        } else {
            updateNfcState("La tarjeta no soporta ISO-DEP", hasError = true)
        }
    }

    private fun updateNfcState(statusMessage: String, tagInfo: NfcTagInfo? = null, hasError: Boolean = false) {
        _nfcState.value = _nfcState.value.copy(
            statusMessage = statusMessage,
            tagInfo = tagInfo,
        )
    }

    fun resetState() {
        _nfcState.value = NfcState(
            isNfcAvailable = nfcAdapter != null,
            isNfcEnabled = nfcAdapter?.isEnabled == true,
            statusMessage = "Esperando tarjeta NFC..."
        )
    }
}