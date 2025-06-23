package com.example.pase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pase.view.theme.PASETheme
import com.example.pase.view.MainPageView
import com.example.pase.view.NavigationView
import com.example.pase.view.NfcView
import com.example.pase.view.HceView
import com.example.pase.view.Page4View

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PASETheme {
                NavigationView { route ->
                    when (route) {
                        "Validar" -> NfcView()
                        "Emular" -> HceView()
                        else -> NfcView()
                    }
                }
            }
        }
    }
}