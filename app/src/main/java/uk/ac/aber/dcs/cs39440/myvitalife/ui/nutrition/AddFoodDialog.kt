package uk.ac.aber.dcs.cs39440.myvitalife.ui.nutrition

import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
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
    var firstValueOfKcal by remember {
        mutableStateOf("")
    }

    var isError by rememberSaveable { mutableStateOf(false) }

    if (dialogIsOpen) {
        Dialog(
            onDismissRequest = { dialogOpen(false) },
        ) {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                numberOfCalories = ""
                                nameOfFood = ""
                                isError = false
                                dialogOpen(false)
                            })
                        {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(id = R.string.close_icon),
                                modifier = Modifier.alpha(0.7f),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        Text(
                            text = stringResource(id = R.string.add_your_food),
                            fontSize = 20.sp
                        )

                        IconButton(
                            onClick = {
                                firebaseViewModel.addFood(nameOfFood, numberOfCalories)

                                numberOfCalories = ""
                                nameOfFood = ""
                                firstValueOfKcal = ""

                                dialogOpen(false)
                            },
                            enabled = !isError && numberOfCalories.isNotEmpty() && nameOfFood.isNotEmpty()
                        )
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = stringResource(id = R.string.save_button),
                                modifier = Modifier.alpha(0.7f),
                            )
                        }
                    }
                    val maxChar = 20

                    OutlinedTextField(
                        value = nameOfFood,
                        onValueChange = {
                            if (it.length <= maxChar) {
                                nameOfFood = it
                            }
                        },
                        label = { Text(text = stringResource(id = R.string.name_of_food)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp),
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                    )

                    OutlinedTextField(
                        value = numberOfCalories,
                        onValueChange = {
                            numberOfCalories = it
                            isError =
                                (numberOfCalories.toIntOrNull() == null && numberOfCalories.isNotEmpty())
                        },
                        label = { Text(text = stringResource(id = R.string.number_of_calories)) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                        ),
                        isError = isError,
                    )
                    if (isError) {
                        Text(
                            text = stringResource(id = R.string.must_be_a_number),
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}
