package uk.ac.aber.dcs.cs39440.myvitalife.ui.authentication

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.Authentication
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.components.TopAppBarWithArrow
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavHostController,
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
    val title = R.string.app_name
    var email by remember {
        mutableStateOf("")
    }
    var username by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var passwordConfirmation by remember {
        mutableStateOf("")
    }
    var isVerificationDialogOpen by rememberSaveable { mutableStateOf(false) }

    var errorMessage by rememberSaveable { mutableStateOf("") }
    var passwordError by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopAppBarWithArrow(navController, title)
        Text(
            text = stringResource(id = R.string.create_account),
            modifier = Modifier.padding(bottom = 25.dp, top = 120.dp),
            fontSize = 30.sp
        )
        OutlinedTextField(
            value = email,
            label = {
                Text(text = stringResource(id = R.string.email))
            },
            onValueChange = {
                email = it
                errorMessage = ""
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 25.dp, start = 8.dp, end = 8.dp),
            isError = errorMessage.isNotEmpty()
        )
        OutlinedTextField(
            value = username,
            label = {
                Text(text = stringResource(id = R.string.user_name_optional))
            },
            onValueChange = {
                username = it
                errorMessage = ""
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            isError = errorMessage.isNotEmpty()
        )

        var passwordVisible by rememberSaveable { mutableStateOf(false) }
        var passwordConfirmationVisible by rememberSaveable { mutableStateOf(false) }
        OutlinedTextField(
            value = password,
            label = {
                Text(text = stringResource(id = R.string.password))
            },
            onValueChange = {
                password = it
                errorMessage = ""
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp),
            isError = passwordError,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                // Please provide localized description for accessibility services
                val description = if (passwordVisible) stringResource(id = R.string.hide_password) else stringResource(id = R.string.show_password)

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            }
        )
        OutlinedTextField(
            value = passwordConfirmation,
            label = {
                Text(text = stringResource(id = R.string.confirm_password))
            },
            onValueChange = {
                passwordConfirmation = it
                errorMessage = ""
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, start = 8.dp, end = 8.dp),
            isError = passwordError,
            visualTransformation = if (passwordConfirmationVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordConfirmationVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                // Please provide localized description for accessibility services
                val description = if (passwordConfirmationVisible) stringResource(id = R.string.hide_password) else stringResource(id = R.string.show_password)

                IconButton(onClick = {
                    passwordConfirmationVisible = !passwordConfirmationVisible
                }) {
                    Icon(imageVector = image, description)
                }
            }
        )
        Text(
            text = "",
            color = MaterialTheme.colorScheme.error
        )

        Button(
            onClick = {
                passwordError = passwordConfirmation != password
                if (!passwordError) {
                    firebaseViewModel.signUpWithEmailAndPassword(
                        email,
                        password,
                        username
                    ) { returnVal ->
                        errorMessage = processLoginUi(returnVal)
                        if (returnVal == Authentication.SIGNED_UP_SUCCESSFULLY) {
                            firebaseViewModel.sendVerificationEmail(context)
                            isVerificationDialogOpen = true
                        }
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        errorMessage = ""
                    }
                }
            },
            modifier = Modifier
                .padding(top = 25.dp),
            enabled = email.isNotEmpty() && password.isNotEmpty() && passwordConfirmation.isNotEmpty()
        ) {
            Text(text = stringResource(id = R.string.sign_up_button))
        }
    }
    VerificationInfoDialog(
        dialogIsOpen = isVerificationDialogOpen,
        dialogOpen = { isOpen ->
            isVerificationDialogOpen = isOpen
        },
        navController = navController
    )
}

@Composable
fun VerificationInfoDialog(
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
    navController: NavController
) {
    if (dialogIsOpen) {
        AlertDialog(
            onDismissRequest = { dialogOpen(false) },
            title = {
                Text(
                    text = stringResource(id = R.string.verification_needed_title),
                    fontSize = 20.sp
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.verification_needed_2),
                    fontSize = 15.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        dialogOpen(false)
                        navController.navigate(Screens.SignIn.route)
                    }
                ) {
                    Text(stringResource(R.string.ok))
                }
            }

        )
    }
}

private fun processLoginUi(errorCode: Int): String {
    when (errorCode) {
        Authentication.SIGNED_UP_SUCCESSFULLY ->
            return "User signed up successfully"
        Authentication.USER_ALREADY_EXISTS ->
            return "User already exists"
        Authentication.EMAIL_WRONG_FORMAT ->
            return "Wrong format of the email"
        Authentication.PASSWORD_WRONG_FORMAT ->
            return "Password has to have 6 signs"
    }
    return "Unknown issue"
}