package uk.ac.aber.dcs.cs39440.myvitalife.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme

/**
 * Displays a top app bar for the chosen screens.
 *
 * @param navController NavController manages app navigation
 * @param title The title of the screen.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithArrow(
    navController: NavHostController,
    title: Int,
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
    )
}

@Preview
@Composable
fun SummaryScreenTopBarPreview() {
    val title = 0
    val click: Unit
    MyVitaLifeTheme(dynamicColor = false) {
//        TopAppBarWithArrow(title, click)
    }
}