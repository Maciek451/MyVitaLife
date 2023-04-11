package uk.ac.aber.dcs.cs39440.myvitalife.model.quotes

import com.google.gson.annotations.SerializedName
import uk.ac.aber.dcs.cs39440.myvitalife.model.DesiredDate

data class Quote(
    @SerializedName("quoteText") var text: String = "",
    @SerializedName("quoteAuthor") var author: String = "",
    var isFavourite: Boolean = false,
    var date: String = DesiredDate.date
)
