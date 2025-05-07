package com.example.myapplication.classes.modules.login.routing

import android.os.Bundle

interface RegisterRoutingInterface {
    fun navigateToLogin()
    fun navigateToRegister()
    fun navigateToMain(bundle: Bundle?)
}