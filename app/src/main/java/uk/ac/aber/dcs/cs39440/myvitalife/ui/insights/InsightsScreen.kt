package uk.ac.aber.dcs.cs39440.myvitalife.ui.insights

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs39440.myvitalife.model.DesiredDate
import uk.ac.aber.dcs.cs39440.myvitalife.model.Mood
import uk.ac.aber.dcs.cs39440.myvitalife.model.Sleep
import uk.ac.aber.dcs.cs39440.myvitalife.model.Water
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme

@Composable
fun InsightsScreen(
    navController: NavHostController,
    firebaseViewModel: FirebaseViewModel = viewModel()
) {

    var userName by rememberSaveable { mutableStateOf("") }
    var totalCalories by rememberSaveable { mutableStateOf(0) }
    val waterData by firebaseViewModel.waterData.observeAsState(Water(0, 0))
    val sleepData by firebaseViewModel.sleepHours.observeAsState(Sleep(0f, "", "", "", ""))

    TopLevelScaffold(
        floatingActionButton = {},
        navController = navController,
        givenDate = DesiredDate.date
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(all = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                firebaseViewModel.getUserName {
                    userName = it
                }
                Text(
                    modifier = Modifier
                        .padding(top = 30.dp),
                    text = userName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
                MoodCounter(firebaseViewModel)
                firebaseViewModel.getTotalCaloriesForADay() {
                    totalCalories = it
                }
                Text(
                    text = stringResource(id = R.string.total_calories) + totalCalories.toString(),
                    fontSize = 20.sp
                )
                Text(
                    text = stringResource(id = R.string.water_intake) + waterData.waterDrunk + "/" + waterData.hydrationGoal
                )
                Text(
                    text = stringResource(id = R.string.hours_of_sleep) + sleepData.duration
                )
            }
        }
    }
}

@Composable
fun Greeting() {

}

@Composable
fun MoodCounter(
    firebaseViewModel: FirebaseViewModel
) {
    val AMAZING = 0
    val GOOD = 1
    val NEUTRAL = 2
    val BAD = 3
    val AWFUL = 4

    var moodCountMap by rememberSaveable { mutableStateOf(mapOf(0 to 0)) }

    firebaseViewModel.fetchMoodSummaryForTheDay() {
        moodCountMap = it
        moodCountMap = moodCountMap.toMutableMap().apply {
            for (i in 0..4) {
                putIfAbsent(i, 0)
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(id = R.string.mood_counter),
                modifier = Modifier.padding(start = 10.dp, top = 6.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 15.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .background(color = Color.Cyan, shape = CircleShape)
                        .size(30.dp),
                    imageVector = Icons.Filled.SentimentVerySatisfied,
                    contentDescription = "Amazing"
                )
                Text(
                    text = moodCountMap[AMAZING].toString(),
                    fontWeight = FontWeight.Bold
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .background(color = Color.Green, shape = CircleShape)
                        .size(30.dp),
                    imageVector = Icons.Filled.SentimentSatisfied,
                    contentDescription = "Good"
                )
                Text(
                    text = moodCountMap[GOOD].toString(),
                    fontWeight = FontWeight.Bold
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .background(color = Color.Yellow, shape = CircleShape)
                        .size(30.dp),
                    imageVector = Icons.Filled.SentimentNeutral,
                    contentDescription = "Neutral"
                )
                Text(
                    text = moodCountMap[NEUTRAL].toString(),
                    fontWeight = FontWeight.Bold
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .background(color = Color.Red, shape = CircleShape)
                        .size(30.dp),
                    imageVector = Icons.Filled.SentimentDissatisfied,
                    contentDescription = "Bad"
                )
                Text(
                    text = moodCountMap[BAD].toString(),
                    fontWeight = FontWeight.Bold
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .background(color = Color.Magenta, shape = CircleShape)
                        .size(30.dp),
                    imageVector = Icons.Filled.SentimentVeryDissatisfied,
                    contentDescription = "Awful"
                )
                Text(
                    text = moodCountMap[AWFUL].toString(),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview
@Composable
fun InsightsScreenPreview() {
    val navController = rememberNavController()
    MyVitaLifeTheme(dynamicColor = false) {
        InsightsScreen(navController)
    }
}