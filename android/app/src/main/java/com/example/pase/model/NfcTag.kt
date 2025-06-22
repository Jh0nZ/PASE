package com.example.pase.model

data class NfcTagInfo(
    val id: String,
    val content: String? = null,
    val hasError: Boolean = false,
    val errorMessage: String? = null
)

data class NfcState(
    val isNfcAvailable: Boolean,
    val isNfcEnabled: Boolean,
    val tagInfo: NfcTagInfo? = null,
    val statusMessage: String = "Waiting for NFC tag..."
)