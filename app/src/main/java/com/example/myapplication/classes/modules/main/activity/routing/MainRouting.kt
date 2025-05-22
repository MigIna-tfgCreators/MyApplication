package com.example.myapplication.classes.modules.main.activity.routing

import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.classes.managers.Navigation
import com.example.myapplication.classes.managers.NavigationManagerInterface

class MainRouting(
    private val navigationManager: NavigationManagerInterface
): MainRoutingInterface {

    override fun navigateToNowPlaying() = navigationManager.navigate(Navigation.to(R.id.nowPlayingFragment))

    override fun navigateToTop() = navigationManager.navigate(Navigation.to(R.id.topFragment))

    override fun navigateToDetails(bundle: Bundle?) = navigationManager.navigate(Navigation.withArguments(R.id.detailsFragment, bundle))

    override fun navigateToSearch() = navigationManager.navigate(Navigation.to(R.id.searchFragment))

    override fun navigateToFavorites() = navigationManager.navigate(Navigation.to(R.id.favoritesFragment))

}