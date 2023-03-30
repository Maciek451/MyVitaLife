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
import uk.ac.aber.dcs.cs39440.myvitalife.ui.add_mood.AddMoodScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.add_mood_or_goal.AddMoodOrGoalScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.add_sleep.AddSleepScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.add_steps.AddStepsScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.login.LoginScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.steps.StepsScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.insights.InsightsScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.journal.JournalScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.nutrition.NutritionScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.sleep.SleepScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.starting_screen.StartingScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.summary.SummaryScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme
import uk.ac.aber.dcs.cs39440.myvitalife.ui.time_and_date.TimeAndDateScreen

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
                    FirebaseApp.initializeApp(LocalContext.current)
                    BuildNavigationGraph()
                }
            }
        }
    }
}

@Composable
private fun BuildNavigationGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.Journal.route
    ) {
        composable(Screen.Steps.route) { StepsScreen(navController)}
        composable(Screen.StartScreen.route) { StartingScreen(navController)}
        composable(Screen.Sleep.route) { SleepScreen(navController)}
        composable(Screen.Insights.route) { InsightsScreen(navController)}
        composable(Screen.Nutrition.route) { NutritionScreen(navController)}
        composable(Screen.Journal.route) { JournalScreen(navController)}
        composable(Screen.ProvideName.route) { LoginScreen(navController)}
        composable(Screen.AddSleep.route) { AddSleepScreen(navController)}
        composable(Screen.TimeAndDate.route) { TimeAndDateScreen(navController) }
        composable(Screen.AddMoodOrGoal.route) { AddMoodOrGoalScreen(navController) }
        composable(Screen.AddMood.route) { AddMoodScreen(navController) }
        composable(Screen.AddSteps.route) { AddStepsScreen(navController) }
        composable(Screen.Summary.route) { SummaryScreen(navController) }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyVitaLifeTheme(dynamicColor = false) {
        BuildNavigationGraph()
    }
}