package uk.ac.aber.dcs.cs39440.myvitalife.ui.summary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs39440.myvitalife.model.Water
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.components.HomeScreenTopBar
import uk.ac.aber.dcs.cs39440.myvitalife.ui.components.SummaryScreenTopBar

@Composable
fun SummaryScreen(
    navController: NavHostController,
    firebaseViewModel: FirebaseViewModel = viewModel()

) {
    val listOfGoals by firebaseViewModel.goalData.observeAsState(emptyList())
    val listOfMoods by firebaseViewModel.moodsList.observeAsState(emptyList())
    val listOfFood by firebaseViewModel.foodList.observeAsState(emptyList())
    val waterData by firebaseViewModel.waterData.observeAsState(Water(0, 0))

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SummaryScreenTopBar(navController)

    }

}
