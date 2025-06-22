package com.example.pase.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.pase.controller.NavigationController
import com.example.pase.model.NavigationData

@Composable
fun NavigationView(content: @Composable (String) -> Unit) {
    val navigationController = remember { NavigationController() }
    val selectedIndex = navigationController.selectedIndex.value

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFDEDEDE),
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFDEDEDE)
            ) {
                NavigationData.items.forEachIndexed { index, navigationItem ->
                    NavigationBarItem(
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF6CB2F1),
                            unselectedIconColor = Color(0xFF000000),
                            selectedTextColor = Color(0xFF2FAFF1),
                            unselectedTextColor = Color(0xFF525252),
                            indicatorColor = Color(0xFF1C1C1C)
                        ),
                        selected = selectedIndex == index,
                        onClick = { navigationController.selectTab(index) },
                        icon = { Icon(navigationItem.icon, contentDescription = navigationItem.label) },
                        label = { Text(navigationItem.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8F8F8))
        ) {
            content(navigationController.getCurrentRoute())
        }
    }
}