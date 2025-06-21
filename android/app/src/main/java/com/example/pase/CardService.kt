import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log

class CardService : HostApduService() {
    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        Log.d("CardService", "APDU received: ${commandApdu?.toHex()}")
        // Respuesta simulada para validación (ejemplo simple)
        return "9000".hexToByteArray() // "9000" = éxito en APDU
    }

    override fun onDeactivated(reason: Int) {
        Log.d("CardService", "Deactivated: $reason")
    }

    // Función de utilidad para convertir bytes a string hex
    private fun ByteArray.toHex(): String = joinToString("") { "%02X".format(it) }

    private fun String.hexToByteArray(): ByteArray =
        chunked(2).map { it.toInt(16).toByte() }.toByteArray()
}
