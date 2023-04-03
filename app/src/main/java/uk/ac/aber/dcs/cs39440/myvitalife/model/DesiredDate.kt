package uk.ac.aber.dcs.cs39440.myvitalife.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import uk.ac.aber.dcs.cs39440.myvitalife.utils.Utils

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