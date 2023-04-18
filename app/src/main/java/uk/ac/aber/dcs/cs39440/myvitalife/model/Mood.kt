package uk.ac.aber.dcs.cs39440.myvitalife.model

/**
 * A data class representing a mood entry.
 *
 * @param type an integer representing the type of mood.
 * @param description a description of the mood.
 * @param time the time at which the mood was recorded.
 */
data class Mood(
    val type: Int = 0,
    val description: String = "",
    val time: String = ""
)
