package uk.ac.aber.dcs.cs39440.myvitalife.model

/**
 * A data class representing a water intake entry.
 *
 * @param cupSize the size of a cup.
 * @param hydrationGoal the daily hydration goal.
 * @param waterDrunk the amount of water drunk so far today.
 */
data class Water(
    val cupSize: Int = 0,
    val hydrationGoal: Int = 0,
    val waterDrunk: Int = 0
)
