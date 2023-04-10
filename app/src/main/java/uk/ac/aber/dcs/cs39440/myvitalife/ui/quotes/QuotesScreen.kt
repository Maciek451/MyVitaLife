package uk.ac.aber.dcs.cs39440.myvitalife.ui.quotes

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs39440.myvitalife.ui.components.TopLevelScaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.model.*
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme

val retrofit = Retrofit.Builder()
    .baseUrl("https://api.forismatic.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val service = retrofit.create(QuoteService::class.java)

@Composable
fun QuotesScreen(
    navController: NavHostController,
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
    val appBarTitle = stringResource(R.string.quotes)

    val tab0Button by remember { mutableStateOf(Icons.Filled.AddReaction) }
    val tab1Button by remember { mutableStateOf("") }

    val quoteData by firebaseViewModel.quoteData.observeAsState(Quote("", ""))

    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    var isDialogOpen by rememberSaveable { mutableStateOf(false) }

    TopLevelScaffold(
        floatingActionButton = {},
        navController = navController,
        appBarTitle = appBarTitle,
        givenDate = DesiredDate.date
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 8.dp, end = 8.dp),
            ) {
                TabRow(
                    selectedTabIndex = selectedTabIndex
                ) {
                    Tab(
                        text = { Text("Quote of the day") },
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 }
                    )
                    Tab(
                        text = { Text("Saved quotes") },
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 }
                    )
                }
                when (selectedTabIndex) {
                    0 -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 8.dp, end = 8.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "\"${quoteData.text}\"",
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp,
                                fontFamily = FontFamily.Cursive,
                                lineHeight = 50.sp,
                                modifier = Modifier.padding(bottom = 20.dp),
                            )
                            Text(
                                text = "- " + quoteData.author,
                                fontSize = 20.sp
                            )
                            Button(
                                onClick = {

                                },
                                modifier = Modifier.padding(top = 40.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.save)
                                )
                            }
                        }
                    }
                    1 -> {
                        EmptyScreen()
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier
                .size(100.dp)
                .alpha(0.3f),
            imageVector = Icons.Default.BookmarkRemove,
            contentDescription = "Quotes"
        )
        Text(
            modifier = Modifier
                .alpha(0.3f),
            text = stringResource(id = R.string.no_quotes),
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun DailyStepsPreview() {
    val navController = rememberNavController()
    MyVitaLifeTheme(dynamicColor = false) {
        QuotesScreen(navController)
    }
}
