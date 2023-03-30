package uk.ac.aber.dcs.cs39440.myvitalife.ui.nutrition

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.database.*
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.utils.*
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodDialog(
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
    var nameOfFood by remember {
        mutableStateOf("")
    }
    var numberOfCalories by remember {
        mutableStateOf("")
    }

    var isError by rememberSaveable { mutableStateOf(false) }

    if (dialogIsOpen) {
        AlertDialog(
            onDismissRequest = { /* Empty so clicking outside has no effect */ },
            title = {
                Text(
                    text = "Add your food",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 20.sp
                )
            },
            modifier = Modifier.height(375.dp).width(600.dp),
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.name_of_food),
                        fontSize = 15.sp,
                    )
                    OutlinedTextField(
                        value = nameOfFood,
                        onValueChange = { nameOfFood = it },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Text(
                        text = stringResource(id = R.string.number_of_calories),
                        fontSize = 15.sp,
                    )
                    OutlinedTextField(
                        value = numberOfCalories,
                        onValueChange = {
                            numberOfCalories = it
                            isError = (numberOfCalories.toIntOrNull() == null && !numberOfCalories.isEmpty())},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth(),
                        isError = isError,
                    )
                    if (isError) {
                        Text(
                            text = stringResource(id = R.string.must_be_a_number),
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        firebaseViewModel.addFood(nameOfFood, numberOfCalories)

                        numberOfCalories = ""
                        nameOfFood = ""

                        dialogOpen(false)
                    },
                    enabled = !isError && numberOfCalories.isNotEmpty() && nameOfFood.isNotEmpty()
                ) {
                    Text(stringResource(R.string.confirm_button))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        numberOfCalories = ""
                        nameOfFood = ""
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