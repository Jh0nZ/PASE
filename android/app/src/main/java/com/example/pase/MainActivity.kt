package com.example.pase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.pase.ui.theme.PASETheme
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
                        "main" -> MainPageView()
                        "validation" -> NfcView()
                        "page3" -> HceView()
                        "page4" -> Page4View()
                        else -> MainPageView()
                    }
                }
            }
        }
    }
}