package uk.ac.aber.dcs.cs39440.myvitalife.ui.journal

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.model.DesiredDate
import uk.ac.aber.dcs.cs39440.myvitalife.model.Mood
import uk.ac.aber.dcs.cs39440.myvitalife.model.ThemeSettings
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun JournalScreen(
    navController: NavHostController,
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
    val appBarTitle = stringResource(R.string.journal)
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    val tab0Button by remember { mutableStateOf(Icons.Filled.AddReaction) }
    val tab1Button by remember { mutableStateOf(Icons.Filled.CheckBox) }

    val listOfGoals by firebaseViewModel.goalData.observeAsState(emptyList())
    val listOfMoods by firebaseViewModel.moodsList.observeAsState(emptyList())

    var itemToDelete by remember { mutableStateOf("") }

    var isGoalDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isMoodDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isConfirmationDialogOpen by rememberSaveable { mutableStateOf(false) }

    TopLevelScaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (selectedTabIndex == 0) {
                        isMoodDialogOpen = true
                    } else {
                        isGoalDialogOpen = true
                    }

                }
            ) {
                if (selectedTabIndex == 0) {
                    Icon(
                        imageVector = tab0Button,
                        contentDescription = stringResource(id = R.string.add)
                    )
                } else {
                    Icon(
                        imageVector = tab1Button,
                        contentDescription = stringResource(id = R.string.modify)
                    )
                }
            }
        },
        navController = navController,
        appBarTitle = appBarTitle,

        ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TabRow(
                    selectedTabIndex = selectedTabIndex
                ) {
                    Tab(
                        text = {
                            Text(stringResource(id = R.string.mood))
                        },
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 }
                    )
                    Tab(
                        text = {
                            Text(stringResource(id = R.string.goals))
                        },
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 }
                    )
                }
                when (selectedTabIndex) {
                    0 -> {
                        if (listOfMoods.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 12.dp),
                                contentPadding = PaddingValues(bottom = 16.dp)
                            )
                            {
                                items(listOfMoods) { entry ->
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.Start
                                        ) {
                                            MoodCard(
                                                mood = entry,
                                                openConfirmationDialog = { isOpen ->
                                                    isConfirmationDialogOpen = isOpen
                                                    itemToDelete = entry.time
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            EmptyJournal(0)
                        }
                    }
                    1 -> {
                        if (listOfGoals.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 12.dp),
                                contentPadding = PaddingValues(bottom = 16.dp)
                            )
                            {
                                items(listOfGoals) { entry ->
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.Start
                                        ) {
                                            GoalCard(
                                                title = entry.title,
                                                openConfirmationDialog = { isOpen ->
                                                    isConfirmationDialogOpen = isOpen
                                                    itemToDelete = entry.title
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            EmptyJournal(1)
                        }
                    }
                }
            }
        }
    }
    AddGoalDialog(
        dialogIsOpen = isGoalDialogOpen,
        dialogOpen = { isOpen ->
            isGoalDialogOpen = isOpen
        }
    )
    AddMoodDialog(
        dialogIsOpen = isMoodDialogOpen,
        dialogOpen = { isOpen ->
            isMoodDialogOpen = isOpen
        }
    )
    DeleteConfirmationDialog(
        dialogIsOpen = isConfirmationDialogOpen,
        dialogOpen = { isOpen ->
            isConfirmationDialogOpen = isOpen
        },
        item = itemToDelete,
        tabIndex = selectedTabIndex
    )
}

@Composable
private fun MoodEmoji(
    time: String,
    backgroundColor: Color,
    icon: ImageVector,
    contentDescription: String
) {
    Icon(
        modifier = Modifier
            .background(color = backgroundColor, shape = CircleShape)
            .size(30.dp),
        imageVector = icon,
        contentDescription = contentDescription,
        tint = Color.Black
    )
    Text(
        text = time,
        fontSize = 20.sp,
        modifier = Modifier.padding(bottom = 9.dp)
    )
}

