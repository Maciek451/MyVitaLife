package uk.ac.aber.dcs.cs.cs31620.majorproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs.cs31620.majorproject.ui.provide_name.ProvideNameScreen
import uk.ac.aber.dcs.cs.cs31620.majorproject.ui.daily_steps.DailyStepsScreen
import uk.ac.aber.dcs.cs.cs31620.majorproject.ui.insights.InsightsScreen
import uk.ac.aber.dcs.cs.cs31620.majorproject.ui.journal.JournalScreen
import uk.ac.aber.dcs.cs.cs31620.majorproject.ui.navigation.Screen
import uk.ac.aber.dcs.cs.cs31620.majorproject.ui.nutrition.NutritionScreen
import uk.ac.aber.dcs.cs.cs31620.majorproject.ui.sleep.SleepScreen
import uk.ac.aber.dcs.cs.cs31620.majorproject.ui.start_screen.StartScreen
import uk.ac.aber.dcs.cs.cs31620.majorproject.ui.theme.MajorProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MajorProjectTheme(dynamicColor = false) {
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
private fun BuildNavigationGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.ProvideName.route
    ) {
        composable(Screen.DailySteps.route) { DailyStepsScreen(navController)}
        composable(Screen.StartScreen.route) { StartScreen(navController)}
        composable(Screen.Sleep.route) { SleepScreen(navController)}
        composable(Screen.Insights.route) { InsightsScreen(navController)}
        composable(Screen.Nutrition.route) { NutritionScreen(navController)}
        composable(Screen.Journal.route) { JournalScreen(navController)}
        composable(Screen.ProvideName.route) { ProvideNameScreen(navController)}
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MajorProjectTheme(dynamicColor = false) {
        BuildNavigationGraph()
    }
}