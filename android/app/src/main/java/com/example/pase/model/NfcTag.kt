package com.example.pase.model

data class NfcTagInfo(
    val id: String,
    val content: String? = null,
    val hasError: Boolean = false,
    val errorMessage: String? = null,
    val parsedData: NfcCardData? = null
)

data class NfcCardData(
    val cardId: String,
    val nombre: String,
    val saldo: String,
    val tipoUsuario: String,
    val fechaExpiracion: String
)

data class NfcState(
    val isNfcAvailable: Boolean,
    val isNfcEnabled: Boolean,
    val tagInfo: NfcTagInfo? = null,
    val statusMessage: String = "Waiting for NFC tag..."
)

fun NfcCardData.toApduResponse(): ByteArray {
    val responseString = "ID:$cardId;N:$nombre;S:$saldo;T:$tipoUsuario;F:$fechaExpiracion"
    return responseString.toByteArray(Charsets.UTF_8)
}
