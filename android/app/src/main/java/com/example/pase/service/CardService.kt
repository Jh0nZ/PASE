package com.example.pase.service

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log

class CardService : HostApduService() {

    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        val card = EmulationBuffer.cardData

        val response = if (card != null) {
            val content = "${card.nombre},${card.tipoUsuario},${card.saldo}"
            Log.d("CardService", "Respondiendo con: $content")
            content.toByteArray() + byteArrayOf(0x90.toByte(), 0x00.toByte())
        } else {
            Log.d("CardService", "Sin tarjeta seleccionada")
            "SIN TARJETA".toByteArray() + byteArrayOf(0x6A.toByte(), 0x82.toByte()) // SW_FILE_NOT_FOUND
        }

        return response
    }

    override fun onDeactivated(reason: Int) {
        Log.d("CardService", "Servicio HCE desactivado. Razón: $reason")
    }
}
