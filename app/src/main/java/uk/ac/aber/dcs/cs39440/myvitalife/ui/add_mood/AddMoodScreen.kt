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
import androidx.compose.ui.res.colorResource
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
fun AddMoodScreen(
    navController: NavHostController
) {
    var answer by rememberSaveable { mutableStateOf("") }
    var isSelected by rememberSaveable { mutableStateOf(false) }

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
//            modifier = Modifier.padding(top = 10.dp)
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
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = { isSelected = !isSelected },
                            modifier = Modifier
                                .background(color = Color.Cyan, shape = CircleShape),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.SentimentVerySatisfied,
                                contentDescription = "Amazing",
                                modifier = Modifier.alpha(if (isSelected) 0.5f else 1f)
                            )
                        }
                        Text(text = stringResource(id = R.string.amazing))
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    )
                    {
                        IconButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .background(color = Color.Green, shape = CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.SentimentSatisfied,
                                contentDescription = "Good"
                            )

                        }
                        Text(text = stringResource(id = R.string.good))
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    )
                    {
                        IconButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .background(color = Color.Yellow, shape = CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.SentimentNeutral,
                                contentDescription = "Neutral"
                            )
                        }
                        Text(text = stringResource(id = R.string.neutral))
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .background(color = Color.Red, shape = CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.SentimentDissatisfied,
                                contentDescription = "Bad"
                            )
                        }
                        Text(text = stringResource(id = R.string.bad))
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        IconButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .background(color = Color.Magenta, shape = CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.SentimentVeryDissatisfied,
                                contentDescription = "Awful"
                            )

                        }
                        Text(text = stringResource(id = R.string.awful))
                    }
                }
            }
            Text(
                text = stringResource(id = R.string.add_a_note),
                fontSize = 25.sp
            )
            TextField(
                value = answer,
                onValueChange = { answer = it },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Image(
                modifier = Modifier.fillMaxSize(),
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
                onClick = { navController.navigate(Screen.AddMoodOrGoal.route) },
                modifier = Modifier
                    .width(100.dp)
                    .height(40.dp)
            ) {
                Text(text = stringResource(R.string.cancel_button))
            }
            Button(
                onClick = { navController.navigate(Screen.Journal.route) },
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
fun AddMoodScreenPreview() {
    val navController = rememberNavController()
    MyVitaLifeTheme(dynamicColor = false) {
        AddMoodScreen(navController)
    }
}