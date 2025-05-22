package com.example.myapplication.classes.modules.auth.activity.routing

import android.os.Bundle

interface RegisterRoutingInterface {
    fun navigateToLogin()
    fun navigateToRegister()
    fun navigateToMain(bundle: Bundle?)
}