package uk.ac.aber.dcs.cs39440.myvitalife.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.DoneOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs39440.myvitalife.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithSave(
    navController: NavHostController,
    title: Int,
    onClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(stringResource(id = title))
        },
        navigationIcon = {
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription =
                    stringResource(R.string.go_back)
                )
            }
        },
        actions = {
            IconButton(
                onClick = { onClick() }
            )
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription =
                    stringResource(R.string.save_button)
                )
            }
        }
    )
}