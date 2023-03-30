package uk.ac.aber.dcs.cs39440.myvitalife.ui.summary

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs39440.myvitalife.model.Food
import uk.ac.aber.dcs.cs39440.myvitalife.model.Water
import uk.ac.aber.dcs.cs39440.myvitalife.model.foodListSaver
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.components.SummaryScreenTopBar
import uk.ac.aber.dcs.cs39440.myvitalife.ui.insights.model.Insight
import uk.ac.aber.dcs.cs39440.myvitalife.ui.nutrition.FoodCard

@Composable
fun SummaryScreen(
    navController: NavHostController,
    firebaseViewModel: FirebaseViewModel = viewModel(),
) {
    val listOfGoals by firebaseViewModel.goalData.observeAsState(emptyList())
    val listOfMoods by firebaseViewModel.moodsList.observeAsState(emptyList())
    var listOfFood by rememberSaveable(stateSaver = foodListSaver) { mutableStateOf(emptyList<Food>()) }
    firebaseViewModel.fetchFoodData(Insight.date) { foods ->
        listOfFood = foods
    }
    val waterData by firebaseViewModel.waterData.observeAsState(Water(0, 0))
    var waterDrunk by rememberSaveable { mutableStateOf(0) }
    firebaseViewModel.fetchWaterData(Insight.date) { water ->
        waterDrunk = water.waterDrunk
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SummaryScreenTopBar(navController)
        Text(text = Insight.date)
        Text(text = waterDrunk.toString())
        if (listOfFood.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            )
            {
                items(listOfFood) { entry ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            FoodCard(
                                name = entry.name,
                                kcal = entry.kcal,
                                openConfirmationDialog = {
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
