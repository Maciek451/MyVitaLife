package uk.ac.aber.dcs.cs39440.myvitalife.ui.journal

import android.util.Log
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
//                        navController.navigate(Screen.AddMood.route)
                        isMoodDialogOpen = true
                    } else {
                        isGoalDialogOpen = true
                    }

                }
            ) {
                if (selectedTabIndex == 0) {
                    Icon(
                        imageVector = tab0Button,
                        contentDescription = "Add"
                    )
                } else {
                    Icon(
                        imageVector = tab1Button,
                        contentDescription = "Modify"
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
                        text = { Text("Mood") },
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 }
                    )
                    Tab(
                        text = { Text("Goals") },
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
                                        Log.d("TEST", "creating mood card: ${entry.time}")
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
    var emojiDescription: String = "Neutral"

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 10.dp, end = 5.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                when (mood.type) {
                    0 -> {
                        emojiBackgroundColor = Color.Green
                        emojiIcon = Icons.Filled.SentimentVerySatisfied
                        emojiDescription = "amazing"
                    }
                    1 -> {
                        emojiBackgroundColor = Color.Cyan
                        emojiIcon = Icons.Filled.SentimentSatisfied
                        emojiDescription = "good"
                    }
                    2 -> {
                        emojiBackgroundColor = Color.Yellow
                        emojiIcon = Icons.Filled.SentimentNeutral
                        emojiDescription = "neutral"
                    }
                    3 -> {
                        emojiBackgroundColor = Color.Magenta
                        emojiIcon = Icons.Filled.SentimentDissatisfied
                        emojiDescription = "bad"
                    }
                    4 -> {
                        emojiBackgroundColor = Color.Red
                        emojiIcon = Icons.Filled.SentimentVeryDissatisfied
                        emojiDescription = "awful"
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
                        contentDescription = "Delete food"
                    )
                }
            }
            var noteText = ""
            noteText = mood.description.ifEmpty {
                "I felt $emojiDescription"
            }
            Text(
                text = noteText,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 10.dp)
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
        if (isChecked.value) CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        else CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 10.dp),
        colors = cardGreen
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 5.dp, end = 5.dp)
        ) {
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
                )
                IconButton(
                    onClick = {
                        openConfirmationDialog(true)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete food"
                    )
                }
            }
            Text(
                text = title,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(bottom = 5.dp)
            )
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
                contentDescription = "EmptySleepScreen"
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
                contentDescription = "EmptySleepScreen"
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
            onDismissRequest = { /* Empty so clicking outside has no effect */ },
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

@Preview
@Composable
fun JournalScreenPreview() {
    val navController = rememberNavController()
    MyVitaLifeTheme(dynamicColor = false) {
        JournalScreen(navController)
    }
}