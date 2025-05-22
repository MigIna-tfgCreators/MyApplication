package com.example.myapplication.classes.modules.auth.activity.model

sealed class AuthEvents {
    data class CheckAuth(val email: String, val pswd: String): AuthEvents()
    data class CheckCreation(val name: String, val email: String, val pswd: String): AuthEvents()
    object RegisterTrip: AuthEvents()
    object CheckSession: AuthEvents()
    object ClearErrors: AuthEvents()
}