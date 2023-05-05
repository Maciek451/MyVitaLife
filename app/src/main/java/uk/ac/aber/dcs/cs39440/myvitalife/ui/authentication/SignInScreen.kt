package uk.ac.aber.dcs.cs39440.myvitalife.ui.authentication

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.Authentication
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screens
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme

/**
 * Screen for login user to the app
 *
 * @param navController NavController manages app navigation
 * @param firebaseViewModel ViewModel providing access to Firebase services.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    navController: NavHostController,
    firebaseViewModel: FirebaseViewModel = viewModel()
) {

    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    var errorMessage by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current

    var isPasswordDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isVerificationDialogOpen by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(150.dp),
            painter = painterResource(id = R.drawable.lifestyle),
            contentDescription = stringResource(id = R.string.app_icon),
            contentScale = ContentScale.Crop
        )
        Text(
            text = stringResource(id = R.string.app_name),
            modifier = Modifier
                .padding(top = 25.dp),
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
                .padding(top = 50.dp, bottom = 3.dp),
            isError = errorMessage.isNotEmpty()
        )

        var passwordVisible by rememberSaveable { mutableStateOf(false) }
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
                .padding(bottom = 8.dp),
            isError = errorMessage.isNotEmpty(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                val description =
                    if (passwordVisible) stringResource(id = R.string.hide_password) else stringResource(id = R.string.show_password)

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
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
                firebaseViewModel.signInWithEmailAndPassword(email, password) { returnVal ->
                    errorMessage = processSignInUi(returnVal)
                    when (returnVal) {
                        Authentication.LOGGED_IN_SUCCESSFULLY -> {
                            navController.navigate(Screens.Insights.route) { popUpTo(0) }
                        }
                        Authentication.USER_IS_NOT_VERIFIED -> {
                            isVerificationDialogOpen = true
                        }
                        else -> {
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            },
            modifier = Modifier
                .padding(top = 10.dp),
            enabled = email.isNotEmpty() && password.isNotEmpty()
        ) {
            Text(text = stringResource(id = R.string.sign_in_button))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = {
                    navController.navigate(Screens.SignUp.route)
                },
                modifier = Modifier
                    .padding(top = 10.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.create_account),
                    textDecoration = TextDecoration.Underline,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            TextButton(
                onClick = {
                    isPasswordDialogOpen = true
                },
                modifier = Modifier
                    .padding(top = 10.dp),
            ) {
                Text(
                    text = stringResource(id = R.string.forgot_password),
                    textDecoration = TextDecoration.Underline,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
    ForgotPasswordDialog(
        dialogIsOpen = isPasswordDialogOpen,
        dialogOpen = { isOpen ->
            isPasswordDialogOpen = isOpen
        }
    )
    VerificationDialog(
        dialogIsOpen = isVerificationDialogOpen,
        dialogOpen = { isOpen ->
            isVerificationDialogOpen = isOpen
        },
        firebaseViewModel = firebaseViewModel
    )
}

/**
 * Displays a dialog that informs user about verification process
 *
 * @param dialogIsOpen Boolean indicating whether the dialog should be displayed or not.
 * @param dialogOpen Function to toggle the dialog's visibility.
 * @param firebaseViewModel ViewModel providing access to Firebase services.
 */
@Composable
fun VerificationDialog(
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
    val context = LocalContext.current

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
                    text = stringResource(id = R.string.verification_needed_1),
                    fontSize = 15.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        firebaseViewModel.sendVerificationEmail(context)
                        dialogOpen(false)
                    }
                ) {
                    Text(stringResource(R.string.resend_email))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        firebaseViewModel.signOut()
                        dialogOpen(false)
                    }
                ) {
                    Text(text = stringResource(R.string.try_again))
                }
            }
        )
    }
}

/**
 * Processes error in the UI on the error code provided.
 */
private fun processSignInUi(errorCode: Int): String {
    when (errorCode) {
        Authentication.LOGGED_IN_SUCCESSFULLY ->
            return "User logged in successfully"
        Authentication.PASSWORD_WRONG ->
            return "Wrong password"
        Authentication.ACCOUNT_DOES_NOT_EXIST ->
            return "Account does not exist"
        Authentication.EMAIL_WRONG_FORMAT ->
            return "Wrong format of the email"
        // Dialog open instead
        Authentication.USER_IS_NOT_VERIFIED ->
            return "User is not verified"
    }
    return "Unknown issue"
}

@Composable
@Preview
private fun ProvideNamePreview() {
    val navController = rememberNavController()
    MyVitaLifeTheme(dynamicColor = false) {
        SignInScreen(navController)
    }
}