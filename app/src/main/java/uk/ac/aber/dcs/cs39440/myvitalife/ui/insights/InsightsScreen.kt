package uk.ac.aber.dcs.cs39440.myvitalife.ui.insights

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
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs39440.myvitalife.model.DesiredDate
import uk.ac.aber.dcs.cs39440.myvitalife.model.Sleep
import uk.ac.aber.dcs.cs39440.myvitalife.model.Water
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme
import java.time.LocalTime

@Composable
fun InsightsScreen(
    navController: NavHostController,
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
    val appBarTitle = stringResource(R.string.insights)
    var userName by rememberSaveable { mutableStateOf("") }
    var totalCalories by rememberSaveable { mutableStateOf(0) }
    val waterData by firebaseViewModel.waterData.observeAsState(Water(0, 0))
    val sleepData by firebaseViewModel.sleepHours.observeAsState(Sleep(0f, "", "", "", ""))
    firebaseViewModel.getUserName {
        userName = it
    }
    firebaseViewModel.getTotalCaloriesForADay() {
        totalCalories = it
    }

    TopLevelScaffold(
        floatingActionButton = {},
        navController = navController,
        appBarTitle = appBarTitle,
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
                Text(
                    text = greeting(userName),
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                MoodCounter(firebaseViewModel)
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 10.dp),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.total_calories),
                            modifier = Modifier.padding(top = 6.dp),
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = totalCalories.toString(),
                            fontSize = 50.sp
                        )
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 10.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.water_intake),
                            modifier = Modifier.padding(top = 6.dp),
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "${waterData.waterDrunk} / ${waterData.hydrationGoal}",
                            fontSize = 50.sp
                        )
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 10.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.hours_of_sleep),
                            modifier = Modifier.padding(top = 6.dp),
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                        val duration = sleepData.duration.ifEmpty { "00:00" }
                        Text(
                            text = duration,
                            fontSize = 50.sp
                        )
                    }
                }
            }
        }
    }
}

fun greeting(userName: String): String {
    val currentTime = LocalTime.now()
    val morning = LocalTime.parse("06:00")
    val afternoon = LocalTime.parse("12:00")
    val evening = LocalTime.parse("18:00")
    val night = LocalTime.parse("00:00")

    val timeOfDay = when {
        currentTime.isAfter(morning) && currentTime.isBefore(afternoon) -> "morning"
        currentTime.isAfter(afternoon) && currentTime.isBefore(evening) -> "afternoon"
        currentTime.isAfter(evening) || currentTime.isBefore(night) -> "evening"
        else -> "night"
    }

    return "Good $timeOfDay $userName!"
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
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 15.dp, bottom = 15.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .background(color = Color.Green, shape = CircleShape)
                        .size(50.dp),
                    imageVector = Icons.Filled.SentimentVerySatisfied,
                    contentDescription = "Amazing"
                )
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = moodCountMap[AMAZING].toString(),
                    fontSize = 20.sp
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .background(color = Color.Cyan, shape = CircleShape)
                        .size(50.dp),
                    imageVector = Icons.Filled.SentimentSatisfied,
                    contentDescription = "Good"
                )
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = moodCountMap[GOOD].toString(),
                    fontSize = 20.sp
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .background(color = Color.Yellow, shape = CircleShape)
                        .size(50.dp),
                    imageVector = Icons.Filled.SentimentNeutral,
                    contentDescription = "Neutral"
                )
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = moodCountMap[NEUTRAL].toString(),
                    fontSize = 20.sp
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .background(color = Color.Magenta, shape = CircleShape)
                        .size(50.dp),
                    imageVector = Icons.Filled.SentimentDissatisfied,
                    contentDescription = "Bad"
                )
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = moodCountMap[BAD].toString(),
                    fontSize = 20.sp
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .background(color = Color.Red, shape = CircleShape)
                        .size(50.dp),
                    imageVector = Icons.Filled.SentimentVeryDissatisfied,
                    contentDescription = "Awful"
                )
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    text = moodCountMap[AWFUL].toString(),
                    fontSize = 20.sp
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