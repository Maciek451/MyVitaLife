package uk.ac.aber.dcs.cs.cs31620.majorproject.ui.daily_steps

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs.cs31620.majorproject.ui.components.TopLevelScaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs.cs31620.majorproject.R
import uk.ac.aber.dcs.cs.cs31620.majorproject.ui.theme.MajorProjectTheme

@Composable
fun DailyStepsScreen(
    navController: NavHostController
) {
    TopLevelScaffold(
        navController
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(id = R.drawable.dailystepsimage),
                    contentDescription = stringResource(R.string.daily_steps_image),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Preview
@Composable
fun DailyStepsPreview() {
    val navController = rememberNavController()
    MajorProjectTheme(dynamicColor = false) {
        DailyStepsScreen(navController)
    }
}
