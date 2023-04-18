package uk.ac.aber.dcs.cs39440.myvitalife.model

/**
 * A data class representing a goal.
 *
 * @param title the title or description of the goal.
 * @param achieved a flag indicating whether the goal has been achieved or not.
 */
data class Goal(
    val title: String = "",
    val achieved: Boolean = false
)
