package uk.ac.aber.dcs.cs39440.myvitalife.ui.add_mood

import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMoodScreen(
    navController: NavHostController,
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
    var optionalDescription by rememberSaveable { mutableStateOf("") }
    var selectedValue by rememberSaveable { mutableStateOf(-1) }

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
                text = stringResource(id = R.string.add_your_mood_description),
                fontSize = 25.sp,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 10.dp)
                    .height(100.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val buttonColors = listOf(
                        Color.Cyan,
                        Color.Green,
                        Color.Yellow,
                        Color.Red,
                        Color.Magenta
                    )

                    buttonColors.forEachIndexed { index, color ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(
                                onClick = {
                                    selectedValue = index
                                },
                                modifier = Modifier
                                    .background(color = color, shape = CircleShape)
                                    .alpha(if (selectedValue != index) 1f else 0.1f)
                            ) {
                                val icon = when (index) {
                                    0 -> Icons.Filled.SentimentVerySatisfied
                                    1 -> Icons.Filled.SentimentSatisfied
                                    2 -> Icons.Filled.SentimentNeutral
                                    3 -> Icons.Filled.SentimentDissatisfied
                                    4 -> Icons.Filled.SentimentVeryDissatisfied
                                    else -> null
                                }
                                icon?.let {
                                    Icon(
                                        imageVector = it,
                                        contentDescription = null
                                    )
                                }
                            }
                            val buttonLabels = listOf(
                                R.string.amazing,
                                R.string.good,
                                R.string.neutral,
                                R.string.bad,
                                R.string.awful
                            )
                            Text(text = stringResource(id = buttonLabels[index]))
                        }
                    }
                }
            }
            Text(
                text = stringResource(id = R.string.add_a_note),
                fontSize = 25.sp,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )
            TextField(
                value = optionalDescription,
                onValueChange = { optionalDescription = it },
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
                    .fillMaxWidth()
            )
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp, bottom = 8.dp),
                painter = painterResource(id = R.drawable.addmoodimage2),
                contentDescription = stringResource(id = R.string.add_mood_image2),
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
                onClick = { navController.navigate(Screen.Journal.route) },
                modifier = Modifier
                    .width(100.dp)
                    .height(40.dp)
            ) {
                Text(text = stringResource(R.string.cancel_button))
            }
            Button(
                onClick = {
                    firebaseViewModel.addMood(selectedValue, optionalDescription)

                    selectedValue = 0
                    optionalDescription = ""

                    navController.navigate(Screen.Journal.route)
                },
                modifier = Modifier
                    .width(100.dp)
                    .height(40.dp),
                enabled = selectedValue == 0 || selectedValue == 1 || selectedValue == 2 || selectedValue == 3 || selectedValue == 4
            ) {
                Text(text = stringResource(R.string.save_button))
            }
        }
    }
}


@Preview
@Composable
fun AddMoodScreenPreview() {
    val navController = rememberNavController()
    MyVitaLifeTheme(dynamicColor = false) {
        AddMoodScreen(navController)
    }
}