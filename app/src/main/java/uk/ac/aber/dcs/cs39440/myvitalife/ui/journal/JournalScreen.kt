package uk.ac.aber.dcs.cs39440.myvitalife.ui.journal

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
fun JournalScreen(
    navController: NavHostController
) {
    TopLevelScaffold(navController = navController) { innerPadding ->
        Surface(
            modifier = androidx.compose.ui.Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            JournalScreenContent(
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun JournalScreenContent(
    modifier: Modifier = Modifier
) {

}

@Preview
@Composable
fun JournalScreenPreview() {
    val navController = rememberNavController()
    MyVitaLifeTheme(dynamicColor = false) {
        JournalScreen(navController)
    }
}