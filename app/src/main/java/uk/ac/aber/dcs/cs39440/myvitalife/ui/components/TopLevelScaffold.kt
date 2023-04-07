package uk.ac.aber.dcs.cs39440.myvitalife.ui.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs39440.myvitalife.model.DesiredDate
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.utils.Utils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopLevelScaffold(
    navController: NavHostController,
    appBarTitle: String,
    floatingActionButton: @Composable () -> Unit = { },
    givenDate: String,
    pageContent: @Composable (innerPadding: PaddingValues) -> Unit = {},
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val calendarState = rememberSheetState()
    calendarState.hide()

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { _date ->
            Log.d("SelectedDate", "$_date")
            val chosenDate = _date.toString()
            if (chosenDate.isNotEmpty()) {
                DesiredDate.date = chosenDate
                DesiredDate.notifyDateChangeListeners()
                calendarState.hide()
            }
        }
    )

    MainPageNavigationDrawer(
        navController,
        drawerState = drawerState,
        closeDrawer = {
            coroutineScope.launch {
                // We know it will be open
                drawerState.close()
            }
        }
    ) {
        Scaffold(
            topBar = {
                Column() {
                    HomeScreenTopBar(
                        title = appBarTitle,
                        onClick = {
                            coroutineScope.launch {
                                if (drawerState.isOpen) {
                                    drawerState.close()
                                } else {
                                    drawerState.open()
                                }
                            }
                        },
                        navController = navController,
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = {
                                val date = Utils.getDateDayBefore(DesiredDate.date)
                                DesiredDate.date = date
                                DesiredDate.notifyDateChangeListeners()
                            },
                            modifier = Modifier.padding(start = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Left Arrow"
                            )
                        }
                        TextButton(onClick = { calendarState.show() }) {
                            Text(DesiredDate.date)
                        }
                        IconButton(
                            onClick = {
                                val date = Utils.getDateDayAfter(DesiredDate.date)
                                DesiredDate.date = date
                                DesiredDate.notifyDateChangeListeners()
                            },
                            modifier = Modifier.padding(end = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Right Arrow"
                            )
                        }
                    }
                }
            },
            bottomBar = {
                NavigationBar(navController)
            },
            floatingActionButton = floatingActionButton,
            content = { innerPadding ->
                pageContent(innerPadding)
            }
        )
    }
}
