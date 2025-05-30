package com.example.myapplication.classes.modules.main.details.model

sealed class DetailsEvent {
    data class ShowDetails(val id: Int): DetailsEvent()
    data class ShowCredits(val id: Int): DetailsEvent()
    data class ShowTrailer(val id: Int): DetailsEvent()
    object ClearError: DetailsEvent()
}