package uk.ac.aber.dcs.cs39440.myvitalife.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreenTopBar(
    navController: NavHostController
) {
    CenterAlignedTopAppBar(
        title = {
            Text(stringResource(id = R.string.summary))
        },
        navigationIcon = {
            IconButton(
                onClick = { navController.navigate(Screen.Insights.route) }
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
    val navController = rememberNavController()
    MyVitaLifeTheme(dynamicColor = false) {
        SummaryScreenTopBar(navController)
    }
}