package uk.ac.aber.dcs.cs39440.myvitalife.ui.add_sleep

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SingleBed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme
import uk.ac.aber.dcs.cs39440.myvitalife.utils.Utils
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalTime
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.absoluteValue
import kotlin.math.min

enum class SleepTime {
    START, END
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSleepScreen(
    navController: NavHostController,
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
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
        })

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 8.dp)
    )
    {
        val (buttons, content) = createRefs()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(all = 8.dp)
                .constrainAs(content) {
                    start.linkTo(parent.start)
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(R.string.rate_your_sleep),
                fontSize = 25.sp,
                modifier = Modifier.padding(top = 10.dp)
            )

            Slider(
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                valueRange = 0f..10f,
                modifier = Modifier.width(200.dp)
            )

            Text(text = sliderPosition.toInt().toString())

            Text(
                text = stringResource(R.string.how_do_you_feel),
                fontSize = 25.sp,
                modifier = Modifier.padding(top = 10.dp)
            )

            OutlinedTextField(
                value = answer,
                onValueChange = { answer = it },
                modifier = Modifier
                    .padding(bottom = 8.dp)
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Start:",
                    fontSize = 25.sp,
                    modifier = Modifier
                        .padding(top = 10.dp)
                )
                TextButton(onClick = {
                    sleepTime = SleepTime.START
                    clockState.show()
                }) {
                    Text(
                        text = startTime,
                        fontSize = 25.sp,
                        modifier = Modifier
                            .padding(top = 10.dp)
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.SingleBed,
                contentDescription = "Bed",
                modifier = Modifier
                    .size(200.dp)
            )
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "End:",
                    fontSize = 25.sp,
                    modifier = Modifier
                        .padding(top = 10.dp)
                )
                TextButton(onClick = {
                    sleepTime = SleepTime.END
                    clockState.show()
                }) {
                    Text(
                        text = endTime,
                        fontSize = 25.sp,
                        modifier = Modifier
                            .padding(top = 10.dp)
                    )
                }
            }

        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(buttons) {
                    bottom.linkTo(parent.bottom)
                }
        ) {
            Button(
                onClick = { navController.navigate(Screen.Sleep.route) },
                modifier = Modifier
                    .width(100.dp)
                    .height(40.dp)
            ) {
                Text(text = stringResource(R.string.cancel_button))
            }
            Button(
                onClick = {
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

                    navController.navigate(Screen.Sleep.route)
                },
                modifier = Modifier
                    .width(100.dp)
                    .height(40.dp),
                enabled = startTime != "00:00" && endTime != "00:00"
            ) {
                Text(text = stringResource(R.string.save_button))
            }
        }
    }
}

// This function takes two strings, dateString and timeString, and combines them into a Date object
fun parseDateTime(dateString: String, timeString: String): Date {
    val dateTimeString = "$dateString $timeString"
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
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