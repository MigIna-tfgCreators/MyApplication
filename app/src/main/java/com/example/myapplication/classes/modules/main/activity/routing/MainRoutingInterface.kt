package com.example.myapplication.classes.modules.main.activity.routing

import android.os.Bundle

interface MainRoutingInterface {
    fun navigateToNowPlaying()
    fun navigateToTop()
    fun navigateToDetails(bundle: Bundle?)
    fun navigateToSearch()
    fun navigateToFavorites()
}