package uk.ac.aber.dcs.cs39440.myvitalife.ui.add_steps

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
fun AddStepsScreen(
    navController: NavHostController
) {
    var number_of_steps by remember {
        mutableStateOf("")
    }

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
                text = stringResource(id = R.string.daily_goal_steps),
                fontSize = 25.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
            TextField(
                value = number_of_steps,
                onValueChange = { number_of_steps = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.addstepsimage),
                contentDescription = stringResource(R.string.add_steps_image),
                contentScale = ContentScale.Crop
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
                onClick = { navController.navigate(Screen.Steps.route) },
                modifier = Modifier
                    .width(100.dp)
                    .height(40.dp)
            ) {
                Text(text = stringResource(R.string.cancel_button))
            }
            Button(
                onClick = { navController.navigate(Screen.Steps.route) },
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

@Preview
@Composable
fun AddStepsScreenPreview() {
    val navController = rememberNavController()
    MyVitaLifeTheme(dynamicColor = false) {
        AddStepsScreen(navController)
    }
}