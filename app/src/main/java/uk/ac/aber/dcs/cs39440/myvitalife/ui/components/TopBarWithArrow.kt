package uk.ac.aber.dcs.cs39440.myvitalife.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.ui.navigation.Screen
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme

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