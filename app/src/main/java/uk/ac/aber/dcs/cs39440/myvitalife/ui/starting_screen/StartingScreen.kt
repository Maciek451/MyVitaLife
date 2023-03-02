package uk.ac.aber.dcs.cs39440.myvitalife.ui.starting_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs39440.myvitalife.datastore.DataStore
import uk.ac.aber.dcs.cs39440.myvitalife.ui.journal.JournalScreen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.provide_name.ProvideNameScreen

@Composable
fun StartingScreen(
    navController: NavHostController
) {
    val context = LocalContext.current
    val dataStore = DataStore(context)
    val firstTimeVal = dataStore.getString("IS_FIRST_TIME").collectAsState(initial = "")
    val firstTime = firstTimeVal.value?.isEmpty() == true || firstTimeVal.value == null
    if (firstTime) {
        ProvideNameScreen(navController = navController)
    } else {
        JournalScreen(navController = navController)
    }
}