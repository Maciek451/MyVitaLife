package uk.ac.aber.dcs.cs39440.myvitalife.ui.sleep

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import uk.ac.aber.dcs.cs39440.myvitalife.model.DesiredDate
import uk.ac.aber.dcs.cs39440.myvitalife.model.Sleep
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme

@Composable
fun SleepScreen(
    navController: NavHostController,
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
    var currentFabImage by remember { mutableStateOf(Icons.Filled.Add) }
    val sleepData by firebaseViewModel.sleepHours.observeAsState(Sleep(0f, "", "", "", ""))

    TopLevelScaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddSleep.route)
                }
            ) {
                Icon(
                    imageVector = currentFabImage,
                    contentDescription = "Add"
                )
            }
        },
        navController = navController,
        givenDate = DesiredDate.date
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (sleepData != Sleep(0f, "", "", "", "")) {
                currentFabImage = Icons.Filled.Settings
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = stringResource(id = R.string.duration_of_the_sleep),
                        fontSize = 20.sp
                    )
                    Text(
                        text = sleepData.duration,
                        fontSize = 30.sp
                    )

                    Spacer(modifier = Modifier.padding(20.dp))

                    Text(
                        text = stringResource(id = R.string.rating),
                        fontSize = 20.sp
                    )
                    Text(
                        text = sleepData.score.toString() + "/10",
                        fontSize = 30.sp
                    )

                    Spacer(modifier = Modifier.padding(20.dp))

                    Text(
                        text = stringResource(id = R.string.start_of_the_sleep),
                        fontSize = 20.sp
                    )
                    Text(
                        text = sleepData.start,
                        fontSize = 30.sp
                    )

                    Spacer(modifier = Modifier.padding(20.dp))

                    Text(
                        text = stringResource(id = R.string.end_of_the_sleep),
                        fontSize = 20.sp
                    )
                    Text(
                        text = sleepData.end,
                        fontSize = 30.sp
                    )

                    Spacer(modifier = Modifier.padding(20.dp))

                    Text(
                        text = stringResource(R.string.note),
                        fontSize = 20.sp
                    )

                    Text(
                        text = sleepData.note,
                        fontSize = 30.sp
                    )
                }
            } else {
                EmptySleepScreen()
            }
        }
    }
}

@Composable
private fun EmptySleepScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(top = 30.dp),
            text = stringResource(id = R.string.sleep_text),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(id = R.drawable.sleepimage),
            contentDescription = stringResource(R.string.sleep_image),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
@Preview
private fun SleepScreenPreview() {
    val navController = rememberNavController()
    MyVitaLifeTheme(dynamicColor = false) {
        SleepScreen(navController)
    }
}