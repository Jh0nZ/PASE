package com.example.pase.controller

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.util.Log
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
            isNfcEnabled = nfcAdapter?.isEnabled == true
        )
    )
    val nfcState: MutableState<NfcState> = _nfcState

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

    fun processNfcIntent(intent: Intent) {
        val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG, Tag::class.java)

        tag?.let {
            val id = it.id
            if (id == null || id.isEmpty()) {
                Log.w("NfcProcessing", "NFC Tag ID is null or empty.")
                updateNfcState("Error: NFC Tag ID is invalid.", hasError = true)
                return
            }

            val hexId = id.joinToString("") { byte -> "%02X".format(byte) }

            try {
                val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
                if (rawMsgs != null) {
                    val messages = rawMsgs.map { it as android.nfc.NdefMessage }
                    val content = extractTextContent(messages)
                    
                    if (content != null) {
                        val validationResult = validateCardFormat(content)
                        if (validationResult.isValid) {
                            val tagInfo = NfcTagInfo(
                                id = hexId,
                                content = content,
                                parsedData = validationResult.cardData
                            )
                            updateNfcState(formatCardDisplay(validationResult.cardData!!), tagInfo = tagInfo)
                        } else {
                            val tagInfo = NfcTagInfo(
                                id = hexId,
                                content = content,
                                hasError = true,
                                errorMessage = validationResult.errorMessage
                            )
                            updateNfcState("Error: ${validationResult.errorMessage}", tagInfo = tagInfo, hasError = true)
                        }
                    } else {
                        val tagInfo = NfcTagInfo(
                            id = hexId,
                            hasError = true,
                            errorMessage = "No se encontró contenido de texto en la tarjeta"
                        )
                        updateNfcState("Error: No se encontró contenido de texto", tagInfo = tagInfo, hasError = true)
                    }
                } else {
                    val tagInfo = NfcTagInfo(
                        id = hexId,
                        hasError = true,
                        errorMessage = "No se encontraron mensajes NDEF"
                    )
                    updateNfcState("Error: No se encontraron mensajes NDEF", tagInfo = tagInfo, hasError = true)
                }
            } catch (e: Exception) {
                Log.e("NfcProcessing", "Error reading NDEF message", e)
                val tagInfo = NfcTagInfo(
                    id = hexId,
                    hasError = true,
                    errorMessage = e.message
                )
                updateNfcState("Error leyendo contenido: ${e.message}", tagInfo = tagInfo, hasError = true)
            }
        } ?: run {
            Log.w("NfcProcessing", "NFC Tag not found in intent.")
            updateNfcState("Error: NFC Tag not found.", hasError = true)
        }
    }

    private fun extractTextContent(messages: List<android.nfc.NdefMessage>): String? {
        for (message in messages) {
            for (record in message.records) {
                if (record.tnf == android.nfc.NdefRecord.TNF_WELL_KNOWN &&
                    record.type.contentEquals(android.nfc.NdefRecord.RTD_TEXT)) {

                    val payload = record.payload
                    val textEncoding = if ((payload[0].toInt() and 128) == 0) "UTF-8" else "UTF-16"
                    val languageCodeLength = payload[0].toInt() and 0x3F

                    return String(
                        payload, languageCodeLength + 1,
                        payload.size - languageCodeLength - 1,
                        charset(textEncoding)
                    )
                }
            }
        }
        return null
    }

    private fun updateNfcState(statusMessage: String, tagInfo: NfcTagInfo? = null, hasError: Boolean = false) {
        _nfcState.value = _nfcState.value.copy(
            statusMessage = statusMessage,
            tagInfo = tagInfo
        )
    }

    fun resetState() {
        _nfcState.value = NfcState(
            isNfcAvailable = nfcAdapter != null,
            isNfcEnabled = nfcAdapter?.isEnabled == true,
            statusMessage = "Waiting for NFC tag..."
        )
    }

    private fun validateCardFormat(content: String): CardValidationResult {
        val parts = content.split(",")
        
        if (parts.size != 5) {
            return CardValidationResult(
                isValid = false,
                errorMessage = "Formato inválido. Se esperan 5 campos separados por comas: id,nombre,saldo,tipo de usuario,fecha expiracion"
            )
        }

        val (cardId, nombre, saldo, tipoUsuario, fechaExpiracion) = parts.map { it.trim() }

        // Validar que ningún campo esté vacío
        if (cardId.isEmpty() || nombre.isEmpty() || saldo.isEmpty() || 
            tipoUsuario.isEmpty() || fechaExpiracion.isEmpty()) {
            return CardValidationResult(
                isValid = false,
                errorMessage = "Todos los campos son obligatorios"
            )
        }

        val cardData = NfcCardData(
            cardId = cardId,
            nombre = nombre,
            saldo = saldo,
            tipoUsuario = tipoUsuario,
            fechaExpiracion = fechaExpiracion
        )

        return CardValidationResult(
            isValid = true,
            cardData = cardData
        )
    }

    private fun formatCardDisplay(cardData: NfcCardData): String {
        return """
            ID: ${cardData.cardId}
            Nombre: ${cardData.nombre}
            Saldo: ${cardData.saldo}
            Tipo de Usuario: ${cardData.tipoUsuario}
            Fecha Expiración: ${cardData.fechaExpiracion}
        """.trimIndent()
    }

    private data class CardValidationResult(
        val isValid: Boolean,
        val cardData: NfcCardData? = null,
        val errorMessage: String? = null
    )
}