package uk.ac.aber.dcs.cs39440.myvitalife.ui.components

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.datastore.IS_DARK_THEME_ON_KEY
import uk.ac.aber.dcs.cs39440.myvitalife.model.DataViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.model.DesiredDate
import uk.ac.aber.dcs.cs39440.myvitalife.model.ThemeSettings
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screens
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme

/**
 * Displays a navigation drawer for the Home screens.
 *
 * @param drawerState: object that represents the current state of the navigation drawer.
 * @param closeDrawer: A callback function to close the navigation drawer.
 * @param content: used to render the content of the navigation drawer.
 * @param navController NavController manages app navigation
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MainPageNavigationDrawer(
    navController: NavHostController,
    drawerState: DrawerState,
    closeDrawer: () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    var isExportDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteDataDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isThemeDialogOpen by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    val items = listOf(
        Pair(
            Icons.Default.ImportExport,
            stringResource(id = R.string.export_data_drawer)
        ),
        Pair(
            Icons.Default.Analytics,
            stringResource(id = R.string.data_summary)
        ),
        Pair(
            Icons.Default.BrightnessMedium,
            stringResource(id = R.string.choose_theme_drawer)
        ),
        Pair(
            Icons.Default.DeleteForever,
            stringResource(id = R.string.clear_data)
        ),
        Pair(
            Icons.Default.NotificationsActive,
            stringResource(id = R.string.notification_permission)
        ),
        Pair(
            Icons.Default.Info,
            stringResource(id = R.string.about)
        )
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
                    modifier = Modifier.padding(bottom = 20.dp, top = 10.dp, start = 20.dp),
                    fontSize = 25.sp
                )
                Divider(
                    thickness = 1.dp,
                    modifier = Modifier
                        .padding(bottom = 7.dp, start = 20.dp, end = 20.dp)
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
                                    isExportDialogOpen = true
                                    closeDrawer()
                                }
                                1 -> {
                                    navController.navigate(Screens.Stats.route)
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
                                4 -> {
                                    if (permissionState.hasPermission) {
                                        Toast.makeText(context, R.string.permission_already_granted, Toast.LENGTH_LONG).show()
                                    } else {
                                        permissionState.launchPermissionRequest()
                                    }
                                    closeDrawer()
                                }
                                5 -> {
                                    navController.navigate(route = Screens.Info.route)
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
        }
    )
    ChooseThemeDialog(
        dialogIsOpen = isThemeDialogOpen,
        dialogOpen = { isOpen ->
            isThemeDialogOpen = isOpen
        }
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
                    text = stringResource(id = R.string.export_data, DesiredDate.date),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAllDataConfirmationDialog(
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
    val context = LocalContext.current
    val prompt = stringResource(id = R.string.data_removed)
    var confirm by remember { mutableStateOf("") }

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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.warning2),
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
                        firebaseViewModel.deleteAllUserData()
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

@Composable
fun ChooseThemeDialog(
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
    dataViewModel: DataViewModel = hiltViewModel()
) {
    val options = listOf(R.string.light, R.string.dark)
    var selectedOptionIndex by rememberSaveable { mutableStateOf(2) }

    selectedOptionIndex = if (ThemeSettings.isDarkTheme) 1 else 0

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
                                .fillMaxWidth()
                                .selectable(
                                    selected = selectedOptionIndex == index,
                                    onClick = { selectedOptionIndex = index }
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedOptionIndex == index,
                                onClick = { selectedOptionIndex = index }
                            )
                            Text(
                                text = stringResource(id = options[index]),
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
                                ThemeSettings.isDarkTheme = false
                                dialogOpen(false)
                            }
                            1 -> {
                                ThemeSettings.isDarkTheme = true
                                dialogOpen(false)
                            }
                        }
                        dataViewModel.saveBoolean(ThemeSettings.isDarkTheme, IS_DARK_THEME_ON_KEY)
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
//        MainPageNavigationDrawer(navController, drawerState)
    }
}