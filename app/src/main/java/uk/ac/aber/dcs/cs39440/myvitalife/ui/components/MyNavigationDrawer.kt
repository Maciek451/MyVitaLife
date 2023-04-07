package uk.ac.aber.dcs.cs39440.myvitalife.ui.components

import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme

/**
 * Creates the navigation drawer. Uses Material 3 ModalNavigationDrawer.
 * Current implementation has an image at the top and then three items.
 * @param navController To pass through the NavHostController since navigation is required
 * @param drawerState The state of the drawer, i.e. whether open or closed
 * @param closeDrawer To pass in the close navigation drawer behaviour as a lambda.
 * By default has an empty lambda.
 * @param content To pass in the page content for the page when the navigation drawer is closed
 * @author Chris Loftus
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPageNavigationDrawer(
    navController: NavHostController,
    drawerState: DrawerState,
    closeDrawer: () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    var isExportDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteDataDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isThemeDialogOpen by rememberSaveable { mutableStateOf(false) }

    val items = listOf(
        Pair(
            Icons.Default.QuestionMark,
            "Why you should track your lifestyle?"
        ),
        Pair(
            Icons.Default.ImportExport,
            "Export data to txt file"
        ),
        Pair(
            Icons.Default.BrightnessMedium,
            "Choose theme"
        ),
        Pair(
            Icons.Default.DeleteForever,
            "Clear all data"
        ),
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .padding(all = 10.dp)
                    .fillMaxSize(),
            ) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    modifier = Modifier.padding(bottom = 25.dp, top = 10.dp, start = 15.dp),
                    fontSize = 25.sp
                )
                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                imageVector = item.first,
                                contentDescription = item.second
                            )
                        },
                        label = { Text(item.second) },
                        selected = false,
                        onClick = {
                            when (index) {
                                0 -> {
                        //                                navController.navigate(route = Screen.Login.route)
                        //                                closeDrawer()
                                }
                                1 -> {
                                    isExportDialogOpen = true
                                    closeDrawer()
                                }
                                2 -> {
                                    isThemeDialogOpen = true
                                    closeDrawer()
                                }
                                3 -> {
                                    isDeleteDataDialogOpen = true
                                    closeDrawer()
                                }
                            }
                        }
                    )
                }
            }
        },
        content = content
    )
    ExportConfirmationDialog(
        dialogIsOpen = isExportDialogOpen,
        dialogOpen = { isOpen ->
            isExportDialogOpen = isOpen
        }
    )
    DeleteAllDataConfirmationDialog(
        dialogIsOpen = isDeleteDataDialogOpen,
        dialogOpen = { isOpen ->
            isDeleteDataDialogOpen = isOpen
        },
        navController = navController
    )
    ChooseThemeDialog(
        dialogIsOpen = isThemeDialogOpen,
        dialogOpen = { isOpen ->
            isThemeDialogOpen = isOpen
        },
        navController = navController
    )
}

@Composable
fun ExportConfirmationDialog(
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
    firebaseViewModel: FirebaseViewModel = viewModel(),
) {

    val context = LocalContext.current
    val prompt = stringResource(id = R.string.data_exported)

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
                    text = stringResource(id = R.string.export_data),
                    fontSize = 15.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        dialogOpen(false)
                        firebaseViewModel.exportData()
                        Toast.makeText(context, prompt, Toast.LENGTH_LONG).show()
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

@Composable
fun DeleteAllDataConfirmationDialog(
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
    firebaseViewModel: FirebaseViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val prompt = stringResource(id = R.string.data_removed)

    if (dialogIsOpen) {
        AlertDialog(
            onDismissRequest = { dialogOpen(false) },
            title = {
                Text(
                    text = stringResource(id = R.string.warning),
                    fontSize = 20.sp
                )
            },
            text = {

                Text(
                    text = stringResource(id = R.string.warning2),
                    fontSize = 15.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        dialogOpen(false)
                        navController.navigate(Screen.Account.route)
                        firebaseViewModel.deleteAllUserData()
                        Toast.makeText(context, prompt, Toast.LENGTH_LONG).show()
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

@Composable
fun ChooseThemeDialog(
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
    navController: NavController,
) {
    val options = listOf("Light", "Dark", "System Default")
    var selectedOptionIndex by rememberSaveable { mutableStateOf(0) }

    if (dialogIsOpen) {
        AlertDialog(
            onDismissRequest = { dialogOpen(false) },
            title = {
                Text(
                    text = stringResource(id = R.string.choose_theme),
                    fontSize = 25.sp
                )
            },
            text = {
                LazyColumn {
                    items(options.size) { index ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedOptionIndex == index,
                                onClick = { selectedOptionIndex = index }
                            )
                            Text(
                                text = options[index],
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        when (selectedOptionIndex) {
                            0 -> {
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                                dialogOpen(false)
                            }
                            1 -> {
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                                dialogOpen(false)
                            }
                            2 -> {
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                                dialogOpen(false)
                            }
                        }
                    }
                ) {
                    Text(stringResource(R.string.ok))
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun MainPageNavigationDrawerPreview() {
    MyVitaLifeTheme(dynamicColor = false) {
        val navController = rememberNavController()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
        MainPageNavigationDrawer(navController, drawerState)
    }
}