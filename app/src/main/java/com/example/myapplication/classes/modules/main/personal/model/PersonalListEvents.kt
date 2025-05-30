package com.example.myapplication.classes.modules.main.personal.model

import com.example.myapplication.classes.models.API.Movie
import com.example.myapplication.classes.models.firebase.UserMovieExtraInfo

sealed class PersonalListEvents {
    object ShowPersonalList: PersonalListEvents()
    object ResetAll: PersonalListEvents()
    data class ShowSearchedList(val query: String): PersonalListEvents()
    data class AddPersonalMovie(val movie: Movie, val info: UserMovieExtraInfo?): PersonalListEvents()
    data class QuitPersonalMovie(val movie: Movie): PersonalListEvents()
    data class HasInPersonal(val movie: Movie, val info: UserMovieExtraInfo?): PersonalListEvents()

}