package uk.ac.aber.dcs.cs.cs31620.majorproject.ui.provide_name

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs.cs31620.majorproject.R
import uk.ac.aber.dcs.cs.cs31620.majorproject.ui.navigation.Screen
import uk.ac.aber.dcs.cs.cs31620.majorproject.ui.theme.MajorProjectTheme

@Composable
fun ProvideNameScreen(
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            modifier = Modifier
                .size(300.dp)
                .padding(bottom = 16.dp, top = 16.dp),
            painter = painterResource(id = R.drawable.providenameimage),
            contentDescription = stringResource(R.string.provide_name_image),
            contentScale = ContentScale.Crop
        )

        Button(
            onClick = { navController.navigate(Screen.Nutrition.route) },
            modifier = Modifier.padding(top = 10.dp)
        ) {

        }
    }
}

@Composable
@Preview
private fun ProvideNamePreview() {
    val navController = rememberNavController()
    MajorProjectTheme(dynamicColor = false) {
        ProvideNameScreen(navController)
    }
}