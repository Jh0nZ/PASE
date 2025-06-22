package com.example.pase.controller

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import com.example.pase.model.NavigationData

class NavigationController {
    private val _selectedIndex = mutableIntStateOf(0)
    val selectedIndex: MutableState<Int> = _selectedIndex

    fun selectTab(index: Int) {
        if (index in NavigationData.items.indices) {
            _selectedIndex.intValue = index
        }
    }

    fun getCurrentRoute(): String {
        return NavigationData.items[_selectedIndex.intValue].route
    }
}