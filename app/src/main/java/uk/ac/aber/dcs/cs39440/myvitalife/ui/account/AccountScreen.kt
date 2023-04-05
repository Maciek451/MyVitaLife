package uk.ac.aber.dcs.cs39440.myvitalife.ui.account

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.Authentication
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.components.TopAppBarWithArrow
import uk.ac.aber.dcs.cs39440.myvitalife.ui.insights.MoodCounter
import uk.ac.aber.dcs.cs39440.myvitalife.ui.insights.greeting
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.nutrition.AddWaterDialog

@Composable
fun AccountScreen(
    navController: NavHostController,
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
    var userName by rememberSaveable { mutableStateOf("") }
    val title = R.string.account
    var isDialogOpen by rememberSaveable { mutableStateOf(false) }
    firebaseViewModel.getUserName {
        userName = it
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 40.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBarWithArrow(navController, title)
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 40.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.user_name),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = userName,
                    modifier = Modifier.padding(top = 6.dp),
                    fontSize = 40.sp
                )
                Button(
                    onClick = {
                        isDialogOpen = true
                    },
                    modifier = Modifier.padding(top = 6.dp),
                ) {
                    Text(text = stringResource(id = R.string.set_user_name))
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 40.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.your_email),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.padding(top = 6.dp),
                    text = Authentication.userEmail,
                    fontSize = 20.sp
                )
                Button(
                    onClick = {
                        firebaseViewModel.signOut { }
                        navController.popBackStack(navController.graph.startDestinationId, false)
                        navController.navigate(Screen.LoginSignIn.route)
                    },
                    modifier = Modifier.padding(top = 6.dp),
                    colors = ButtonDefaults.buttonColors(Color.Red)
                ) {
                    Text(text = stringResource(id = R.string.signOut))
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 40.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.user_identifier),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = Authentication.userId,
                    modifier = Modifier.padding(top = 6.dp),
                    fontSize = 20.sp
                )
            }
        }
    }
    SetNameDialog(
        dialogIsOpen = isDialogOpen,
        dialogOpen = { isOpen ->
            isDialogOpen = isOpen
        }
    )
}
