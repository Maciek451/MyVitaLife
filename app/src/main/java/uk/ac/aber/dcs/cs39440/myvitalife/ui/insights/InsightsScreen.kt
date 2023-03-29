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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(
    navController: NavHostController
) {
    val calendarState = com.maxkeppeker.sheets.core.models.base.rememberSheetState()
    var date by rememberSaveable { mutableStateOf("") }

    calendarState.hide()
    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { _date ->
            Log.d("SelectedDate", "$_date")
            date = _date.toString();
        }
    )

    TopLevelScaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    calendarState.show()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.CalendarMonth,
                    contentDescription = "Calendar Month"
                )
            }
        },
        navController = navController
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            InsightsScreenContent(
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun InsightsScreenContent(
    modifier: Modifier = Modifier
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(all = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 10.dp)
                .height(150.dp)
        ) {
            Text(
                text = stringResource(id = R.string.mood_counter),
                modifier = Modifier.padding(start = 10.dp, top = 6.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(id = R.string.total),
                modifier = Modifier.padding(start = 10.dp),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier
                            .background(color = Color.Cyan, shape = CircleShape),
                        imageVector = Icons.Filled.SentimentVerySatisfied,
                        contentDescription = "Amazing"
                    )
                    Text(
                        text = "0",
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier
                            .background(color = Color.Green, shape = CircleShape),
                        imageVector = Icons.Filled.SentimentSatisfied,
                        contentDescription = "Good"
                    )
                    Text(
                        text = "0",
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier
                            .background(color = Color.Yellow, shape = CircleShape),
                        imageVector = Icons.Filled.SentimentNeutral,
                        contentDescription = "Neutral"
                    )
                    Text(
                        text = "0",
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier
                            .background(color = Color.Red, shape = CircleShape),
                        imageVector = Icons.Filled.SentimentDissatisfied,
                        contentDescription = "Bad"
                    )
                    Text(
                        text = "0",
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier
                            .background(color = Color.Magenta, shape = CircleShape),
                        imageVector = Icons.Filled.SentimentVeryDissatisfied,
                        contentDescription = "Awful"
                    )
                    Text(
                        text = "0",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 10.dp)
                .height(300.dp)
        ) {
            Text(
                text = stringResource(id = R.string.mood_chart),
                modifier = Modifier.padding(start = 10.dp, top = 6.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
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