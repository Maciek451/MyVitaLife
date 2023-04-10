package uk.ac.aber.dcs.cs39440.myvitalife

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.aber.dcs.cs39440.myvitalife.datastorage.IS_DARK_THEME_ON_KEY
import uk.ac.aber.dcs.cs39440.myvitalife.model.DataViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.model.QuoteOfTheDay
import uk.ac.aber.dcs.cs39440.myvitalife.model.ThemeSettings
import uk.ac.aber.dcs.cs39440.myvitalife.ui.Authentication
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.account.AccountScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.add_sleep.AddSleepScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.authentication.SignInScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.authentication.SignUpScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.quotes.QuotesScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.insights.InsightsScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.journal.JournalScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screens
import uk.ac.aber.dcs.cs39440.myvitalife.ui.nutrition.NutritionScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.sleep.SleepScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.quotes.service
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme
import uk.ac.aber.dcs.cs39440.myvitalife.ui.why_to_track.InfoScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SetTheme()
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
private fun BuildNavigationGraph() {
    val navController = rememberNavController()
    FirebaseApp.initializeApp(LocalContext.current)
    Firebase.database.setPersistenceEnabled(true)
    val user = Firebase.auth.currentUser
    var startScreenRoute = Screens.SignIn.route

    if (user != null) {
        Authentication.userId = user.uid
        Authentication.userEmail = user.email.toString()
        IsQuoteToday()
        startScreenRoute = Screens.Insights.route
    }

    NavHost(
        navController = navController,
        startDestination = startScreenRoute
    ) {
        composable(Screens.Quote.route) { QuotesScreen(navController)}
        composable(Screens.Sleep.route) { SleepScreen(navController)}
        composable(Screens.Insights.route) { InsightsScreen(navController)}
        composable(Screens.Nutrition.route) { NutritionScreen(navController)}
        composable(Screens.Journal.route) { JournalScreen(navController)}
        composable(Screens.SignIn.route) { SignInScreen(navController)}
        composable(Screens.SignUp.route) { SignUpScreen(navController)}
        composable(Screens.AddSleep.route) { AddSleepScreen(navController)}
        composable(Screens.Account.route) { AccountScreen(navController) }
        composable(Screens.Info.route) { InfoScreen(navController) }
    }
}

@Composable
fun IsQuoteToday(
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
    if (QuoteOfTheDay.quote.text.isEmpty()) {
        LaunchedEffect(Unit) {
            try {
                QuoteOfTheDay.quote = service.getQuoteOfTheDay()
            } catch (e: Exception) {
                Log.e("QUOTE_ERROR", e.toString())
            }
        }
        firebaseViewModel.addQuote(QuoteOfTheDay.quote.text, QuoteOfTheDay.quote.author)
    }
    else {
//        firebaseViewModel.addQuote(QuoteOfTheDay.quote.text, QuoteOfTheDay.quote.author)
    }
}

@Composable
fun SetTheme(
    dataViewModel: DataViewModel = hiltViewModel()
) {
    val isDarkThemeOn = dataViewModel.getBoolean(IS_DARK_THEME_ON_KEY)
    ThemeSettings.isDarkTheme = isDarkThemeOn ?: isSystemInDarkTheme()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyVitaLifeTheme(dynamicColor = false) {
        BuildNavigationGraph()
    }
}