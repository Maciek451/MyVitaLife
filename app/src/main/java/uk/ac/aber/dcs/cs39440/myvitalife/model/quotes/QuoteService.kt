package uk.ac.aber.dcs.cs39440.myvitalife.model.quotes

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.isActive
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import uk.ac.aber.dcs.cs39440.myvitalife.model.Food
import uk.ac.aber.dcs.cs39440.myvitalife.model.quotes.Quote
import uk.ac.aber.dcs.cs39440.myvitalife.ui.FirebaseViewModel

val retrofit = Retrofit.Builder()
    .baseUrl("https://api.forismatic.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val service = retrofit.create(QuoteService::class.java)

interface QuoteService {
    @GET("api/1.0/")
    suspend fun getQuoteOfTheDay(
        @Query("method") method: String = "getQuote",
        @Query("lang") lang: String = "en",
        @Query("format") format: String = "json"
    ): Quote
}

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

@Composable
fun GenerateQuoteIfEmpty(
    firebaseViewModel: FirebaseViewModel = viewModel()
) {
    if (QuoteOfTheDay.quote.text.isEmpty()) {
        GetRandomQuote { generatedQuote ->
            QuoteOfTheDay.quote = generatedQuote
            firebaseViewModel.addQuote(generatedQuote)
        }
    }
}