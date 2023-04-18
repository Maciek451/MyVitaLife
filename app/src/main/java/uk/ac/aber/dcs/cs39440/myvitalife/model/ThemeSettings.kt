package uk.ac.aber.dcs.cs39440.myvitalife.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * An object that manages the dark theme state of the app.
 *
 * @_isDarkTheme a private mutable state variable that tracks the dark theme state.
 * @property isDarkTheme a public boolean property that gets or sets the dark theme state.
 */
object ThemeSettings {
    private val _isDarkTheme = mutableStateOf( true )
    var isDarkTheme: Boolean by _isDarkTheme
}