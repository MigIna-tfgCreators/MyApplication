package com.example.myapplication.classes.modules.auth.routing

import android.os.Bundle
import com.example.myapplication.R
import com.example.myapplication.classes.managers.Navigation
import com.example.myapplication.classes.managers.NavigationManagerInterface
import com.example.myapplication.classes.modules.main.activity.view.MainActivity

class RegisterRouting(
    private val navigationManager: NavigationManagerInterface
): RegisterRoutingInterface{
    override fun navigateToLogin() {
        navigationManager.navigate(Navigation.to(R.id.loginFragment))
    }

    override fun navigateToRegister() {
        navigationManager.navigate(Navigation.to(R.id.registerFragment))
    }

    override fun navigateToMain(bundle: Bundle?) {
        navigationManager.navigate(Navigation.main(MainActivity::class.java, bundle))
    }

}