package uk.ac.aber.dcs.cs39440.myvitalife.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.screens
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme

@Composable
fun NavigationBar(
    navController: NavController
) {
    val icons = mapOf(
        Screen.DailySteps to IconGroup(
            filledIcon = Icons.Filled.DirectionsWalk,
            outlineIcon = Icons.Outlined.DirectionsWalk,
            label = stringResource(id = R.string.daily_steps)
        ),
        Screen.Nutrition to IconGroup(
            filledIcon = Icons.Filled.Restaurant,
            outlineIcon = Icons.Outlined.Restaurant,
            label = stringResource(id = R.string.nutrition)
        ),
        Screen.Journal to IconGroup(
            filledIcon = Icons.Filled.NoteAdd,
            outlineIcon = Icons.Outlined.NoteAdd,
            label = stringResource(id = R.string.journal)
        ),
        Screen.Insights to IconGroup(
            filledIcon = Icons.Filled.Insights,
            outlineIcon = Icons.Outlined.Insights,
            label = stringResource(id = R.string.insights)
        ),
        Screen.Sleep to IconGroup(
            filledIcon = Icons.Filled.Bed,
            outlineIcon = Icons.Outlined.Bed,
            label = stringResource(id = R.string.sleep)
        ),
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        screens.forEach { screen ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            val labelText = icons[screen]!!.label
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = (if (isSelected)
                            icons[screen]!!.filledIcon
                        else
                            icons[screen]!!.outlineIcon),
                        contentDescription = labelText
                    )
                },
                label = { Text(labelText) },
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun NavigationBarPreview() {
    val navController = rememberNavController()
    MyVitaLifeTheme(dynamicColor = false) {
        NavigationBar(navController)
    }
}