package uk.ac.aber.dcs.cs39440.myvitalife.ui.statistics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FormatQuote
import androidx.compose.material.icons.outlined.Hotel
import androidx.compose.material.icons.outlined.NoteAdd
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.components.TopAppBarWithArrow
import uk.ac.aber.dcs.cs39440.myvitalife.ui.insights.MoodCounter

/**
 * Displays statistics screen
 *
 * @param navController NavController manages app navigation
 * @param firebaseViewModel ViewModel providing access to Firebase services.
 */
@Composable
fun StatisticsScreen(
    navController: NavHostController,
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
    var totalSleep by rememberSaveable { mutableStateOf("") }
    var averageSleepRate by rememberSaveable { mutableStateOf("") }

    var totalWater by rememberSaveable { mutableStateOf("") }
    var waterEntryCount by rememberSaveable { mutableStateOf("") }

    var averageCalories by rememberSaveable { mutableStateOf("") }
    var dayActivity by rememberSaveable { mutableStateOf("") }

    var totalQuotes by rememberSaveable { mutableStateOf("") }
    var totalFavouriteQuotes by rememberSaveable { mutableStateOf("") }

    var achievedGoals by rememberSaveable { mutableStateOf("") }
    var totalGoals by rememberSaveable { mutableStateOf("") }

    val title = R.string.data_summary
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    var moodCountMap by rememberSaveable { mutableStateOf(mapOf(0 to 0)) }

    firebaseViewModel.getMoodAllTimeData {
        moodCountMap = it
        moodCountMap = moodCountMap.toMutableMap().apply {
            for (i in 0..4) {
                putIfAbsent(i, 0)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBarWithArrow(navController, title)
        TabRow(
            selectedTabIndex = selectedTabIndex
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 10.dp, top = 10.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.NoteAdd,
                        contentDescription = stringResource(id = R.string.journal_icon),
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.journal)
                    )
                }
            }
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 10.dp, top = 10.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Restaurant,
                        contentDescription = stringResource(id = R.string.nutrition_icon),
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.nutrition)
                    )
                }
            }
            Tab(
                selected = selectedTabIndex == 2,
                onClick = { selectedTabIndex = 2 }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 10.dp, top = 10.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Hotel,
                        contentDescription = stringResource(id = R.string.sleep_icon),
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.sleep)
                    )
                }
            }
            Tab(
                selected = selectedTabIndex == 3,
                onClick = { selectedTabIndex = 3 }
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(bottom = 10.dp, top = 10.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FormatQuote,
                        contentDescription = stringResource(id = R.string.quote_icon),
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.quotes)
                    )
                }
            }
        }
        when (selectedTabIndex) {
            0 -> {
                firebaseViewModel.getAllTimeGoalData { goalData ->
                    achievedGoals = goalData.firstStat
                    totalGoals = goalData.secondStat
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MoodCounter(moodCountMap)
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
                                text = stringResource(id = R.string.custom_goals),
                                modifier = Modifier.padding(top = 6.dp),
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                            )
                            Text(
                                text = "$achievedGoals/$totalGoals",
                                fontSize = 50.sp
                            )
                        }
                    }
                }
            }
            1 -> {
                firebaseViewModel.getAllTimeWaterData { waterData ->
                    totalWater = waterData.firstStat
                    waterEntryCount = waterData.secondStat
                }
                firebaseViewModel.getAllTimeFoodData { foodData ->
                    dayActivity = foodData.firstStat
                    averageCalories = foodData.secondStat
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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
                                text = totalWater,
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
                                text = stringResource(id = R.string.days_with_water_data),
                                modifier = Modifier.padding(top = 6.dp),
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = waterEntryCount,
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
                                text = stringResource(id = R.string.average_calories),
                                modifier = Modifier.padding(top = 6.dp),
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = averageCalories,
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
                                text = stringResource(id = R.string.days_with_food_data),
                                modifier = Modifier.padding(top = 6.dp),
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = dayActivity,
                                fontSize = 50.sp
                            )
                        }
                    }
                }
            }
            2 -> {
                firebaseViewModel.getAllTimeSleepData { sleepData ->
                    totalSleep = sleepData.firstStat
                    averageSleepRate = sleepData.secondStat
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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
                                text = stringResource(id = R.string.total_sleep),
                                modifier = Modifier.padding(top = 6.dp),
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = totalSleep,
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
                                text = stringResource(id = R.string.average_sleep_rate),
                                modifier = Modifier.padding(top = 6.dp),
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "$averageSleepRate/10",
                                fontSize = 50.sp
                            )
                        }
                    }
                }
            }
            3 -> {
                firebaseViewModel.getAllTimeQuoteData { quoteData ->
                    totalQuotes = quoteData.firstStat
                    totalFavouriteQuotes = quoteData.secondStat
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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
                                text = stringResource(id = R.string.total_generated_quotes),
                                modifier = Modifier.padding(top = 6.dp),
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = totalQuotes,
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
                                text = stringResource(id = R.string.total_favourite_quotes),
                                modifier = Modifier.padding(top = 6.dp),
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = totalFavouriteQuotes,
                                fontSize = 50.sp
                            )
                        }
                    }
                }
            }
        }
    }
}