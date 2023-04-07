package uk.ac.aber.dcs.cs39440.myvitalife

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import uk.ac.aber.dcs.cs39440.myvitalife.ui.Authentication
import uk.ac.aber.dcs.cs39440.myvitalife.ui.account.AccountScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.add_sleep.AddSleepScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.login_sign_up.LoginSignUpScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.steps.StepsScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.insights.InsightsScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.journal.JournalScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.nutrition.NutritionScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.sleep.SleepScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyVitaLifeTheme(dynamicColor = false) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BuildNavigationGraph()
                }
            }
        }
    }
}

@Composable
private fun BuildNavigationGraph(
) {
    val navController = rememberNavController()
    FirebaseApp.initializeApp(LocalContext.current)
    Firebase.database.setPersistenceEnabled(true)
    val user = Firebase.auth.currentUser

    var startScreenRoute = Screen.LoginSignIn.route
    if (user != null) {
        Authentication.userId = user.uid
        Authentication.userEmail = user.email.toString()
        startScreenRoute = Screen.Insights.route
    }

    NavHost(
        navController = navController,
        startDestination = startScreenRoute
    ) {
        composable(Screen.Steps.route) { StepsScreen(navController)}
        composable(Screen.Sleep.route) { SleepScreen(navController)}
        composable(Screen.Insights.route) { InsightsScreen(navController)}
        composable(Screen.Nutrition.route) { NutritionScreen(navController)}
        composable(Screen.Journal.route) { JournalScreen(navController)}
        composable(Screen.LoginSignUp.route) { LoginSignUpScreen(navController)}
        composable(Screen.AddSleep.route) { AddSleepScreen(navController)}
        composable(Screen.Account.route) { AccountScreen(navController) }
        composable(Screen.LoginSignIn.route) { LoginSignUpScreen(navController) }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyVitaLifeTheme(dynamicColor = false) {
        BuildNavigationGraph()
    }
}