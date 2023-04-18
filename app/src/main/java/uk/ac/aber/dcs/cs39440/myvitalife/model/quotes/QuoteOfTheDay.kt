package uk.ac.aber.dcs.cs39440.myvitalife.model.quotes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * An object that manages the quote of the day.
 *
 * @_quote a private mutable state variable that tracks the quote of the day.
 * @property quote a public property that gets or sets the quote of the day.
 */
object QuoteOfTheDay {
    private val _quote = mutableStateOf(Quote("", ""))
    var quote: Quote by _quote
}