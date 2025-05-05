package com.prueba.apphouse.classes.modules.start.routing

import android.os.Bundle
import androidx.collection.floatSetOf
import com.example.myapplication.R
import com.example.myapplication.classes.managers.NavigationManagerInterface
import com.prueba.apphouse.classes.managers.Navigation

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
        var flag = false
        if(bundle!= null)
            flag = true
            //navigationManager.navigate(Navigation.main(NaveganteActivity::class.java, bundle))
        else
            flag = false
            //navigationManager.navigate(Navigation.main(NaveganteActivity::class.java))
    }

}