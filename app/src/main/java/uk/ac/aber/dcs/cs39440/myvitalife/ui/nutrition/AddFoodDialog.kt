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
            onDismissRequest = { },
        ) {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surface
            ) {
                ConstraintLayout(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                ) {
                    val (mainText, firstField, secondField, saveButton, cancelButton, error) = createRefs()

                    Text(
                        text = stringResource(id = R.string.add_your_food),
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
                            .padding(bottom = 8.dp)
                            .constrainAs(firstField) {
                                start.linkTo(parent.start)
                                top.linkTo(mainText.bottom)
                            }
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
                            .padding(bottom = 8.dp)
                            .constrainAs(secondField) {
                                start.linkTo(parent.start)
                                top.linkTo(firstField.bottom)
                            },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                        ),
                        isError = isError,
                    )
                    if (isError) {
                        Text(
                            text = stringResource(id = R.string.must_be_a_number),
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.constrainAs(error) {
                                top.linkTo(secondField.bottom)
                                bottom.linkTo(cancelButton.top)
                            }
                        )
                    }

                    Button(
                        onClick = {
                            numberOfCalories = ""
                            nameOfFood = ""
                            isError = false
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
                            firebaseViewModel.addFood(nameOfFood, numberOfCalories)

                            numberOfCalories = ""
                            nameOfFood = ""
                            firstValueOfKcal = ""

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
                        enabled = !isError && numberOfCalories.isNotEmpty() && nameOfFood.isNotEmpty()
                    ) {
                        Text(stringResource(R.string.confirm_button))
                    }
                }
            }
        }
    }
}
