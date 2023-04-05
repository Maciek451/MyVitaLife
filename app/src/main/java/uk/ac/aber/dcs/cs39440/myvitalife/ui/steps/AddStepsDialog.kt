package uk.ac.aber.dcs.cs39440.myvitalife.ui.steps

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStepsDialog(
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
    var stepsGoal by rememberSaveable {
        mutableStateOf("")
    }

    if (dialogIsOpen) {
        Dialog(
            onDismissRequest = { /* Empty so clicking outside has no effect */ }
        ) {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surface
            ) {
                ConstraintLayout(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                ) {
                    val (mainText, firstField, saveButton, cancelButton) = createRefs()

                    Text(
                        text = stringResource(id = R.string.steps_goal),
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

                    OutlinedTextField(
                        value = stepsGoal,
                        onValueChange = { stepsGoal = it },
                        label = { Text(text = stringResource(id = R.string.number_of_steps)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .constrainAs(firstField) {
                                start.linkTo(parent.start)
                                top.linkTo(mainText.bottom)
                            }
                    )

                    Button(
                        onClick = {
//                            firebaseViewModel.addGoal(goalTitle, false)

                            stepsGoal = ""

                            dialogOpen(false)
                        },
                        modifier = Modifier
                            .padding(end = 16.dp, top = 8.dp, bottom = 8.dp)
                            .constrainAs(saveButton) {
                                top.linkTo(firstField.bottom)
                                end.linkTo(parent.end)
                            }
                            .height(50.dp)
                            .width(120.dp),
                        enabled = stepsGoal.isNotEmpty()
                    ) {
                        Text(stringResource(R.string.confirm_button))
                    }

                    Button(
                        onClick = {
                            stepsGoal = ""
                            dialogOpen(false)
                        },
                        modifier = Modifier
                            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                            .constrainAs(cancelButton) {
                                top.linkTo(firstField.bottom)
                                start.linkTo(parent.absoluteLeft)
                            }
                            .height(50.dp)
                            .width(120.dp),
                    ) {
                        Text(stringResource(R.string.cancel_button))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AddStepsScreenPreview() {
    val navController = rememberNavController()
    MyVitaLifeTheme(dynamicColor = false) {
//        AddStepsDialog(navController)
    }
}