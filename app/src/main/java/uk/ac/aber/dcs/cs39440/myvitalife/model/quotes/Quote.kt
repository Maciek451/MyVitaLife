package uk.ac.aber.dcs.cs39440.myvitalife.model.quotes

import com.google.gson.annotations.SerializedName
import uk.ac.aber.dcs.cs39440.myvitalife.model.DesiredDate

/**
 * A data class representing a quote entry.
 *
 * @param text the text of the quote. SerializedName annotation required for Retrofit API requests
 * @param author the author of the quote. SerializedName annotation required for Retrofit API requests
 * @param isFavourite a boolean indicating whether the quote is a favorite.
 * @param date the date on which the quote was generated.
 */
data class Quote(
    @SerializedName("quoteText") var text: String = "",
    @SerializedName("quoteAuthor") var author: String = "",
    var isFavourite: Boolean = false,
    var date: String = DesiredDate.date
)
