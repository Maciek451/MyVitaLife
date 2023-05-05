package uk.ac.aber.dcs.cs39440.myvitalife.ui.authentication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel

/**
 * Displays a dialog that allows to enter email address to receive
 * email with password reset link
 *
 * @param dialogIsOpen Boolean indicating whether the dialog should be displayed or not.
 * @param dialogOpen Function to toggle the dialog's visibility.
 * @param firebaseViewModel ViewModel providing access to Firebase services.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordDialog(
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
    var email by rememberSaveable {
        mutableStateOf("")
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
                            onClick = {
                                email = ""
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
                            text = stringResource(id = R.string.reset_password),
                            fontSize = 20.sp
                        )

                        IconButton(
                            onClick = {
                                firebaseViewModel.sendPasswordResetEmail(email, context)

                                email = ""

                                dialogOpen(false)
                            },
                            enabled = email.isNotEmpty()
                        )
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = stringResource(id = R.string.save_button),
                                modifier = Modifier.alpha(0.7f),
                            )
                        }
                    }

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(text = stringResource(id = R.string.email)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                    )
                }
            }
        }
    }
}