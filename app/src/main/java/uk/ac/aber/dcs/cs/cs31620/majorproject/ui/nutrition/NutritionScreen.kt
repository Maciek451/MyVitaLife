package uk.ac.aber.dcs.cs.cs31620.majorproject.ui.nutrition

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs.cs31620.majorproject.R
import uk.ac.aber.dcs.cs.cs31620.majorproject.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs.cs31620.majorproject.ui.theme.MajorProjectTheme

@Composable
fun NutritionScreen(
    navController: NavHostController
) {

    TopLevelScaffold(navController = navController) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            NutritionScreenContent(
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun NutritionScreenContent(
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex
        ) {
            Tab(
                text = { Text("Hydration")},
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 }
            )
            Tab(
                text = { Text("Eating")},
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 }
            )
        }
        when (selectedTabIndex) {
            0 -> {
                Image(
                    painter = painterResource(id = R.drawable.hydrationimage),
                    contentDescription = stringResource(R.string.hydration_image),
                    contentScale = ContentScale.Crop
                )
            }
            1 -> {
                Image(
                    painter = painterResource(id = R.drawable.eatingimage),
                    contentDescription = stringResource(R.string.eating_image),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Preview
@Composable
fun NutritionPreview() {
    val navController = rememberNavController()
    MajorProjectTheme(dynamicColor = false) {
        NutritionScreen(navController)
    }
}