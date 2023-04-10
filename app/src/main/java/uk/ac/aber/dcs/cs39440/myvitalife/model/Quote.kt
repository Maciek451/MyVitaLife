package uk.ac.aber.dcs.cs39440.myvitalife.model

import com.google.gson.annotations.SerializedName

data class Quote(
    @SerializedName("quoteText") var text: String = "",
    @SerializedName("quoteAuthor") var author: String = "",
)
