package uk.ac.aber.dcs.cs39440.myvitalife.ui.account

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.Authentication
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.components.DeleteAllDataConfirmationDialog
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screens

/**
 * Displays a dialog with information about the user's account
 * and allows them to modify their user name or remove their account.
 *
 * @param dialogIsOpen Boolean indicating whether the dialog should be displayed or not.
 * @param dialogOpen Function to toggle the dialog's visibility.
 * @param firebaseViewModel ViewModel providing access to Firebase services.
 * @param navController NavController manages app navigation
 */
@Composable
fun AccountDialog(
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
    firebaseViewModel: FirebaseViewModel = viewModel(),
    navController: NavHostController
) {
    var userName by rememberSaveable { mutableStateOf("") }
    var signUpDate by rememberSaveable { mutableStateOf("") }

    var isNameDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isRemoveAccountDialogOpen by rememberSaveable { mutableStateOf(false) }

    firebaseViewModel.getUserName {
        userName = it
    }

    firebaseViewModel.getSignUpDate {
        signUpDate = it
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
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { dialogOpen(false) })
                        {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(id = R.string.close_icon),
                                modifier = Modifier.alpha(0.7f),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        Text(
                            text = stringResource(id = R.string.account),
                            fontSize = 20.sp
                        )

                        IconButton(
                            onClick = {
                                dialogOpen(false)
                                firebaseViewModel.signOut { }
                                navController.navigate(Screens.SignIn.route) { popUpTo(0) }
                                Toast.makeText(context, R.string.signedOut, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        )
                        {
                            Icon(
                                imageVector = Icons.Filled.Logout,
                                contentDescription = stringResource(id = R.string.close_icon),
                                modifier = Modifier.alpha(0.7f),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                            .clickable(onClick = {
                                isNameDialogOpen = true
                            })
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                modifier = Modifier.padding(top = 5.dp),
                                text = stringResource(id = R.string.user_name),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Row(
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.padding(bottom = 5.dp),
                                    text = userName,
                                    fontSize = 30.sp
                                )
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = stringResource(id = R.string.close_icon),
                                    modifier = Modifier.alpha(0.5f)
                                )
                            }
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                modifier = Modifier.padding(top = 5.dp),
                                text = stringResource(id = R.string.your_email),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                modifier = Modifier.padding(bottom = 5.dp),
                                text = Authentication.userEmail,
                                fontSize = 20.sp
                            )
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 10.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                modifier = Modifier.padding(top = 5.dp),
                                text = stringResource(id = R.string.created_at),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                modifier = Modifier.padding(bottom = 5.dp),
                                text = signUpDate,
                                fontSize = 20.sp
                            )
                        }
                    }
                    Button(
                        onClick = {
                            isRemoveAccountDialogOpen = true
                        },
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White)
                    )
                    {
                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PersonOff,
                                contentDescription = stringResource(id = R.string.remove_account_icon),
                                modifier = Modifier.alpha(0.7f).padding(end = 3.dp),
                            )
                            Text(
                                text = stringResource(id = R.string.remove_account)
                            )
                        }
                    }
                }
            }
        }
    }
    SetNameDialog(
        dialogIsOpen = isNameDialogOpen,
        dialogOpen = { isOpen ->
            isNameDialogOpen = isOpen
        }
    )
    RemoveAccountDialog(
        dialogIsOpen = isRemoveAccountDialogOpen,
        dialogOpen = { isOpen ->
            isRemoveAccountDialogOpen = isOpen
        },
        navController = navController
    )
}