package uk.ac.aber.dcs.cs39440.myvitalife.ui.journal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
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
 * Screen for adding mood data
 *
 * @param dialogIsOpen Boolean indicating whether the dialog should be displayed or not.
 * @param dialogOpen Function to toggle the dialog's visibility.
 * @param firebaseViewModel ViewModel providing access to Firebase services.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMoodDialog(
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
    var optionalDescription by rememberSaveable { mutableStateOf("") }
    var selectedValue by remember { mutableStateOf(-1) }

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
                                selectedValue = -1
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
                            text = stringResource(id = R.string.add_your_mood_description),
                            fontSize = 20.sp
                        )

                        IconButton(
                            onClick = {
                                firebaseViewModel.addMood(selectedValue, optionalDescription)

                                selectedValue = -1
                                optionalDescription = ""

                                dialogOpen(false)
                            },
                            enabled = selectedValue == 0 || selectedValue == 1 || selectedValue == 2 || selectedValue == 3 || selectedValue == 4
                        )
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = stringResource(id = R.string.save_button),
                                modifier = Modifier.alpha(0.7f),
                            )
                        }
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp)
                            .height(100.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 15.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val buttonColors = listOf(
                                Color.Green,
                                Color.Cyan,
                                Color.Yellow,
                                Color.Magenta,
                                Color.Red
                            )

                            buttonColors.forEachIndexed { index, color ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(
                                        onClick = {
                                            selectedValue = index
                                        },
                                        modifier = Modifier
                                            .background(color = color, shape = CircleShape)
                                            .alpha(if (selectedValue != index) 1f else 0.1f)
                                    ) {
                                        val icon = when (index) {
                                            0 -> Icons.Filled.SentimentVerySatisfied
                                            1 -> Icons.Filled.SentimentSatisfied
                                            2 -> Icons.Filled.SentimentNeutral
                                            3 -> Icons.Filled.SentimentDissatisfied
                                            4 -> Icons.Filled.SentimentVeryDissatisfied
                                            else -> null
                                        }
                                        icon?.let {
                                            Icon(
                                                modifier = Modifier
                                                    .alpha(0.7f)
                                                    .fillMaxSize(),
                                                imageVector = it,
                                                contentDescription = null,
                                                tint = Color.Black
                                            )
                                        }
                                    }
                                    val buttonLabels = listOf(
                                        R.string.amazing,
                                        R.string.good,
                                        R.string.neutral,
                                        R.string.bad,
                                        R.string.awful
                                    )
                                    Text(text = stringResource(id = buttonLabels[index]))
                                }
                            }
                        }
                    }
                    val maxChar = 25

                    OutlinedTextField(
                        value = optionalDescription,
                        onValueChange = {
                            if (it.length <= maxChar) {
                                optionalDescription = it
                            }
                        },
                        label = { Text(text = stringResource(id = R.string.note)) },
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