@Composable
private fun MoodCard(
    mood: Mood,
    openConfirmationDialog: (Boolean) -> Unit = {}
) {
    // Default values
    var emojiBackgroundColor: Color = Color.Yellow
    var emojiIcon: ImageVector = Icons.Filled.SentimentNeutral
    var emojiDescription: String = stringResource(id = R.string.neutral)

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 10.dp, end = 5.dp, top = 4.dp, bottom = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                when (mood.type) {
                    0 -> {
                        emojiBackgroundColor = Color.Green
                        emojiIcon = Icons.Filled.SentimentVerySatisfied
                        emojiDescription = stringResource(id = R.string.amazing_lower_case)
                    }
                    1 -> {
                        emojiBackgroundColor = Color.Cyan
                        emojiIcon = Icons.Filled.SentimentSatisfied
                        emojiDescription = stringResource(id = R.string.good_lower_case)
                    }
                    2 -> {
                        emojiBackgroundColor = Color.Yellow
                        emojiIcon = Icons.Filled.SentimentNeutral
                        emojiDescription = stringResource(id = R.string.neutral_lower_case)
                    }
                    3 -> {
                        emojiBackgroundColor = Color.Magenta
                        emojiIcon = Icons.Filled.SentimentDissatisfied
                        emojiDescription = stringResource(id = R.string.bad_lower_case)
                    }
                    4 -> {
                        emojiBackgroundColor = Color.Red
                        emojiIcon = Icons.Filled.SentimentVeryDissatisfied
                        emojiDescription = stringResource(id = R.string.awful_lower_case)
                    }
                }
                MoodEmoji(
                    time = mood.time,
                    backgroundColor = emojiBackgroundColor,
                    icon = emojiIcon,
                    contentDescription = emojiDescription
                )
                IconButton(
                    onClick = {
                        openConfirmationDialog(true)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(id = R.string.delete_button),
                        modifier = Modifier.padding(bottom = 7.dp)
                    )
                }
            }
            var noteText = ""
            noteText = mood.description.ifEmpty {
                stringResource(id = R.string.i_felt, emojiDescription)
            }
            Text(
                text = noteText,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 3.dp, bottom = 5.dp, top = 2.dp)
            )
        }
    }
}

@Composable
private fun GoalCard(
    title: String,
    firebaseViewModel: FirebaseViewModel = viewModel(),
    openConfirmationDialog: (Boolean) -> Unit = {}
) {
    val isChecked = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = title) {
        val goalIsDone = withContext(Dispatchers.IO) {
            firebaseViewModel.getGoalIsDone(title)
        }
        isChecked.value = goalIsDone
    }

    val cardGreen =
        if (isChecked.value) CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
        else CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.error)
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 10.dp),
        colors = cardGreen
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(end = 5.dp, top = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 6.dp, bottom = 7.dp),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Checkbox(
                    checked = isChecked.value,
                    onCheckedChange = {
                        isChecked.value = !isChecked.value
                        firebaseViewModel.addGoal(title, isChecked.value)
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.Green,
                        uncheckedColor = Color.Red
                    )
                )
                IconButton(
                    onClick = {
                        openConfirmationDialog(true)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(id = R.string.delete_button),
                        modifier = Modifier.padding(bottom = 7.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyJournal(tabIndex: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        if (tabIndex == 0) {
            Icon(
                modifier = Modifier
                    .size(100.dp)
                    .alpha(0.3f),
                imageVector = Icons.Default.FaceRetouchingOff,
                contentDescription = stringResource(id = R.string.empty_mood_tab)
            )
            Text(
                modifier = Modifier
                    .alpha(0.3f),
                text = stringResource(id = R.string.empty_mood_tab),
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        } else {
            Icon(
                modifier = Modifier
                    .size(100.dp)
                    .alpha(0.3f),
                imageVector = Icons.Default.Unpublished,
                contentDescription = stringResource(id = R.string.empty_goal_tab)
            )
            Text(
                modifier = Modifier
                    .alpha(0.3f),
                text = stringResource(id = R.string.empty_goal_tab),
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }

    }
}

@Composable
fun DeleteConfirmationDialog(
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
    firebaseViewModel: FirebaseViewModel = viewModel(),
    item: String,
    tabIndex: Int
) {
    if (dialogIsOpen) {
        AlertDialog(
            onDismissRequest = { dialogOpen(false) },
            title = {
                Text(
                    text = stringResource(id = R.string.click_to_confirm),
                    fontSize = 20.sp
                )
            },
            text = {

                Text(
                    text = stringResource(id = R.string.confirmation_text, item),
                    fontSize = 15.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        dialogOpen(false)
                        if (tabIndex == 0) {
                            firebaseViewModel.deleteMood(item)
                        } else {
                            firebaseViewModel.deleteGoal(item)
                        }
                    }
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

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview
@Composable
fun JournalScreenPreview() {
    val navController = rememberNavController()
    MyVitaLifeTheme(dynamicColor = false) {
        JournalScreen(navController)
    }
}