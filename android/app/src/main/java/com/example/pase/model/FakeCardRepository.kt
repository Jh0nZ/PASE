package com.example.pase.model

object FakeCardRepository {
    var tarjetas = listOf(
        NfcCardData("4e4c9620-5fc3-4dbf-90f4-ea4706d62b83", "Juan Pérez", "10", "Estudiante", "2025-12-31"),
        NfcCardData("1103eae5-df6e-4d91-8e83-0fd44c0bc9b1", "Ana Gómez", "15", "Normal", "2025-11-30"),
        NfcCardData("9db97971-d01f-4ed1-a94b-ff3f0d9a2aa6", "Luis Quispe", "8.90","Estudiante", "2025-10-15")
    )
}