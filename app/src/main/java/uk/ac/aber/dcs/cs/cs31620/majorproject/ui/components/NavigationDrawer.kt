package uk.ac.aber.dcs.cs.cs31620.majorproject.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs.cs31620.majorproject.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(
    navController: NavHostController,
    drawerState: DrawerState,
    closeDrawer: () -> Unit = {},
    content: @Composable () -> Unit = {}

) {
    var isDialogOpen by rememberSaveable { mutableStateOf(false) }
    val items = listOf(
        Pair(
            Icons.Default.CollectionsBookmark,
            stringResource(R.string.change_languages_button)
        ),
        Pair(
            Icons.Default.DeleteForever,
            stringResource(R.string.clear_dict_button)
        )
    )
}