package com.example.myapplication.classes.modules.main.detalles.model

import androidx.lifecycle.ViewModel


sealed class DetailsEvent {
    data class ShowDetails(val id: Int): DetailsEvent()
    data class ShowCredits(val id: Int): DetailsEvent()
    data class ShowTrailer(val id: Int): DetailsEvent()
}