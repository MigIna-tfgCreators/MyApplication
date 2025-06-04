package com.example.myapplication.classes.services.firebase.authUserService

import android.util.Log
import com.example.myapplication.classes.models.API.Movie
import com.example.myapplication.classes.models.firebase.UsersModel
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthServiceImpl: AuthService{

    override val db: FirebaseFirestore
        get() =  FirebaseFirestore.getInstance()

    override suspend fun logout() { FirebaseAuth.getInstance().signOut() }

    override suspend fun session(): String?{

        var connected = FirebaseAuth.getInstance().currentUser
        return if (connected != null){

            val snap = db.collection("Usuarios").document(connected.uid).get().await()

            snap.getString("Nombre")
        }else{
            return null
        }
    }



    override suspend fun register(name: String,email: String, pswd: String): Boolean {
        return try{
            val id = checkUser(email,pswd, true).toString()

            val user = UsersModel(id,name, email)

            db.collection("Usuarios").document(id).set(
                hashMapOf(
                    "Nombre" to user.userName,
                    "Correo" to user.userEmail
                )
            ).await()

            return id != null

        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw Exception("Credenciales inválidas")
            false
        } catch (e: FirebaseAuthEmailException){
            throw Exception("Email ya está en uso")
            false
        } catch (e: FirebaseAuthWeakPasswordException) {
            throw Exception("Contraseña débil: ${e.reason}")
            false
        }catch (e: Exception){
            throw Exception("Error desconocido ${e.localizedMessage}")
            false
        }
    }

    override suspend fun login(email: String, pswd: String): Boolean {
        return try {
            val id = checkUser(email,pswd,false).toString()

            id != null
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw Exception("Credenciales inválidas al iniciar sesión: $email")
            false
        } catch (e: Exception){
            throw Exception("Error desconocido ${e.localizedMessage}")
            false
        }
    }

    override suspend fun checkUser(email: String, pswd: String, isCreating: Boolean): String?{
        var auth: AuthResult
        if(isCreating)
            auth = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pswd).await()
        else
            auth = FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pswd).await()

        return auth.user?.uid
    }

}