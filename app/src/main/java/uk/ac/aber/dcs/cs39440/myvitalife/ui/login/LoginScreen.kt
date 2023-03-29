package uk.ac.aber.dcs.cs39440.myvitalife.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme


//private var user: FirebaseUser? = null

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProvideNameScreen(
    navController: NavHostController,
    firebaseViewModel: FirebaseViewModel = viewModel()
) {

    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

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
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
        )

        OutlinedTextField(
            value = password,
            label = {
                Text(text = stringResource(id = R.string.password))
            },
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
        )

        Button(
            onClick = {
                firebaseViewModel.signInWithEmail(email, password)
                navController.navigate(Screen.Journal.route)
            },
            modifier = Modifier
                .padding(top = 10.dp),
        ) {
            Text(text = stringResource(id = R.string.login_button))
        }

        Button(
            onClick = {
                navController.navigate(Screen.Journal.route)
            },
            modifier = Modifier
                .padding(top = 8.dp)
        ) {
            Text(text = stringResource(id = R.string.cancel_button))
        }
    }
}

//if (viewModel.isLoggedIn.value) {
//    // Display content for logged in user
//} else {
//    // Display content for not logged in user
//}


@Composable
@Preview
private fun ProvideNamePreview() {
    val navController = rememberNavController()
    MyVitaLifeTheme(dynamicColor = false) {
        ProvideNameScreen(navController)
    }
}


//private fun signIn() {
//    val providers = arrayListOf(
//        AuthUI.IdpConfig.EmailBuilder().build()
//    )
//    val signInIntent = AuthUI.getInstance()
//        .createSignInIntentBuilder()
//        .setAvailableProviders(providers)
//        .build()
//}
//
//private val signInLauncher = registerForActivityResult(
//    FirebaseAuthUIActivityResultContract()
//) {
//    res -> this.signInResult(res)
//}
//
//private fun signInResult(result: FirebaseAuthUIAuthenticationResult) {
//    val response = result.idpResponse
//    if (result.resultCode == RESULT_OK) {
//        user = FirebaseAuth.getInstance().currentUser
//    } else {
//        Log.e("LoginScreen.kt", "Error" + response?.error?.errorCode)
//    }
//}
