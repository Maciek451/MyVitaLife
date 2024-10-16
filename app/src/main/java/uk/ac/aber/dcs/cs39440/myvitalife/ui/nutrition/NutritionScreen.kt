package uk.ac.aber.dcs.cs39440.myvitalife.ui.nutrition

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.model.Water
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme
import uk.ac.aber.dcs.cs39440.myvitalife.utils.Utils

/**
 * Displays Nutrition screen
 *
 * @param navController NavController manages app navigation
 * @param firebaseViewModel ViewModel providing access to Firebase services.
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun NutritionScreen(
    navController: NavHostController,
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
    val appBarTitle = stringResource(R.string.nutrition)

    val tab0Button by remember { mutableStateOf(Icons.Filled.Coffee) }
    val tab1Button by remember { mutableStateOf(Icons.Filled.Fastfood) }

    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    val listOfFood by firebaseViewModel.foodList.observeAsState(emptyList())
    val waterData by firebaseViewModel.waterData.observeAsState(Water(0, 0))

    var foodToDelete by remember { mutableStateOf("") }

    var isWaterDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isFoodDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isConfirmationDialogOpen by rememberSaveable { mutableStateOf(false) }

    var totalCalories by rememberSaveable { mutableStateOf(0) }

    TopLevelScaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (selectedTabIndex == 0) {
                        isWaterDialogOpen = true
                    } else {
                        isFoodDialogOpen = true
                    }
                },
            ) {
                if (selectedTabIndex == 0) {
                    Icon(
                        imageVector = tab0Button,
                        contentDescription = stringResource(id = R.string.add)
                    )
                } else {
                    Icon(
                        imageVector = tab1Button,
                        contentDescription = stringResource(id = R.string.add)
                    )
                }
            }
        },
        navController = navController,
        appBarTitle = appBarTitle,

        ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
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
                        text = { Text(stringResource(id = R.string.hydration)) },
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 }
                    )
                    Tab(
                        text = { Text(stringResource(id = R.string.eating)) },
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 }
                    )
                }
                when (selectedTabIndex) {
                    0 -> {
                        if (waterData.waterDrunk >= waterData.hydrationGoal && waterData != Water(
                                0,
                                0
                            )
                        ) {
                            CompletedScreen(waterDrunk = waterData.hydrationGoal)
                        } else if (waterData != Water(0, 0)) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState()),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    modifier = Modifier.padding(top = 20.dp, bottom = 40.dp),
                                    text = stringResource(id = R.string.hydration_goal),
                                    fontSize = 25.sp,
                                    textAlign = TextAlign.Center
                                )
                                Utils.CircularProgressBar(
                                    currentValue = waterData.waterDrunk,
                                    maxGoal = waterData.hydrationGoal
                                )
                                Button(
                                    onClick = {
                                        firebaseViewModel.updateWaterCounter(waterData.cupSize)
                                    },
                                    modifier = Modifier.padding(top = 40.dp)
                                ) {
                                    Text(
                                        "+ ${waterData.cupSize}ml",
                                    )
                                }
                            }
                        } else {
                            EmptyScreen(0)
                        }
                    }
                    1 -> {
                        if (listOfFood.isNotEmpty()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, bottom = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = stringResource(id = R.string.total_calories),
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = totalCalories.toString(),
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Divider(thickness = 1.dp)
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(bottom = 20.dp),
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
                                                amount = entry.amount,
                                                openConfirmationDialog = { isOpen ->
                                                    isConfirmationDialogOpen = isOpen
                                                    foodToDelete = entry.name
                                                },
                                                updateTotalCalories = {
                                                    totalCalories = it
                                                }
                                            )
                                        }
                                    }
                                    Divider(thickness = 1.dp)
                                }
                            }
                        } else {
                            EmptyScreen(1)
                        }
                    }
                }
            }
        }
    }
    AddWaterDialog(
        dialogIsOpen = isWaterDialogOpen,
        dialogOpen = { isOpen ->
            isWaterDialogOpen = isOpen
        }
    )
    AddFoodDialog(
        dialogIsOpen = isFoodDialogOpen,
        dialogOpen = { isOpen ->
            isFoodDialogOpen = isOpen
        }
    )
    DeleteConfirmationDialog(
        dialogIsOpen = isConfirmationDialogOpen,
        dialogOpen = { isOpen ->
            isConfirmationDialogOpen = isOpen
        },
        item = foodToDelete,
    )
}

/**
 * Displays a card representing a goal with a checkbox and title
 *
 * @param name The title of the goal
 * @param amount The title of the goal
 * @param openConfirmationDialog Function to toggle the dialog's visibility.
 * @param firebaseViewModel ViewModel providing access to Firebase services.
 * @param updateTotalCalories  lambda function that takes an integer argument
 * representing the total calories
 */
