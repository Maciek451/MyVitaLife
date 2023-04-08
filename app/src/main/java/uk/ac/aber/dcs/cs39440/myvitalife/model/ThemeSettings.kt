package uk.ac.aber.dcs.cs39440.myvitalife.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object ThemeSettings {
    private val _isDarkTheme = mutableStateOf( true )
    var isDarkTheme: Boolean by _isDarkTheme
}