package uk.ac.aber.dcs.cs39440.myvitalife.ui.add_food

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.*
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.utils.*
import uk.ac.aber.dcs.cs39440.myvitalife.utils.Utils.Companion.getCurrentDate
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodDialog(
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
) {
    //This line of code initializes a reference to the Firebase Realtime Database
    // by calling the getInstance() method of the FirebaseDatabase class
    val firebaseDatabase = FirebaseDatabase.getInstance();

    var name_of_food by remember {
        mutableStateOf("")
    }
    var number_of_calories by remember {
        mutableStateOf("")
    }

    var isError by rememberSaveable { mutableStateOf(false) }

    if (dialogIsOpen) {
        AlertDialog(
            onDismissRequest = { /* Empty so clicking outside has no effect */ },
            title = {
                Text(
                    text = "Add your epic food",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 20.sp
                )
            },
            modifier = Modifier.height(375.dp).width(600.dp),
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Name of the product",
                        fontSize = 25.sp,
                    )
                    OutlinedTextField(
                        value = name_of_food,
                        onValueChange = { name_of_food = it },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Text(
                        text = "Number of calories",
                        fontSize = 25.sp,
                    )
                    OutlinedTextField(
                        value = number_of_calories,
                        onValueChange = {
                            number_of_calories = it
                            isError = (number_of_calories.toIntOrNull() == null && !number_of_calories.isEmpty())},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth(),
                        isError = isError,
                    )
                    if (isError) {
                        Text(
                            text = "Must be a number!",
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val databaseReference =
                            firebaseDatabase.getReference(getCurrentDate()).child("ListOfFood")
                                .child(name_of_food)
                        databaseReference.child("kcal").setValue(number_of_calories)

                        number_of_calories = ""
                        name_of_food = ""

                        dialogOpen(false)
                    },
                    enabled = !isError && number_of_calories.isNotEmpty() && name_of_food.isNotEmpty()
                ) {
                    Text(stringResource(R.string.confirm_button))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        number_of_calories = ""
                        name_of_food = ""
                        isError = false
                        dialogOpen(false)
                    }
                ) {
                    Text(stringResource(R.string.cancel_button))
                }
            }
        )
    }
}