@Composable
fun FoodCard(
    name: String,
    amount: Int,
    openConfirmationDialog: (Boolean) -> Unit = {},
    firebaseViewModel: FirebaseViewModel = viewModel(),
    updateTotalCalories: (Int) -> Unit = {}
) {
    var totalKcal by rememberSaveable { mutableStateOf(0) }
    firebaseViewModel.getFoodsTotalCalories(name) { kcal ->
        totalKcal = kcal
    }
    firebaseViewModel.getTotalCaloriesForADay { kcal ->
        updateTotalCalories(kcal)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        ) {
            Text(
                text = name,
                fontSize = 25.sp,
            )
            Row(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.kcal),
                    fontSize = 20.sp
                )
                Text(
                    text = totalKcal.toString(),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(id = R.string.amount),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(
                        start = 20.dp,
                    )
                )
                Text(
                    text = amount.toString(),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column() {
                IconButton(
                    onClick = {
                        firebaseViewModel.countUp(name)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = stringResource(id = R.string.add)
                    )
                }
                IconButton(
                    onClick = {
                        firebaseViewModel.countDown(name)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Remove,
                        contentDescription = stringResource(id = R.string.remove_icon)
                    )
                }
            }
            IconButton(
                onClick = {
                    openConfirmationDialog(true)
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = stringResource(id = R.string.delete_icon)
                )
            }
        }
    }
}

/**
 * Appears when there is no data in database
 *
 * @param tabIndex indicates selected tab
 */
@Composable
private fun EmptyScreen(tabIndex: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        if (tabIndex == 0) {
            Icon(
                modifier = Modifier
                    .size(100.dp)
                    .alpha(0.3f),
                imageVector = Icons.Default.FormatColorReset,
                contentDescription = stringResource(id = R.string.empty_hydration_tab)
            )
            Text(
                modifier = Modifier
                    .alpha(0.3f),
                text = stringResource(id = R.string.nutrition_text1),
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        } else {
            Icon(
                modifier = Modifier
                    .size(100.dp)
                    .alpha(0.3f),
                imageVector = Icons.Default.NoMeals,
                contentDescription = stringResource(id = R.string.empty_eating_tab)
            )
            Text(
                modifier = Modifier
                    .alpha(0.3f),
                text = stringResource(id = R.string.nutrition_text2),
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Appears when water goal has been achieved
 *
 * @param waterDrunk indicates drunk water
 */
@Composable
private fun CompletedScreen(waterDrunk: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(bottom = 20.dp),
            text = stringResource(id = R.string.completed_text),
            fontSize = 25.sp,
            textAlign = TextAlign.Center
        )
        Icon(
            modifier = Modifier
                .size(100.dp),
            imageVector = Icons.Default.WaterDrop,
            contentDescription = stringResource(id = R.string.completed_hydration),
            tint = MaterialTheme.colorScheme.tertiaryContainer
        )
        Text(
            text = "$waterDrunk ml",
            fontSize = 50.sp
        )
    }
}

/**
 * Displays a alert dialog which asks the user to confirm
 * removing an item from database
 *
 * @param dialogIsOpen Boolean indicating whether the dialog should be displayed or not.
 * @param dialogOpen Function to toggle the dialog's visibility.
 * @param firebaseViewModel ViewModel providing access to Firebase services.
 * @param item item to be removed
 */
@Composable
fun DeleteConfirmationDialog(
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
    firebaseViewModel: FirebaseViewModel = viewModel(),
    item: String
) {
    if (dialogIsOpen) {
        AlertDialog(
            onDismissRequest = { dialogOpen(false) },
            title = {
                Text(
                    text = stringResource(id = R.string.click_to_confirm),
                    fontSize = 20.sp
                )
            },
            text = {

                Text(
                    text = stringResource(id = R.string.confirmation_text, item),
                    fontSize = 15.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        dialogOpen(false)
                        firebaseViewModel.deleteFood(item)
                    }
                ) {
                    Text(stringResource(R.string.confirm_button))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        dialogOpen(false)
                    }
                ) {
                    Text(stringResource(R.string.cancel_button))
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview
@Composable
fun NutritionScreenPreview() {
    val navController = rememberNavController()
    MyVitaLifeTheme(dynamicColor = false) {
        NutritionScreen(navController)
    }
}