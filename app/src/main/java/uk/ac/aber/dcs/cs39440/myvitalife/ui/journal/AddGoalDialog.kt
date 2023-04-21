package uk.ac.aber.dcs.cs39440.myvitalife.ui.journal

import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGoalDialog(
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
    var goalTitle by rememberSaveable {
        mutableStateOf("")
    }

    if (dialogIsOpen) {
        Dialog(
            onDismissRequest = { dialogOpen(false) }
        ) {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surfaceVariant
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
                                goalTitle = ""
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
                            text = stringResource(id = R.string.add_your_goal),
                            fontSize = 20.sp
                        )

                        IconButton(
                            onClick = {
                                firebaseViewModel.addGoal(goalTitle, false)

                                goalTitle = ""

                                dialogOpen(false)
                            },
                            enabled = goalTitle.isNotEmpty()
                        )
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = stringResource(id = R.string.save_button),
                                modifier = Modifier.alpha(0.7f),
                            )
                        }
                    }
                    val maxChar = 30

                    OutlinedTextField(
                        value = goalTitle,
                        onValueChange = {
                            if (it.length <= maxChar) {
                                goalTitle = it
                            }
                        },
                        label = { Text(text = stringResource(id = R.string.add_your_goal_description)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                    )
                }
            }
        }
    }
}