package uk.ac.aber.dcs.cs39440.myvitalife.ui.login_sign_in

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.Authentication
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginSignInScreen(
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

    Column(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(300.dp)
                .padding(bottom = 16.dp, top = 16.dp),
            painter = painterResource(id = R.drawable.providenameimage),
            contentDescription = stringResource(R.string.provide_name_image),
            contentScale = ContentScale.Crop
        )
        OutlinedTextField(
            value = email,
            label = {
                Text(text = stringResource(id = R.string.user_name))
            },
            onValueChange = {
                email = it
                errorMessage = ""
                            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, start = 8.dp, end = 8.dp),
            isError = errorMessage.isNotEmpty()
        )
        Text(
            text = "",
            color = MaterialTheme.colorScheme.error
        )
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
                .padding(bottom = 8.dp, start = 8.dp, end = 8.dp),
            isError = errorMessage.isNotEmpty(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
        )
        Text(
            text = "",
            color = MaterialTheme.colorScheme.error
        )

        Button(
            onClick = {
                firebaseViewModel.logInWithEmailAndPassword(email, password) { returnVal ->
                    errorMessage = processLoginUi(returnVal)
                    if (returnVal == 0) {
                        navController.navigate(Screen.Journal.route)
                    }
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                }

            },
            modifier = Modifier
                .padding(top = 10.dp),
            enabled = email.isNotEmpty() && password.isNotEmpty()
        ) {
            Text(text = stringResource(id = R.string.login_button))
        }

        Button(
            onClick = {
                firebaseViewModel.signInWithEmailAndPassword(email, password) { returnVal ->
                    errorMessage = processLoginUi(returnVal)
                    if (returnVal == 0) {
                        navController.navigate(Screen.Journal.route)
                    }
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier
                .padding(top = 10.dp),
            enabled = email.isNotEmpty() && password.isNotEmpty()
        ) {
            Text(text = stringResource(id = R.string.signIn_button))
        }
    }
}

//if (viewModel.isLoggedIn.value) {
//    // Display content for logged in user
//} else {
//    // Display content for not logged in user
//}
private fun processLoginUi(errorCode: Int): String {
    when (errorCode) {
        Authentication.LOGGED_IN_SUCCESSFULLY ->
            return "User logged in successfully"
        Authentication.PASSWORD_WRONG ->
            return "Wrong password"
        Authentication.ACCOUNT_DOES_NOT_EXIST ->
            return "Account does not exist"
        Authentication.USER_ALREADY_EXISTS ->
            return "User already exists"
        Authentication.EMAIL_WRONG_FORMAT ->
            return "Wrong format of the email"
        Authentication.PASSWORD_WRONG_FORMAT ->
            return "Password has to have 6 signs"
    }
    return "Unknown issue"
}

@Composable
@Preview
private fun ProvideNamePreview() {
    val navController = rememberNavController()
    MyVitaLifeTheme(dynamicColor = false) {
        LoginSignInScreen(navController)
    }
}