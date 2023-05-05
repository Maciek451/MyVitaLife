package uk.ac.aber.dcs.cs39440.myvitalife.ui.account

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel

/**
 * Displays a dialog that allows to set/change username
 *
 * @param dialogIsOpen Boolean indicating whether the dialog should be displayed or not.
 * @param dialogOpen Function to toggle the dialog's visibility.
 * @param firebaseViewModel ViewModel providing access to Firebase services.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetNameDialog(
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
    var userName by remember {
        mutableStateOf("")
    }

    if (dialogIsOpen) {
        Dialog(
            onDismissRequest = { dialogOpen(false) },
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
                            onClick = {
                                userName = ""
                                dialogOpen(false)
                            })
                        {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(id = R.string.close_icon),
                                modifier = Modifier.alpha(0.7f),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        Text(
                            text = stringResource(id = R.string.set_user_name),
                            fontSize = 20.sp
                        )

                        IconButton(
                            onClick = {
                                firebaseViewModel.addUserName(userName)

                                userName = ""

                                dialogOpen(false)
                            },
                            enabled = userName.isNotEmpty()
                        )
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = stringResource(id = R.string.save_button),
                                modifier = Modifier.alpha(0.7f),
                            )
                        }
                    }
                    val maxChar = 10

                    OutlinedTextField(
                        value = userName,
                        onValueChange = {
                            if (it.length <= maxChar) {
                                userName = it
                            }
                        },
                        label = { Text(text = stringResource(id = R.string.user_name)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                    )
                }
            }
        }
    }
}