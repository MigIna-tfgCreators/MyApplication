package com.example.myapplication.classes.repositories.firebase.authUserRepository

interface AuthRepository {
    suspend fun session(): String?
    suspend fun register(name: String,email: String, pswd: String): Boolean
    suspend fun login(email: String, pswd: String): Boolean
    suspend fun logout()
}