package uk.ac.aber.dcs.cs39440.myvitalife.ui.quotes

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.HeartBroken
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs39440.myvitalife.ui.components.TopLevelScaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs39440.myvitalife.R
import uk.ac.aber.dcs.cs39440.myvitalife.model.*
import uk.ac.aber.dcs.cs39440.myvitalife.model.quotes.GenerateQuoteIfEmpty
import uk.ac.aber.dcs.cs39440.myvitalife.model.quotes.QuoteOfTheDay
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel
import uk.ac.aber.dcs.cs39440.myvitalife.ui.theme.MyVitaLifeTheme

@Composable
fun QuotesScreen(
    navController: NavHostController,
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
    val appBarTitle = stringResource(R.string.quotes)

    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }
    val favouriteQuotes by firebaseViewModel.favouriteQuotes.observeAsState(emptyList())

    var isDialogOpen by rememberSaveable { mutableStateOf(false) }

    GenerateQuoteIfEmpty()

    TopLevelScaffold(
        floatingActionButton = {},
        navController = navController,
        appBarTitle = appBarTitle,
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
                        text = { Text("Favourite quotes") },
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
                                text = "\"${QuoteOfTheDay.quote.text}\"",
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp,
                                fontFamily = FontFamily.Cursive,
                                lineHeight = 50.sp,
                                modifier = Modifier.padding(bottom = 20.dp),
                            )
                            Text(
                                text = "- ${QuoteOfTheDay.quote.author}",
                                fontSize = 20.sp
                            )
                            IconButton(
                                onClick = {
                                    firebaseViewModel.changeQuotesFavouriteStatus(DesiredDate.date)
                                },
                                modifier = Modifier.padding(top = 40.dp)
                            ) {
                                val imageIndex =
                                    if (QuoteOfTheDay.quote.isFavourite) R.drawable.filled_favorite else R.drawable.outlined_favorite
                                val tintColor =
                                    if (ThemeSettings.isDarkTheme) Color.White else Color.Black
                                Image(
                                    painter = painterResource(id = imageIndex),
                                    contentDescription = "Favourite icon",
                                    modifier = Modifier.size(100.dp),
                                    colorFilter = ColorFilter.tint(tintColor)
                                )
                            }
                        }
                    }
                    1 -> {
                        if (favouriteQuotes.isEmpty()) {
                            EmptyScreen()
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                            )
                            {
                                items(favouriteQuotes) { entry ->
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "\"${entry.text}\"",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 30.sp,
                                            fontFamily = FontFamily.Cursive,
                                            lineHeight = 30.sp,
                                            modifier = Modifier.padding(bottom = 10.dp, top = 10.dp),
                                        )
                                        Text(
                                            text = "- ${entry.author}",
                                            fontSize = 20.sp
                                        )
                                        IconButton(
                                            onClick = {
                                                firebaseViewModel.changeQuotesFavouriteStatus(entry.date)
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.HeartBroken,
                                                contentDescription = "Remove",
                                                modifier = Modifier.padding(bottom = 10.dp)
                                            )
                                        }
                                    }
                                    Spacer(
                                        modifier = Modifier
                                            .height(5.dp)
                                            .fillMaxWidth()
                                    )
                                }
                            }
                        }

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
            imageVector = Icons.Default.Favorite,
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
