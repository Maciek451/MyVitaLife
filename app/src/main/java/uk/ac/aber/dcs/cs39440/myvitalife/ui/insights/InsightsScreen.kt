package uk.ac.aber.dcs.cs39440.myvitalife.ui.insights

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs39440.myvitalife.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme

@Composable
fun InsightsScreen(
    navController: NavHostController
) {
    TopLevelScaffold(navController = navController) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            InsightsScreenContent(
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun InsightsScreenContent(
    modifier: Modifier = Modifier
) {

}

@Preview
@Composable
fun InsightsScreenPreview() {
    val navController = rememberNavController()
    MyVitaLifeTheme(dynamicColor = false) {
        InsightsScreen(navController)
    }
}