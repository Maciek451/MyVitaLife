package uk.ac.aber.dcs.cs39440.myvitalife.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import uk.ac.aber.dcs.cs39440.myvitalife.utils.Utils

object DesiredDate {
    var date: String by mutableStateOf(Utils.getCurrentDate())
    val dateChangeListeners = mutableListOf<() -> Unit>()

    fun notifyDateChangeListeners() {
        for (listener in dateChangeListeners) {
            listener.invoke()
        }
    }
}