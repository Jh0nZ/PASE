package com.example.pase.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FactCheck
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

object NavigationData {
    val items = listOf(
        NavigationItem("Main", Icons.Default.Home, "main"),
        NavigationItem("Validacion", Icons.AutoMirrored.Filled.FactCheck, "validation"),
        NavigationItem("Page 3", Icons.Default.Favorite, "page3"),
        NavigationItem("Page 4", Icons.Default.Person, "page4")
    )
}