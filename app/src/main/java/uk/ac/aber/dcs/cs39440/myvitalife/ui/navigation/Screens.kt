package uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation

sealed class Screens(
    val route: String
)   {
    object Quote : Screens("quote")
    object Insights : Screens("insights")
    object Journal : Screens("journal")
    object Nutrition : Screens("nutrition")
    object Stats : Screens("stats")
    object Sleep : Screens("sleep")
    object SignIn : Screens("sign_in")
    object SignUp : Screens("sign_up")
    object AddSleep : Screens("add_sleep")
    object Info : Screens("info")
}
val screens = listOf(
    Screens.Quote,
    Screens.Journal,
    Screens.Insights,
    Screens.Nutrition,
    Screens.Sleep,
)