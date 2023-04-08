package uk.ac.aber.dcs.cs39440.myvitalife.ui.steps

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoNotStep
import androidx.compose.material.icons.filled.NordicWalking
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs39440.myvitalife.ui.components.TopLevelScaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.model.DesiredDate
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme

@Composable
fun StepsScreen(
    navController: NavHostController
) {
    val appBarTitle = stringResource(R.string.steps)

    var isDialogOpen by rememberSaveable { mutableStateOf(false) }

    TopLevelScaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isDialogOpen = true
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.NordicWalking,
                    contentDescription = "Add"
                )
            }
        },
        navController = navController,
        appBarTitle = appBarTitle,
        givenDate = DesiredDate.date
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
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
                    imageVector = Icons.Default.DoNotStep,
                    contentDescription = "EmptySleepScreen"
                )
                Text(
                    modifier = Modifier
                        .alpha(0.3f),
                    text = stringResource(id = R.string.daily_steps_text),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
    AddStepsDialog(
        dialogIsOpen = isDialogOpen,
        dialogOpen = { isOpen ->
            isDialogOpen = isOpen
        }
    )
}

@Preview
@Composable
fun DailyStepsPreview() {
    val navController = rememberNavController()
    MyVitaLifeTheme(dynamicColor = false) {
        StepsScreen(navController)
    }
}
