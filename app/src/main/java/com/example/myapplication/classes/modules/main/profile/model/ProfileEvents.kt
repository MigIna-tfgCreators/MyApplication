package com.example.myapplication.classes.modules.main.profile.model


sealed class ProfileEvents(){
    object LogOut: ProfileEvents()
    object GetPersonalInformation: ProfileEvents()
}
