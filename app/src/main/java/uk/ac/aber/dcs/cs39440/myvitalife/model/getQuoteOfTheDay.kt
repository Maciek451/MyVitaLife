package uk.ac.aber.dcs.cs39440.myvitalife.model

import retrofit2.http.GET
import retrofit2.http.Query

interface QuoteService {
    @GET("api/1.0/")
    suspend fun getQuoteOfTheDay(
        @Query("method") method: String = "getQuote",
        @Query("lang") lang: String = "en",
        @Query("format") format: String = "json"
    ): Quote
}