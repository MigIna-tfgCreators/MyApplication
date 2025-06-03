package com.example.myapplication.classes.modules.main.profile.routing

import com.example.myapplication.classes.managers.Navigation
import com.example.myapplication.classes.managers.NavigationManagerInterface
import com.example.myapplication.classes.modules.auth.activity.view.SignActivity

class ProfileRouting(
    private val navigationManager: NavigationManagerInterface
): ProfileRoutingInterface {

    override fun navigateToStart() = navigationManager.navigate(Navigation.main(SignActivity::class.java))

}