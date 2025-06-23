package com.example.pase.service

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log

class CardService : HostApduService() {

    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        val apduCommand = commandApdu?.joinToString(" ") { "%02X".format(it) }
        Log.d("CardService", "Comando APDU recibido: $apduCommand")

        val responseData = "PASE-OK".toByteArray()
        val statusWord = byteArrayOf(0x90.toByte(), 0x00.toByte()) // SW_SUCCESS
        return responseData + statusWord
    }

    override fun onDeactivated(reason: Int) {
        Log.d("CardService", "Servicio desactivado, razón: $reason")
    }
}
