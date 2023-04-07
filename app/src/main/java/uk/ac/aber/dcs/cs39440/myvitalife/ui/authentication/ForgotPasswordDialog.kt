package uk.ac.aber.dcs.cs39440.myvitalife.ui.authentication

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordDialog(
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
    var email by rememberSaveable {
        mutableStateOf("")
    }

    val context = LocalContext.current

    if (dialogIsOpen) {
        Dialog(
            onDismissRequest = { dialogOpen(false) }
        ) {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surface
            ) {
                ConstraintLayout(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                ) {
                    val (mainText, firstField, sendButton, cancelButton) = createRefs()

                    Text(
                        text = stringResource(id = R.string.reset_your_password_text),
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
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(text = stringResource(id = R.string.email)) },
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
                            firebaseViewModel.sendPasswordResetEmail(email, context)

                            email = ""

                            dialogOpen(false)
                        },
                        modifier = Modifier
                            .padding(end = 16.dp, top = 8.dp, bottom = 8.dp)
                            .constrainAs(sendButton) {
                                top.linkTo(firstField.bottom)
                                end.linkTo(parent.end)
                            }
                            .height(50.dp)
                            .width(120.dp),
                        enabled = email.isNotEmpty()
                    ) {
                        Text(stringResource(R.string.confirm_button))
                    }

                    Button(
                        onClick = {
                            email = ""
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