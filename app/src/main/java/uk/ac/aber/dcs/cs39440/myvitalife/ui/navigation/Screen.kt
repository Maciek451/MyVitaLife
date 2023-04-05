package uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation

sealed class Screen(
    val route: String
)   {
    object StartScreen : Screen("start_screen")
    object Steps : Screen("steps")
    object Insights : Screen("insights")
    object Journal : Screen("journal")
    object Nutrition : Screen("nutrition")
    object Sleep : Screen("sleep")
    object ProvideName : Screen("provide_name")
    object AddSleep : Screen("add_sleep")
    object AddMoodOrGoal : Screen("add_mood_or_goal")
    object AddMood : Screen("add_mood")
    object Account : Screen("account")
    object LoginSignIn : Screen("login_sign_in")
}
val screens = listOf(
    Screen.Steps,
    Screen.Journal,
    Screen.Insights,
    Screen.Nutrition,
    Screen.Sleep,
)