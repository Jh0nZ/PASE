package com.example.pase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.pase.ui.theme.PASETheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PASETheme {
                var selectedIndex by remember { mutableIntStateOf(0) }
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    containerColor = Color(0xFFDEDEDE),
                    bottomBar = {
                        NavigationBar(
                            containerColor = Color(0xFFDEDEDE)
                        ) {
                            destinations.forEachIndexed { index, destination ->
                                NavigationBarItem(
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = Color(0xFF6CB2F1),
                                        unselectedIconColor = Color(0xFF000000),
                                        selectedTextColor = Color(0xFF2FAFF1),
                                        unselectedTextColor = Color(0xFF525252),
                                        indicatorColor = Color(0xFF1C1C1C)
                                    ),
                                    selected = selectedIndex == index,
                                    onClick = { selectedIndex = index },
                                    icon = { Icon(destination.icon, contentDescription = destination.label) },
                                    label = { Text(destination.label) }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize().padding(innerPadding).background(Color(
                        0xFFF8F8F8
                    )
                    )) {
                        destinations[selectedIndex].content()
                    }
                }
            }
        }
    }
}


sealed class NavDestination(
    val label: String,
    val icon: ImageVector,
    val content: @Composable () -> Unit
) {
    object Main : NavDestination("Main", Icons.Default.Home, { MainPage() })
    object Page2 : NavDestination("Validacion", Icons.Default.CheckCircle, { com.example.pase.ui.theme.Page2() })
    object Page3 : NavDestination("Page 3", Icons.Default.Favorite, { Page3() })
    object Page4 : NavDestination("Page 4", Icons.Default.Person, { Page4() })
}

val destinations = listOf(
    NavDestination.Main,
    NavDestination.Page2,
    NavDestination.Page3,
    NavDestination.Page4
)

@Composable
fun MainPage() {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("texto en columna")
    }
}

@Composable
fun Page3() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Page 3")
    }
}

@Composable
fun Page4() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Page 4")
    }
}