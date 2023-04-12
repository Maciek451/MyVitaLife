package uk.ac.aber.dcs.cs39440.myvitalife.ui.nutrition

import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
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
        Dialog(
            onDismissRequest = { dialogOpen(false) },
        ) {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.tertiaryContainer
            ) {
                ConstraintLayout(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                ) {
                    val (mainText, firstField, secondField, saveButton, cancelButton, error1, error2) = createRefs()

                    Text(
                        text = stringResource(id = R.string.your_water_goal),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .constrainAs(mainText) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                        fontSize = 20.sp
                    )
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
                            .padding(bottom = 8.dp)
                            .constrainAs(firstField) {
                                start.linkTo(parent.start)
                                top.linkTo(mainText.bottom)
                            },
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
                            modifier = Modifier
                                .constrainAs(error1) {
                                    top.linkTo(firstField.bottom)
                                    bottom.linkTo(secondField.top)
                                }
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
                            .padding(bottom = 8.dp)
                            .constrainAs(secondField) {
                                start.linkTo(parent.start)
                                top.linkTo(firstField.bottom)
                            },
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
                            modifier = Modifier.constrainAs(error2) {
                                top.linkTo(secondField.bottom)
                                bottom.linkTo(cancelButton.top)
                            }
                        )
                    }

                    Button(
                        onClick = {
                            cupSize = ""
                            hydrationGoal = ""
                            waterGoalError = false
                            cupSizeError = false
                            dialogOpen(false)
                        },
                        modifier = Modifier
                            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                            .constrainAs(cancelButton) {
                                top.linkTo(secondField.bottom)
                                start.linkTo(parent.absoluteLeft)
                            }
                            .height(50.dp)
                            .width(120.dp),
                    ) {
                        Text(stringResource(R.string.cancel_button))
                    }

                    Button(
                        onClick = {
                            firebaseViewModel.addWater(cupSize, hydrationGoal)

                            hydrationGoal = ""
                            cupSize = ""

                            dialogOpen(false)
                        },
                        modifier = Modifier
                            .padding(end = 16.dp, top = 8.dp, bottom = 8.dp)
                            .constrainAs(saveButton) {
                                top.linkTo(secondField.bottom)
                                end.linkTo(parent.end)
                            }
                            .height(50.dp)
                            .width(120.dp),
                        enabled = !waterGoalError && !cupSizeError && cupSize.isNotEmpty() && hydrationGoal.isNotEmpty()
                    ) {
                        Text(stringResource(R.string.confirm_button))
                    }
                }
            }
        }
    }
}