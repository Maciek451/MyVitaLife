package uk.ac.aber.dcs.cs39440.myvitalife.ui.journal

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme

@Composable
fun JournalScreen(
    navController: NavHostController
) {
    TopLevelScaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddMoodOrGoal.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add"
                )
            }
        },
        navController = navController
    ) { innerPadding ->
        Surface(
            modifier = Modifier
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
    EmptyJournal()
}

@Composable
private fun EmptyJournal() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            modifier = Modifier
                .padding(top = 30.dp),
            text = stringResource(id = R.string.empty_journal),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            painter = painterResource(id = R.drawable.emptyjournal),
            contentDescription = stringResource(R.string.empty_journal_image),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview
@Composable
fun JournalScreenPreview() {
    val navController = rememberNavController()
    MyVitaLifeTheme(dynamicColor = false) {
        JournalScreen(navController)
    }
}