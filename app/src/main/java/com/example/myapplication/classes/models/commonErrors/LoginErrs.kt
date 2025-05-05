package com.prueba.apphouse.classes.models.commonErrors

sealed class ErrorLogin {
    object Erlogin: ErrorLogin()
    object Ercreate: ErrorLogin()
}