package uk.ac.aber.dcs.cs39440.myvitalife.model

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver

data class Food(
    val name: String = "",
    val kcal: Int = 0,
)

val foodListSaver: Saver<List<Food>, Any> = listSaver(
    save = { it },
    restore = { it }
)
