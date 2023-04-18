package uk.ac.aber.dcs.cs39440.myvitalife.model

/**
 * A data class representing a sleep entry.
 *
 * @param score the score of the sleep.
 * @param start the start time of the sleep in ISO 8601 format.
 * @param end the end time of the sleep in ISO 8601 format.
 * @param duration the duration of the sleep in HH:MM format.
 * @param note an optional note about the sleep.
 */
data class Sleep(
    val score: Float = 0f,
    val start: String = "",
    val end: String = "",
    val duration: String = "",
    val note: String = ""
)
