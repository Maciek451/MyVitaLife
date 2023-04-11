package uk.ac.aber.dcs.cs39440.myvitalife.model.quotes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object QuoteOfTheDay {
    private val _quote = mutableStateOf(Quote("", ""))
    var quote: Quote by _quote
}