package com.example.pase.service

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log

class CardService : HostApduService() {

    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        val card = EmulationBuffer.cardData

        if (card == null) {
            Log.d("CardService", "No hay tarjeta seleccionada para emular")
            return byteArrayOf(0x6A.toByte(), 0x82.toByte()) // SW_FILE_NOT_FOUND
        }

        val message = "${card.cardId},${card.nombre},${card.tipoUsuario},${card.saldo},${card.fechaExpiracion}"
        Log.d("CardService", "Tarjeta emulada: $message")

        val responseData = message.toByteArray(Charsets.UTF_8)
        val statusWord = byteArrayOf(0x90.toByte(), 0x00.toByte()) // SW_SUCCESS
        return responseData + statusWord
    }

    override fun onDeactivated(reason: Int) {
        Log.d("CardService", "Emulación desactivada. Razón: $reason")
    }
}
