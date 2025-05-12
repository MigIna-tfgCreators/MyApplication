package com.example.myapplication.classes.models.response

import com.example.myapplication.classes.models.API.PeliculaModel
import com.google.gson.annotations.SerializedName

data class PeliculaResponsePaginada(
    @SerializedName("page")
    val page: Int,

    @SerializedName("total_results")
    val totalResults: Int,

    @SerializedName("total_pages")
    val totalPages: Int,

    @SerializedName("results")
    val results: List<PeliculaModel>
)