package com.example.myapplication.classes.modules.main.now_playing.model

import com.example.myapplication.classes.models.API.Movie
import com.example.myapplication.classes.models.firebase.UserMovieExtraInfo


sealed class NowPlayingEvents {
    object ShowAllList: NowPlayingEvents()
    object ResetAll: NowPlayingEvents()
    data class ShowSearchedList(val query: String): NowPlayingEvents()
    data class AddPersonalMovie(val movie: Movie, val info: UserMovieExtraInfo?): NowPlayingEvents()
    data class QuitPersonalMovie(val movie: Movie): NowPlayingEvents()
    data class HasInPersonal(val movie: Movie, val info: UserMovieExtraInfo?): NowPlayingEvents()
    data class CheckMovie(val movie: Movie): NowPlayingEvents()
}