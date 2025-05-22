package com.example.myapplication.classes.modules.main.activity.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.classes.modules.main.activity.routing.MainRoutingInterface
import com.example.myapplication.classes.modules.main.activity.model.MainEvents

class MoviesMainViewModel(
    private val routing: MainRoutingInterface
): ViewModel() {

    fun addEventNavigation(events: MainEvents) {
        when(events) {
            MainEvents.SearchList -> {
                routing.navigateToSearch()
            }
            MainEvents.NowPlayingList -> {
                routing.navigateToNowPlaying()
            }
            MainEvents.FavoritesList -> {
                routing.navigateToFavorites()
            }
            MainEvents.TopList -> {
                routing.navigateToTop()
            }
            MainEvents.AppearanceProfile -> {
                //Todavía por implementar
            }
        }
    }




}