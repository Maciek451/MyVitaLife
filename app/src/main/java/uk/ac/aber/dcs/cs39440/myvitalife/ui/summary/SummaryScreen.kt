package uk.ac.aber.dcs.cs39440.myvitalife.ui.summary

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel

//@Composable
//fun SummaryScreen(
//    navController: NavHostController,
//    firebaseViewModel: FirebaseViewModel = viewModel(),
//) {
//    var listOfFood by rememberSaveable(stateSaver = foodListSaver) { mutableStateOf(emptyList<Food>()) }
//
//    var waterDrunk by rememberSaveable { mutableStateOf(0) }
//    firebaseViewModel.fetchWaterData(Insight.date) { water ->
//        waterDrunk = water.waterDrunk
//    }
//    val title = R.string.summary
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//    ) {
//        TopAppBarWithArrow(navController, title)
//        Text(text = Insight.date)
//        Text(text = waterDrunk.toString())
//        firebaseViewModel.fetchFoodData(Insight.date) { foods ->
//            if (foods.isNotEmpty()) {
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(top = 12.dp),
//                    contentPadding = PaddingValues(bottom = 16.dp)
//                )
//                {
//                    items(foods) { entry ->
//                        Row(
//                            horizontalArrangement = Arrangement.SpaceBetween,
//                            verticalAlignment = Alignment.CenterVertically,
//                            modifier = Modifier.fillMaxWidth()
//                        ) {
//                            Column(
//                                horizontalAlignment = Alignment.Start
//                            ) {
//                                FoodCard(
//                                    name = entry.name,
//                                    kcal = entry.kcal,
//                                    openConfirmationDialog = {
//                                    }
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
