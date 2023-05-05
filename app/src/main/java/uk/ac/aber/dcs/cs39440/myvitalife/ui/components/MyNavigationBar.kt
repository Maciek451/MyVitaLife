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
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screens
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.screens
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme

/**
 * Displays a navigation bar for the Home screens.
 *
 * @param navController NavController manages app navigation
 */
@Composable
fun NavigationBar(
    navController: NavController
) {
    val icons = mapOf(
        Screens.Quote to IconGroup(
            filledIcon = Icons.Filled.FormatQuote,
            outlineIcon = Icons.Outlined.FormatQuote,
            label = stringResource(id = R.string.quotes)
        ),
        Screens.Nutrition to IconGroup(
            filledIcon = Icons.Filled.Restaurant,
            outlineIcon = Icons.Outlined.Restaurant,
            label = stringResource(id = R.string.nutrition)
        ),
        Screens.Journal to IconGroup(
            filledIcon = Icons.Filled.NoteAdd,
            outlineIcon = Icons.Outlined.NoteAdd,
            label = stringResource(id = R.string.journal)
        ),
        Screens.Insights to IconGroup(
            filledIcon = Icons.Filled.Insights,
            outlineIcon = Icons.Outlined.Insights,
            label = stringResource(id = R.string.insights)
        ),
        Screens.Sleep to IconGroup(
            filledIcon = Icons.Filled.Hotel,
            outlineIcon = Icons.Outlined.Hotel,
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
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
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