package uk.ac.aber.dcs.cs39440.myvitalife.ui.journal

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.runBlocking
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screen

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
        AlertDialog(
            onDismissRequest = { /* Empty so clicking outside has no effect */ },
            title = {
                Text(
                    text = stringResource(id = R.string.add_your_goal),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 20.sp
                )
            },
            modifier = Modifier
                .height(260.dp)
                .width(600.dp),
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.add_your_goal_description),
                        fontSize = 25.sp,
                    )
                    OutlinedTextField(
                        value = goalTitle,
                        onValueChange = { goalTitle = it },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        firebaseViewModel.addGoal(goalTitle, false)

                        goalTitle = ""

                        dialogOpen(false)
                    },
                    enabled = goalTitle.isNotEmpty()
                ) {
                    Text(stringResource(R.string.confirm_button))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        goalTitle = ""
                        dialogOpen(false)
                    }
                ) {
                    Text(stringResource(R.string.cancel_button))
                }
            }
        )
    }
}