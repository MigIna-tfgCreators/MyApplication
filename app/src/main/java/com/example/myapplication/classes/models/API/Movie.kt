package com.example.myapplication.classes.models.API

import com.example.myapplication.classes.extensions.valueOrEmpty
import com.example.myapplication.classes.extensions.valueOrZero
import com.example.myapplication.classes.models.response.MovieDTO
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Movie(
    @SerializedName("id") val movieId: Int = 0,

    @SerializedName("title") val movieTitle: String = "",

    @SerializedName("overview") val movieDescription: String? = "",

    @SerializedName("poster_path") val moviePoster: String? = "",

    @SerializedName("release_date") val movieReleaseDate: String? = "",

    @SerializedName("vote_average") val movieAverageVote: Float? = 0.0f,

    @SerializedName("vote_count") val movieTotalVotes: Float? = 0.0f,

    @SerializedName("genres") val movieGenres: List<Genre>? = emptyList()

): Serializable

data class Genre(
    @SerializedName("id") val genreId: Int,
    @SerializedName("name") val genreName: String
)

val MovieDTO.toModel: Movie
    get(){
        return Movie(
            movieId = this@toModel.movieId.valueOrZero,
            movieTitle = this@toModel.movieTitle.valueOrEmpty,
            movieDescription = this@toModel.movieDescription.valueOrEmpty,
            moviePoster = movieImage.valueOrEmpty,
            movieReleaseDate = this@toModel.movieReleaseDate.valueOrEmpty,
            movieAverageVote = this@toModel.movieAverageVote,
            movieTotalVotes = this@toModel.movieTotalVotes,
            movieGenres = this.movieGenres?.map { Genre(genreId = it.genreId, genreName = it.genreName) }.valueOrEmpty
        )
    }
