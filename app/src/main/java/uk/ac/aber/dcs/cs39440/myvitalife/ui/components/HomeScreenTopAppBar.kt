package uk.ac.aber.dcs.cs39440.myvitalife.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.account.AccountDialog

/**
 * Displays a top app bar for the Home screens.
 *
 * @param navController NavController manages app navigation
 * @param onClick The click listener for the navigation icon
 * @param title The title of the screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTopBar(
    navController: NavHostController,
    onClick: () -> Unit = {},
    title: String
) {
    var isDialogOpen by rememberSaveable { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = {
            Text(title)
        },
        navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription =
                    stringResource(R.string.nav_drawer_menu)
                )
            }
        },
        actions = {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxHeight()
            ) {
                IconButton(
                    onClick = {
                              isDialogOpen = true
                    },
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = stringResource(R.string.account_icon)
                    )
                }
            }
        }
    )
    AccountDialog(
        dialogIsOpen = isDialogOpen,
        dialogOpen = { isOpen ->
            isDialogOpen = isOpen
        },
        navController = navController
    )
}

@Preview
@Composable
fun HomeScreenTopBarPreview() {
    val navController = rememberNavController()
//    HomeScreenTopBar(navController)
}