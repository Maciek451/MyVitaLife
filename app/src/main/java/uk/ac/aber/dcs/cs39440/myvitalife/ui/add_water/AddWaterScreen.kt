package uk.ac.aber.dcs.cs39440.myvitalife.ui.add_water

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWaterScreen(
    navController: NavHostController
) {
    var amount_of_water by remember {
        mutableStateOf("")
    }
    var cup_size by remember {
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
                text = stringResource(id = R.string.amount_of_water),
                fontSize = 25.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
            TextField(
                value = amount_of_water,
                onValueChange = { amount_of_water = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            Spacer(modifier = Modifier.padding(40.dp))
            Text(
                text = stringResource(id = R.string.cup_size),
                fontSize = 25.sp,
                modifier = Modifier.padding(top = 10.dp)
            )
            TextField(
                value = cup_size,
                onValueChange = { cup_size = it },
                modifier = Modifier
                    .fillMaxWidth()
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
                onClick = { navController.navigate(Screen.Nutrition.route) },
                modifier = Modifier
                    .width(100.dp)
                    .height(40.dp)
            ) {
                Text(text = stringResource(R.string.cancel_button))
            }
            Button(
                onClick = { navController.navigate(Screen.Nutrition.route) },
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