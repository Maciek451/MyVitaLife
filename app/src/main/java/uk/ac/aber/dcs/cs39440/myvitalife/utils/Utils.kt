package uk.ac.aber.dcs.cs39440.myvitalife.utils

import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        fun getCurrentDate(): String {
            val time = Calendar.getInstance().time
            val formatter = SimpleDateFormat("yyyy-MM-dd")

            return formatter.format(time)
        }
    }
}