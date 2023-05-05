package uk.ac.aber.dcs.cs39440.myvitalife.ui.why_to_track

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.model.DataViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.components.TopAppBarWithArrow

/**
 * Displays app info screen
 *
 * @param navController NavController manages app navigation
 */
@Composable
fun InfoScreen(
    navController: NavHostController,
) {
    val title = R.string.about

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopAppBarWithArrow(navController, title)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, start = 10.dp, end = 10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,

            ) {
                Image(
                    modifier = Modifier.size(70.dp),
                    painter = painterResource(id = R.drawable.lifestyle),
                    contentDescription = stringResource(id = R.string.app_icon),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontSize = 35.sp
                )
            }
            Text(
                text = stringResource(id = R.string.app_description),
                modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
            )
            Divider(thickness = 1.dp)
            Text(
                text = stringResource(id = R.string.app_version),
                modifier = Modifier.padding(top = 10.dp, bottom = 5.dp)
            )
            Text(
                text = stringResource(id = R.string.version_of_the_app),
                modifier = Modifier.padding(bottom = 10.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Divider(thickness = 1.dp)
            Text(
                text = stringResource(id = R.string.creator),
                modifier = Modifier.padding(top = 10.dp, bottom = 5.dp)
            )
            Text(
                text = stringResource(id = R.string.creator_name),
                modifier = Modifier.padding(bottom = 20.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

        }
    }
}