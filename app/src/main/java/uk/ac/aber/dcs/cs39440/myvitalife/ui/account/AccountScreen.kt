package uk.ac.aber.dcs.cs39440.myvitalife.ui.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.Authentication
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.components.TopAppBarWithArrow
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screen

@Composable
fun AccountScreen(
    navController: NavHostController,
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
    val title = R.string.account

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBarWithArrow(navController, title)
        Text(text = Authentication.userEmail)
        Button(
            onClick = {
                firebaseViewModel.signOut {  }
                navController.popBackStack(navController.graph.startDestinationId, false)
                navController.navigate(Screen.LoginSignIn.route)
            },
            modifier = Modifier
                .padding(top = 8.dp)
        ) {
            Text(text = stringResource(id = R.string.signOut))
        }
    }
}