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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel

/**
 * Screen for adding water goal
 *
 * @param dialogIsOpen Boolean indicating whether the dialog should be displayed or not.
 * @param dialogOpen Function to toggle the dialog's visibility.
 * @param firebaseViewModel ViewModel providing access to Firebase services.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWaterDialog(
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
    var hydrationGoal by remember {
        mutableStateOf("")
    }
    var cupSize by remember {
        mutableStateOf("")
    }

    var waterGoalError by rememberSaveable { mutableStateOf(false) }
    var cupSizeError by rememberSaveable { mutableStateOf(false) }

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
                                cupSize = ""
                                hydrationGoal = ""
                                waterGoalError = false
                                cupSizeError = false
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
                            text = stringResource(id = R.string.your_water_goal),
                            fontSize = 20.sp
                        )

                        IconButton(
                            onClick = {
                                firebaseViewModel.addWater(cupSize, hydrationGoal)
                                hydrationGoal = ""
                                cupSize = ""
                                dialogOpen(false)
                            },
                            enabled = !waterGoalError && !cupSizeError && cupSize.isNotEmpty() && hydrationGoal.isNotEmpty()
                        )
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = stringResource(id = R.string.save_button),
                                modifier = Modifier.alpha(0.7f),
                            )
                        }
                    }
                    val maxChar = 5

                    OutlinedTextField(
                        value = hydrationGoal,
                        onValueChange = {
                            if (it.length <= maxChar) {
                                hydrationGoal = it
                            }
                            waterGoalError =
                                (hydrationGoal.toIntOrNull() == null && hydrationGoal.isNotEmpty())
                        },
                        label = { Text(text = stringResource(id = R.string.amount_of_water)) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                        ),
                        isError = waterGoalError,
                    )
                    if (waterGoalError) {
                        Text(
                            text = stringResource(id = R.string.must_be_a_number),
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }

                    OutlinedTextField(
                        value = cupSize,
                        onValueChange = {
                            if (it.length <= maxChar) {
                                cupSize = it
                            }
                            cupSizeError = (cupSize.toIntOrNull() == null && cupSize.isNotEmpty())
                        },
                        label = { Text(text = stringResource(id = R.string.cup_size)) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                        ),
                        isError = cupSizeError,
                    )
                    if (cupSizeError) {
                        Text(
                            text = stringResource(id = R.string.must_be_a_number),
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            }
        }
    }
}