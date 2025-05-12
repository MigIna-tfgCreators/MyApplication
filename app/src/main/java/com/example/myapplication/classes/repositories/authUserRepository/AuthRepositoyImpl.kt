package com.example.myapplication.classes.repositories.authUserRepository

import com.example.myapplication.classes.services.authUserService.AuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoyImpl(
    private val bbdd: AuthService
): AuthRepository {
    override suspend fun session(): String? {
        return withContext(Dispatchers.IO) {
            bbdd.session()
        }
    }

    override suspend fun register(name: String, email: String, pswd: String): Boolean {
        return withContext(Dispatchers.IO) {
            bbdd.register(name,email,pswd)
        }
    }

    override suspend fun login(email: String, pswd: String): Boolean {
        return withContext(Dispatchers.IO) {
            bbdd.login(email,pswd)
        }
    }

    override suspend fun logout() {
        bbdd.logout()
    }
}