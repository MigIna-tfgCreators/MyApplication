package com.example.myapplication.classes.models.response

import com.example.myapplication.classes.models.API.Genre
import com.google.gson.annotations.SerializedName

data class GenreResponse(
    @SerializedName("genres") val listGenres: List<Genre>?
)
