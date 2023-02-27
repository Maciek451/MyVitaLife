package uk.ac.aber.dcs.cs.cs31620.majorproject.ui.navigation

sealed class Screen(
    val route: String
)   {
    object StartScreen : Screen("start_screen")
    object DailySteps : Screen("daily_steps")
    object Insights : Screen("insights")
    object Journal : Screen("journal")
    object Nutrition : Screen("nutrition")
    object Sleep : Screen("sleep")
    object ProvideName : Screen("provide_name")
}
val screens = listOf(
    Screen.DailySteps,
    Screen.Insights,
    Screen.Journal,
    Screen.Nutrition,
    Screen.Sleep,
)