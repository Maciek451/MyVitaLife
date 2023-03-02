package uk.ac.aber.dcs.cs39440.myvitalife.ui.provide_name

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.datastore.DataStore
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme

@Composable
fun ProvideNameScreen(
    navController: NavHostController
) {
    var usersName by remember {
        mutableStateOf("")
    }
    var isDialogOpen by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val dataStore = DataStore(context)
    val firstTimeVal = dataStore.getString("IS_FIRST_TIME").collectAsState(initial = "")
    val firstTime = firstTimeVal.value?.isEmpty() == true || firstTimeVal.value == null
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(300.dp)
                .padding(bottom = 16.dp, top = 16.dp),
            painter = painterResource(id = R.drawable.providenameimage),
            contentDescription = stringResource(R.string.provide_name_image),
            contentScale = ContentScale.Crop
        )

        OutlinedTextField(
            value = usersName,
            label = {
                Text(text = stringResource(id = R.string.users_name))
            },
            onValueChange = { usersName = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
        )

        Button(
            onClick = {
//                if (firstTime) {
//                    scope.launch {
//                        dataStore.saveString("NOT_FIRST_TIME", "IS_FIRST_TIME")
//                        dataStore.saveString(name = usersName, key = "USERS_NAME")
//                    }
//                }
//                else {
//                    isDialogOpen = true
//                }
                navController.navigate(Screen.Journal.route)
            },
            modifier = Modifier
                .padding(top = 10.dp),
            enabled = usersName.isNotEmpty()
        ) {
            Text(text = stringResource(id = R.string.lets_go_button))
        }

        Button(
            onClick = {
                navController.navigate(Screen.Journal.route)
            },
            enabled = !firstTime,
            modifier = Modifier
                .padding(top = 8.dp)
        ) {
            Text(text = stringResource(id = R.string.cancel_button))
        }
    }
}

@Composable
fun ConfirmDialog(
    dialogIsOpen: Boolean,
    dialogOpen: (Boolean) -> Unit = {},
    navController: NavHostController,
    usersName: String,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = DataStore(context)
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
                    text = stringResource(id = R.string.confirmation_text),
                    fontSize = 15.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        dialogOpen(false)
                        scope.launch {
                            dataStore.saveString(name = usersName, key = "USERS_NAME")
                        }
                        navController.navigate(Screen.Journal.route)
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
@Preview
private fun ProvideNamePreview() {
    val navController = rememberNavController()
    MyVitaLifeTheme(dynamicColor = false) {
        ProvideNameScreen(navController)
    }
}
