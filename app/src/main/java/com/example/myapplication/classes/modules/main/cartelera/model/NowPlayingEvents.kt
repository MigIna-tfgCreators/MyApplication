package com.example.myapplication.classes.modules.main.cartelera.model

import androidx.lifecycle.ViewModel

sealed class NowPlayingEvents {
    object ShowAllList: NowPlayingEvents()
    object ResetAll: NowPlayingEvents()
    data class ShowSearchedList(val query: String): NowPlayingEvents()
}