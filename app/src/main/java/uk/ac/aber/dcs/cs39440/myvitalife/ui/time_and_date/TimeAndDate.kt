package uk.ac.aber.dcs.cs39440.myvitalife.ui.time_and_date

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavHostController
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeAndDateScreen(
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

    Column(

    ) {

        Text(date)

        Button(
            onClick = { calendarState.show() }
        ) {
            Text(text = "show")
        }

    }

}