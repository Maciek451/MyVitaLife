package uk.ac.aber.dcs.cs39440.myvitalife.ui.add_sleep

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.components.TopAppBarWithArrow
import uk.ac.aber.dcs.cs39440.myvitalife.ui.components.TopAppBarWithSave
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screens
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme
import uk.ac.aber.dcs.cs39440.myvitalife.utils.Utils
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalTime
import java.util.*
import java.util.concurrent.TimeUnit

enum class SleepTime {
    START, END
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSleepScreen(
    navController: NavHostController,
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
    val title = R.string.sleep_recording
    var sliderPosition by rememberSaveable { mutableStateOf(0f) }
    var answer by rememberSaveable { mutableStateOf("") }
    var startTime by rememberSaveable { mutableStateOf("00:00") }
    var endTime by rememberSaveable { mutableStateOf("00:00") }
    var sleepDuration by rememberSaveable { mutableStateOf("00:00") }
    var sleepTime by rememberSaveable { mutableStateOf(SleepTime.START) }
    val clockState = rememberSheetState()
    clockState.hide()

    ClockDialog(
        state = clockState,
        config = ClockConfig(is24HourFormat = true),
        selection = ClockSelection.HoursMinutes { hours, minutes ->
            val time = String.format("%02d:%02d", hours, minutes)
            when (sleepTime) {
                SleepTime.START ->
                    startTime = time
                SleepTime.END ->
                    endTime = time
            }
        }
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBarWithSave(navController, title, onClick = {
            sleepDuration = calculateTimeDuration(startTime, endTime)

            firebaseViewModel.addSleep(
                sliderPosition.toInt(),
                startTime,
                endTime,
                sleepDuration,
                answer
            )

            sliderPosition = 0f
            startTime = "00:00"
            endTime = "00:00"
            sleepDuration = "00:00"
            answer = ""

            navController.navigate(Screens.Sleep.route)
        }
        )
        Column(
            modifier = Modifier.padding(all = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = stringResource(R.string.rate_your_sleep),
                fontSize = 25.sp,
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
            )

            Text(
                text = sliderPosition.toInt().toString() + "/10",
                fontSize = 25.sp,
                color = MaterialTheme.colorScheme.tertiary
            )
            Slider(
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                valueRange = 0f..10f,
                modifier = Modifier
                    .width(300.dp)
                    .padding(bottom = 10.dp)
            )

            Text(
                text = stringResource(R.string.note),
                fontSize = 25.sp,
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
            )
            val maxChar = 20

            OutlinedTextField(
                value = answer,
                onValueChange = {
                    if (it.length <= maxChar) {
                        answer = it
                    }
                },
                modifier = Modifier
                    .padding(bottom = 10.dp),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 10.dp)
                        .clickable(onClick = {
                            sleepTime = SleepTime.START
                            clockState.show()
                        })
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 10.dp)
                    ) {
                        Icon(
                            modifier = Modifier.padding(end = 8.dp),
                            imageVector = Icons.Filled.Schedule,
                            contentDescription = stringResource(id = R.string.clock)
                        )
                        Text(
                            text = stringResource(id = R.string.start_of_the_sleep),
                            fontSize = 25.sp,
                            modifier = Modifier.padding(end = 8.dp),
                        )
                        Text(
                            text = startTime,
                            fontSize = 25.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 10.dp)
                        .clickable(onClick = {
                            sleepTime = SleepTime.END
                            clockState.show()
                        })
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 10.dp)
                    ) {
                        Icon(
                            modifier = Modifier.padding(end = 8.dp),
                            imageVector = Icons.Filled.Schedule,
                            contentDescription = stringResource(id = R.string.clock)
                        )
                        Text(
                            text = stringResource(id = R.string.end_of_the_sleep),
                            fontSize = 25.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = endTime,
                            fontSize = 25.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

// This function takes two strings, dateString and timeString, and combines them into a Date object
fun parseDateTime(dateString: String, timeString: String): Date {
    val dateTimeString = "$dateString $timeString"
    val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
    // Disable leniency to ensure that the date is parsed strictly according to the format
    dateFormat.isLenient = false
    return dateFormat.parse(dateTimeString)!!
}

// This function calculates the time difference between two dates and returns it as a formatted string
fun getTimeDifference(startDate: Date, endDate: Date): String {
    val diffInMillis = endDate.time - startDate.time
    // Convert the difference to hours and minutes using the TimeUnit class
    val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis - TimeUnit.HOURS.toMillis(hours))
    return String.format("%02d:%02d", hours, minutes)
}

// This function calculates the duration between two times and returns it as a formatted string
private fun calculateTimeDuration(startTime: String, endTime: String): String {
    val startTimeParsed = LocalTime.parse(startTime)
    val endTimeParsed = LocalTime.parse(endTime)

    // Calculate the duration between the start and end times using the Duration class
    val duration = Duration.between(startTimeParsed, endTimeParsed)

    val currentDate = Utils.getCurrentDate()
    val dayAfterDate = Utils.getDateDayAfter(currentDate)

    // Combine the current date and start/end times into Date objects
    val startDate = parseDateTime(currentDate, startTime)
    var endDate = parseDateTime(currentDate, endTime)

    // If the duration is negative, it means the end time is on the next day, so combine the day after date and end time instead
    if (duration.isNegative) {
        endDate = parseDateTime(dayAfterDate, endTime)
    }

    return getTimeDifference(startDate, endDate)
}

@Composable
@Preview
private fun AddSleepScreenPreview() {
    val navController = rememberNavController()
    MyVitaLifeTheme(dynamicColor = false) {
        AddSleepScreen(navController)
    }
}