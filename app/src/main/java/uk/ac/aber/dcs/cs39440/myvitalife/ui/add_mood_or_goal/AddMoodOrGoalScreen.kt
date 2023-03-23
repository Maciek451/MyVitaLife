package uk.ac.aber.dcs.cs39440.myvitalife.ui.add_mood_or_goal

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme

@Composable
fun AddMoodOrGoalScreen(
    navController: NavHostController
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 8.dp)
    )
    {
        val (button, content) = createRefs()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
//                .padding(all = 8.dp)
                .constrainAs(content) {
                    start.linkTo(parent.start)
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.add_your_entry),
                modifier = Modifier.padding(top = 8.dp),
                fontSize = 25.sp
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(4.dp)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = R.drawable.addmoodimage),
                        contentDescription = stringResource(R.string.add_mood_image),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        Column(
                            modifier = Modifier
                                .padding(4.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.add_your_mood),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(all = 8.dp)
                            )
                            Text(
                                text = stringResource(id = R.string.add_your_mood_description),
                                modifier = Modifier.padding(all = 8.dp)
                            )
                        }
                        Column(
                            modifier = Modifier
                                .padding(4.dp)
                        ) {
                            Button(onClick = {
                                navController.navigate(Screen.AddMood.route)
                            }
                            ) {
                                Text(text = stringResource(id = R.string.add_button))
                            }
                        }
                    }
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(4.dp)
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = R.drawable.addgoalimage),
                        contentDescription = stringResource(R.string.add_goal_image),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.add_your_goal),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(all = 8.dp)
                            )
                            Text(
                                text = stringResource(id = R.string.add_your_goal_description),
                                modifier = Modifier.padding(all = 8.dp)
                            )
                        }
                        Column(
                            modifier = Modifier
                                .padding(4.dp)
                        ) {
                            Button(onClick = {
                                navController.navigate(Screen.Journal.route)
                            }
                            ) {
                                Text(text = stringResource(id = R.string.add_button))
                            }
                        }
                    }
                }
            }
        }
        Button(
            onClick = { navController.navigate(Screen.Journal.route) },
            modifier = Modifier
                .width(100.dp)
                .height(40.dp)
                .constrainAs(button) {
                    bottom.linkTo(parent.bottom)
                    centerHorizontallyTo(parent)
                }
        ) {
            Text(text = stringResource(R.string.cancel_button))
        }
    }
}

@Preview
@Composable
fun AddMoodOrGoalScreenPreview() {
    val navController = rememberNavController()
    MyVitaLifeTheme(dynamicColor = false) {
        AddMoodOrGoalScreen(navController)
    }
}