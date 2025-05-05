package com.prueba.apphouse.classes.modules.start.routing

import android.os.Bundle

interface RegisterRoutingInterface {
    fun navigateToLogin()
    fun navigateToRegister()
    fun navigateToMain(bundle: Bundle?)

}