package uk.ac.aber.dcs.cs39440.myvitalife.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import uk.ac.aber.dcs.cs39440.myvitalife.utils.Utils

/**
 * An object representing a desired date that can be listened to for changes.
 *
 * @property dateChangeListeners A mutable list of functions to be called when the date changes.
 * @property _date mutable state holding the current date value.
 * @property date string representing the current date.
 */
object DesiredDate {
    val dateChangeListeners = mutableListOf<() -> Unit>()

    private val _date = mutableStateOf(Utils.getCurrentDate())
    var date: String by _date
    fun notifyDateChangeListeners() {
        for (listener in dateChangeListeners) {
            listener.invoke()
        }
    }
}