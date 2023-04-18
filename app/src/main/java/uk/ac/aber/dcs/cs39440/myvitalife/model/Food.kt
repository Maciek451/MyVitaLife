package uk.ac.aber.dcs.cs39440.myvitalife.model

/**
 * A data class representing a food item.
 *
 * @param name the name of the food item.
 * @param amount the amount of the food item in grams.
 * @param kcal the number of calories in the food item.
 */
data class Food(
    val name: String = "",
    val amount: Int = 0,
    val kcal: Int = 0,
)