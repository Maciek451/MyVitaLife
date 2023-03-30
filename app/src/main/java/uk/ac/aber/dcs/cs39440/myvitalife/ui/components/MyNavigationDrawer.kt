package uk.ac.aber.dcs.cs39440.myvitalife.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import uk.ac.aber.dcs.cs39440.myvitalife.R
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
    content: @Composable () -> Unit = {}

) {
    val items = listOf(
        Pair(
            Icons.Default.Web,
            "Useful links"
        ),
        Pair(
            Icons.Default.Settings,
            "Settings"
        )
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            val selectedItem = rememberSaveable { mutableStateOf(0) }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize()
            ) {
                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                imageVector = item.first,
                                contentDescription = item.second
                            )
                        },
                        label = { Text(item.second) },
                        selected = index == selectedItem.value,
                        onClick = {
                            selectedItem.value = index
                            if (index == 0) {
//                                navController.navigate(route = Screen.Login.route)
//                                closeDrawer()
                            } else {

                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.padding(40.dp))
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    painter = painterResource(id = R.drawable.drawerimage),
                    contentDescription = stringResource(R.string.drawer_image),
                    contentScale = ContentScale.Crop
                )
            }
        },
        content = content
    )
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