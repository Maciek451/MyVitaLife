package uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation

sealed class Screen(
    val route: String
)   {
    object Steps : Screen("steps")
    object Insights : Screen("insights")
    object Journal : Screen("journal")
    object Nutrition : Screen("nutrition")
    object Sleep : Screen("sleep")
    object SignIn : Screen("sign_in")
    object SignUp : Screen("sign_up")
    object AddSleep : Screen("add_sleep")
    object Account : Screen("account")
    object LoginSignIn : Screen("login_sign_in")
    object Info : Screen("info")
}
val screens = listOf(
    Screen.Steps,
    Screen.Journal,
    Screen.Insights,
    Screen.Nutrition,
    Screen.Sleep,
)