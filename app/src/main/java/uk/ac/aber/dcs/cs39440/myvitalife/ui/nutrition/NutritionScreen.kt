package uk.ac.aber.dcs.cs39440.myvitalife.ui.nutrition

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.material3.*
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme

@Composable
fun NutritionScreen(
    navController: NavHostController
) {
    TopLevelScaffold(
        navController
    ) { innerPadding ->
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
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex
        ) {
            Tab(
                text = { Text("Hydration") },
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 }
            )
            Tab(
                text = { Text("Eating") },
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 }
            )
        }
        when (selectedTabIndex) {
            0 -> {
                Text(
                    modifier = Modifier
                        .padding(top = 30.dp),
                    text = stringResource(id = R.string.nutrition_text1),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
                Image(
                    painter = painterResource(id = R.drawable.hydrationimage),
                    contentDescription = stringResource(R.string.hydration_image),
                    contentScale = ContentScale.Crop
                )
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Text(text = stringResource(id = R.string.add_button))
                }
            }
            1 -> {
                Text(
                    modifier = Modifier
                        .padding(top = 30.dp),
                    text = stringResource(id = R.string.nutrition_text2),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
                Image(
                    modifier = Modifier
                        .size(300.dp),
                    painter = painterResource(id = R.drawable.eatingimage),
                    contentDescription = stringResource(R.string.eating_image),
                    contentScale = ContentScale.Crop
                )
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Text(text = stringResource(id = R.string.add_button))
                }
            }
        }
    }
}

@Preview
@Composable
fun NutritionScreenPreview() {
    val navController = rememberNavController()
    MyVitaLifeTheme(dynamicColor = false) {
        NutritionScreen(navController)
    }
}