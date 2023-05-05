package uk.ac.aber.dcs.cs39440.myvitalife.ui.sleep

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.model.Sleep
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screens
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme

/**
 * Displays Sleep screen
 *
 * @param navController NavController manages app navigation
 * @param firebaseViewModel ViewModel providing access to Firebase services.
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SleepScreen(
    navController: NavHostController,
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
    val appBarTitle = stringResource(R.string.sleep)
    val currentFabImage by remember { mutableStateOf(Icons.Filled.Bedtime) }
    val sleepData by firebaseViewModel.sleepHours.observeAsState(Sleep(0f, "", "", "", ""))

    TopLevelScaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screens.AddSleep.route)
                }
            ) {
                Icon(
                    imageVector = currentFabImage,
                    contentDescription = "Add"
                )
            }
        },
        navController = navController,
        appBarTitle = appBarTitle,

    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (sleepData != Sleep(0f, "", "", "", "")) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Divider(thickness = 1.dp)
                    Spacer(modifier = Modifier.padding(10.dp))
                    Text(
                        text = stringResource(id = R.string.duration_of_the_sleep),
                        fontSize = 20.sp
                    )
                    Text(
                        text = sleepData.duration,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 30.sp
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    Divider(thickness = 1.dp)
                    Spacer(modifier = Modifier.padding(10.dp))

                    Text(
                        text = stringResource(id = R.string.rating),
                        fontSize = 20.sp
                    )
                    Text(
                        text = "${sleepData.score.toInt()}/10",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 30.sp
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    Divider(thickness = 1.dp)
                    Spacer(modifier = Modifier.padding(10.dp))
                    Text(
                        text = stringResource(id = R.string.start_of_the_sleep),
                        fontSize = 20.sp
                    )
                    Text(
                        text = sleepData.start,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 30.sp
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    Divider(thickness = 1.dp)
                    Spacer(modifier = Modifier.padding(10.dp))
                    Text(
                        text = stringResource(id = R.string.end_of_the_sleep),
                        fontSize = 20.sp
                    )
                    Text(
                        text = sleepData.end,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 30.sp
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    Divider(thickness = 1.dp)
                    Spacer(modifier = Modifier.padding(10.dp))
                    Text(
                        text = stringResource(R.string.note_text),
                        fontSize = 20.sp
                    )
                    var noteText = ""
                    noteText = sleepData.note.ifEmpty {
                        stringResource(id = R.string.empty_with_brackets)
                    }
                    Text(
                        text = noteText,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 30.sp
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    Divider(thickness = 1.dp)
                }
            } else {
                EmptySleepScreen()
            }
        }
    }
}

/**
 * Appears when there is no data in database
 *
 */
@Composable
private fun EmptySleepScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier
                .size(100.dp)
                .alpha(0.3f),
            imageVector = Icons.Default.BedtimeOff,
            contentDescription = stringResource(id = R.string.empty_sleep_screen_icon)
        )
        Text(
            modifier = Modifier
                .alpha(0.3f),
            text = stringResource(id = R.string.sleep_text),
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
@Preview
private fun SleepScreenPreview() {
    val navController = rememberNavController()
    MyVitaLifeTheme(dynamicColor = false) {
        SleepScreen(navController)
    }
}