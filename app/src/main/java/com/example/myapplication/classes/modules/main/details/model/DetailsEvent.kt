package com.example.myapplication.classes.modules.main.details.model

sealed class DetailsEvent {
    data class ShowDetails(val id: Int): DetailsEvent()
    data class ShowCredits(val id: Int): DetailsEvent()
    data class ShowTrailer(val id: Int): DetailsEvent()
    data class ShowPersonalData(val id: Int): DetailsEvent()
    data class UpdateData(val vote: Int, val review: String, val id: Int): DetailsEvent()
    object ClearError: DetailsEvent()
}