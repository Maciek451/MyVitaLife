package uk.ac.aber.dcs.cs39440.myvitalife.ui.add_sleep

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSleepScreen(
    navController: NavHostController
) {
    var sliderPosition by rememberSaveable { mutableStateOf(0f) }
    var answer by rememberSaveable { mutableStateOf("") }

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

            Text(
                text = stringResource(R.string.how_do_you_feel),
                fontSize = 25.sp,
                modifier = Modifier.padding(top = 10.dp)
            )

            TextField(
                value = answer,
                onValueChange = { answer = it },
                modifier = Modifier
                    .padding(bottom = 8.dp)
            )
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
                onClick = { navController.navigate(Screen.TimeAndDate.route) },
                modifier = Modifier
                    .width(100.dp)
                    .height(40.dp)
//                enabled = answer.isNotEmpty()
            ) {
                Text(text = stringResource(R.string.save_button))
            }
        }
    }
}

@Composable
@Preview
private fun AddSleepScreenPreview() {
    val navController = rememberNavController()
    MyVitaLifeTheme(dynamicColor = false) {
        AddSleepScreen(navController)
    }
}