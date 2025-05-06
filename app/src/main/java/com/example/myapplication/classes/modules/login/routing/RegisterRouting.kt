package com.prueba.apphouse.classes.modules.start.routing

import android.os.Bundle
import androidx.collection.floatSetOf
import com.example.myapplication.R
import com.example.myapplication.classes.managers.Navigation
import com.example.myapplication.classes.managers.NavigationManagerInterface
import com.example.myapplication.classes.modules.main.view.MainActivity

class RegisterRouting(
    private val navigationManager: NavigationManagerInterface
): RegisterRoutingInterface {
    override fun navigateToLogin() {
        navigationManager.navigate(Navigation.to(R.id.loginFragment))
    }

    override fun navigateToRegister() {
        navigationManager.navigate(Navigation.to(R.id.registerFragment))
    }

    override fun navigateToMain(bundle: Bundle?) {
        if(bundle!= null)
            navigationManager.navigate(Navigation.main(MainActivity::class.java, bundle))
        else
            navigationManager.navigate(Navigation.main(MainActivity::class.java))
    }

}