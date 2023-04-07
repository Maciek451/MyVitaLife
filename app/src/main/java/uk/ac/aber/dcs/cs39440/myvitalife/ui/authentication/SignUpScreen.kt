package uk.ac.aber.dcs.cs39440.myvitalife.ui.authentication

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.Authentication
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavHostController,
    firebaseViewModel: FirebaseViewModel = viewModel()
) {

    var email by remember {
        mutableStateOf("")
    }
    var username by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    var errorMessage by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current

    var isDialogOpen by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.create_account),
            modifier = Modifier.padding(bottom = 25.dp, top = 25.dp),
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
                .padding(top = 25.dp),
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
                .fillMaxWidth(),
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

                // Please provide localized description for accessibility services
                val description = if (passwordVisible) "Hide password" else "Show password"

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
                firebaseViewModel.signUpWithEmailAndPassword(email, password) { returnVal ->
                    errorMessage = processLoginUi(returnVal)
                    if (returnVal == 0) {
                        navController.navigate(Screen.Insights.route)
                        firebaseViewModel.addUserName(username)
                    }
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier
                .padding(top = 10.dp),
            enabled = email.isNotEmpty() && password.isNotEmpty()
        ) {
            Text(text = stringResource(id = R.string.sign_up_button))
        }

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