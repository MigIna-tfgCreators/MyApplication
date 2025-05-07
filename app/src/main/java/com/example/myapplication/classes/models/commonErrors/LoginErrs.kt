package com.example.myapplication.classes.models.commonErrors

sealed class ErrorLogin {
    object Erlogin: ErrorLogin()
    object Ercreate: ErrorLogin()
}