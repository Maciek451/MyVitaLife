package uk.ac.aber.dcs.cs39440.myvitalife.ui.nutrition

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.model.Food
import uk.ac.aber.dcs.cs39440.myvitalife.ui.add_food.AddFoodDialog
import uk.ac.aber.dcs.cs39440.myvitalife.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme
import uk.ac.aber.dcs.cs39440.myvitalife.utils.Utils

@Composable
fun NutritionScreen(
    navController: NavHostController,
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val listOfFood = getFoodList()

    var isDialogOpen by rememberSaveable { mutableStateOf(false) }

    //The LocalContext is a Compose function that provides access to the current context of the application,
    // which is required for many operations such as creating views or accessing resources.
    val context = LocalContext.current

    TopLevelScaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (selectedTabIndex == 0) {
                        navController.navigate(Screen.AddWater.route)
                    } else {
//                        navController.navigate(Screen.AddFood.route)
                        isDialogOpen = true;
                    }
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
                    }
                    1 -> {
                        if(listOfFood.isNotEmpty()) {
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
                                        FoodCard(name = entry.name, kcal = entry.kcal)
                                    }
                                }
                            }
                        } else {
                            EmptyList()
                        }
                    }
                }
            }
        }
    }

   AddFoodDialog(
       dialogIsOpen = isDialogOpen,
       dialogOpen = { isOpen ->
           isDialogOpen = isOpen
       }
    )
}
@Composable
private fun getFoodList(): List<Food> {
    val foodList = remember { mutableStateOf(emptyList<Food>()) }

    // on below line creating variable for message.
    val firebaseDatabase = FirebaseDatabase.getInstance();
    val databaseReference =
        firebaseDatabase.getReference(Utils.getCurrentDate())
            .child("ListOfFood")

    databaseReference.addValueEventListener(object :
        ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val foods = mutableListOf<Food>()

            var calories = 0

            // loop over the children of the "foods" node to retrieve each food
            for (foodSnapshot in snapshot.children) {
                // retrieve the food name and kcal value
                val foodName = foodSnapshot.key
                val kcal = foodSnapshot.child("kcal").value.toString().toInt()

                // add the food to the list
                foods.add(Food(name = foodName!!, kcal = kcal))
                calories += kcal
            }

            Log.d("EPIC TEST", "Total calories: $calories")
            // update the state with the new list of foods
            foodList.value = foods
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e(TAG, "Failed to read value.", error.toException())
        }
    })

    return foodList.value
}


@Composable
private fun FoodCard(name: String, kcal: Int){
    Column(horizontalAlignment = Alignment.Start) {
        Text(
            text = name,
            modifier = Modifier.padding(
                start = 8.dp,
                top = 8.dp
            )
        )
        Text(
            text = kcal.toString(),
            fontSize = 20.sp,
            color = Color.Blue,
            modifier = Modifier.padding(
                start = 8.dp,
                bottom = 24.dp
            )
        )
    }
}

@Composable
private fun EmptyList() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
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