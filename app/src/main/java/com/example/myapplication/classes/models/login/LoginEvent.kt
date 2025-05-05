package com.example.myapplication.classes.models.login

import androidx.lifecycle.ViewModel

sealed class LoginEvents {
    data class CheckLogin(val correo: String, val pswd: String): LoginEvents()
    data class CheckCreation(val nombre: String, val correo: String, val pswd: String): LoginEvents()
    object RegisterTrip: LoginEvents()
    object CheckSession: LoginEvents()
}
fun <T: LoginEvents> ViewModel.handleEvent(event: T, handler: (T) -> Unit){ handler(event) }