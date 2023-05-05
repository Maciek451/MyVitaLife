package uk.ac.aber.dcs.cs39440.myvitalife.model.quotes

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.isActive
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel

/**
 * Builds a Retrofit instance with a base URL and Gson converter factory to make network requests for quotes.
 * @_retrofit the Retrofit instance created with the base URL and Gson converter factory.
 * @service an interface that defines a suspend function to get a quote of the day from the API.
 */
val retrofit = Retrofit.Builder()
    .baseUrl("https://api.forismatic.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val service = retrofit.create(QuoteService::class.java)


/**
 * Fetches the Quote of the Day from the specified API endpoint.
 *
 * @param method The method to use for fetching the quote. Default value is "getQuote".
 * @param lang The language to use for the quote. Default value is "en".
 * @param format The format of the quote data. Default value is "json".
 *
 * @return A Quote object representing the quote of the day.
 */
interface QuoteService {
    @GET("api/1.0/")
    suspend fun getQuoteOfTheDay(
        @Query("method") method: String = "getQuote",
        @Query("lang") lang: String = "en",
        @Query("format") format: String = "json"
    ): Quote
}

/**
 * Retrieves a random quote from the API and passes it to a callback function.
 * @param callback a function that accepts a [Quote] object and returns no output.
 */
@Composable
private fun GetRandomQuote(callback: (Quote) -> Unit) {
    LaunchedEffect(Unit) {
        try {
            val quote: Quote = service.getQuoteOfTheDay()
            if (isActive) {
                if (quote.author.isEmpty()) {
                    quote.author = "Anonymous"
                }
                quote.text = quote.text.trim()
                quote.author = quote.author.trim()
                callback(quote)
            }
        } catch (e: Exception) {
            Log.e("QUOTE_ERROR", e.toString())
            if (isActive) {
                callback(Quote("", ""))
            }
        }
    }
}

/**
 * Generates a quote if none is available in the [QuoteOfTheDay] object.
 * @param firebaseViewModel a ViewModel that handles the Firebase database interactions.
 */
@Composable
fun GenerateQuoteIfEmpty(
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
    val user = Firebase.auth.currentUser

    if (user != null) {
        if (QuoteOfTheDay.quote.text.isEmpty()) {
            GetRandomQuote { generatedQuote ->
                QuoteOfTheDay.quote = generatedQuote
                firebaseViewModel.addQuote(generatedQuote)
            }
        }
    }
}