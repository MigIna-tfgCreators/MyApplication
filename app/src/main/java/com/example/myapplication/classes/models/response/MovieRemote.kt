package com.example.myapplication.classes.models.response

import com.example.myapplication.classes.models.API.Genre
import com.google.gson.annotations.SerializedName

data class MovieRemote(
    @SerializedName("page") val moviePage: Int? = 1,

    @SerializedName("total_results") val movieTotalResults: Int? = 0,

    @SerializedName("total_pages") val movieTotalPages: Int? = 1,

    @SerializedName("results") val movieResults: List<MovieDTO>? = emptyList()
)


data class MovieDTO(
    @SerializedName("id") val movieId: Int?,

    @SerializedName("title") val movieTitle: String = "",

    @SerializedName("overview") val movieDescription: String? = "",

    @SerializedName("poster_path") val movieImage: String?,

    @SerializedName("backdrop_path") val movieBackdropPath: String?,

    @SerializedName("release_date") val movieReleaseDate: String?,

    @SerializedName("vote_average") val movieAverageVote: Float?,

    @SerializedName("vote_count") val movieTotalVotes: Float?,

    @SerializedName("runtime") val movieRuntime: Int?,

    @SerializedName("genres") val movieGenres: List<Genre>? = emptyList()
)
