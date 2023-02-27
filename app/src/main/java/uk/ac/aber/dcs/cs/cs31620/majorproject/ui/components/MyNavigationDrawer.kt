package uk.ac.aber.dcs.cs.cs31620.majorproject.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Login
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs.cs31620.majorproject.ui.navigation.Screen
import uk.ac.aber.dcs.cs.cs31620.majorproject.ui.theme.MajorProjectTheme
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
    content: @Composable () -> Unit = {}

) {
    val items = listOf(
        Pair(
            Icons.Default.Login,
            "aaa"
        ),
        Pair(
            Icons.Default.Help,
            "bbb"
        ),
        Pair(
            Icons.Default.Feedback,
            "ccc"
        )
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // Not sure if rememberSaveable or remember is what we
            // want since it stays selected when we close and
            // reopen the drawer. However...
            val selectedItem = rememberSaveable { mutableStateOf(0) }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize()
            ) {
                // This probably doesn't add much, although we
                // have quite a bit of screen space, even in landscape
                // Anything larger and can lose the buttons in landscape
                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                imageVector = item.first,
                                contentDescription = item.second
                            )
                        },
                        label = { Text(item.second) },
                        // I'm not sure that having a default selected makes sense
                        selected = index == selectedItem.value,
                        onClick = {
                            // Here we would navigate to appropriate page
                            // We'll just set as selected.
                            selectedItem.value = index

                            // Exercise solution. Just close the drawer
                            // and navigate
                            if (index == 0) {
                                // If we don't do this the drawer will be hidden
                                // when we navigate to the login screen, however,
                                // the back button etc will take us back to the
                                // open drawer, which is not usual behaviour
                                closeDrawer()
//                                navController.navigate(route = Screen.Login.route)
                            }
                        }
                    )
                }
            }
        },
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun MainPageNavigationDrawerPreview() {
    MajorProjectTheme(dynamicColor = false) {
        val navController = rememberNavController()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
        MainPageNavigationDrawer(navController, drawerState)
    }
}