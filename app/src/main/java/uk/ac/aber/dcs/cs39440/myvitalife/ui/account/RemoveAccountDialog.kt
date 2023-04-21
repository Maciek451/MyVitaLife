package uk.ac.aber.dcs.cs39440.myvitalife.ui.account

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoveAccountDialog(
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
    firebaseViewModel: FirebaseViewModel = viewModel(),
    navController: NavHostController
) {
    val context = LocalContext.current
    val prompt = stringResource(id = R.string.account_removed)
    var confirm by remember { mutableStateOf("") }

    if (dialogIsOpen) {
        AlertDialog(
            onDismissRequest = { dialogOpen(false) },
            title = {
                Text(
                    text = stringResource(id = R.string.remove_account_prompt1),
                    fontSize = 20.sp
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.remove_account_prompt2),
                        fontSize = 15.sp
                    )
                    OutlinedTextField(
                        value = confirm,
                        onValueChange = { confirm = it },
                        modifier = Modifier
                            .padding(top = 10.dp),
                        label = { Text(text = stringResource(id = R.string.confirm_delete)) },
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        dialogOpen(false)
                        firebaseViewModel.deleteAccount() { deletedSuccessfully ->
                            if (deletedSuccessfully) {
                                navController.navigate(Screens.SignIn.route)
                            }
                        }
                        Toast.makeText(context, prompt, Toast.LENGTH_LONG).show()
                    },
                    enabled = confirm == stringResource(id = R.string.confirm_capital)
                ) {
                    Text(stringResource(R.string.confirm_button))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        dialogOpen(false)
                    }
                ) {
                    Text(stringResource(R.string.cancel_button))
                }
            }
        )
    }
}