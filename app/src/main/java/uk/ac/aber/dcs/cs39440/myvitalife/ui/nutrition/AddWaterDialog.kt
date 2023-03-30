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
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel

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
        AlertDialog(
            onDismissRequest = { /* Empty so clicking outside has no effect */ },
            title = {
                Text(
                    text = "Add your hydration goal",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 20.sp
                )
            },
            modifier = Modifier
                .height(385.dp)
                .width(600.dp),
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.amount_of_water),
                        fontSize = 15.sp,
                    )
                    OutlinedTextField(
                        value = hydrationGoal,
                        onValueChange = {
                            hydrationGoal = it
                            waterGoalError = (hydrationGoal.toIntOrNull() == null && !hydrationGoal.isEmpty())
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth(),
                        isError = waterGoalError,
                    )
                    if (waterGoalError) {
                        Text(
                            text = stringResource(id = R.string.must_be_a_number),
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.cup_size),
                        fontSize = 15.sp,
                    )
                    OutlinedTextField(
                        value = cupSize,
                        onValueChange = {
                            cupSize = it
                            cupSizeError = (cupSize.toIntOrNull() == null && !cupSize.isEmpty())
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth(),
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
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        firebaseViewModel.addWater(cupSize, hydrationGoal)

                        hydrationGoal = ""
                        cupSize = ""

                        dialogOpen(false)
                    },
                    enabled = !waterGoalError && !cupSizeError && cupSize.isNotEmpty() && hydrationGoal.isNotEmpty()
                ) {
                    Text(stringResource(R.string.confirm_button))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        cupSize = ""
                        hydrationGoal = ""
                        waterGoalError = false
                        cupSizeError = false
                        dialogOpen(false)
                    }
                ) {
                    Text(stringResource(R.string.cancel_button))
                }
            }
        )
    }
}