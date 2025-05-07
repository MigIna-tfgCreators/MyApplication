package com.example.myapplication.classes.services.authUserService

import android.util.Log
import com.example.myapplication.classes.models.API.PeliculaModel
import com.example.myapplication.classes.models.firebase.UsuariosModel
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


    override suspend fun session(): String?{

        var connected = FirebaseAuth.getInstance().currentUser
        Log.i("comprob", connected?.uid ?: "hola")

        return if (connected != null){

            val snap = db.collection("Usuarios")
                .whereEqualTo("Correo",connected.email)
                .get().await()

            val document = snap.documents[0]
            val check = document.getString("Nombre")

            return check
        }else{
            return null
        }
    }



    override suspend fun register(name: String,email: String, pswd: String): Boolean {
        return try{
            val id = checkUser(email,pswd, true).toString()

            val usuario = UsuariosModel(id,name, email, pswd, listOf<PeliculaModel>())

            db.collection("Usuarios").document(id).set(
                hashMapOf(
                    "Nombre" to usuario.nombre,
                    "Correo" to usuario.correo,
                    "Lista Personal" to usuario.listaPersonal
                )
            ).await()

            return id != null

        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Log.e("mal", "Credenciales inválidas al iniciar sesión: $email")
            false
        } catch (e: FirebaseAuthEmailException){
            Log.e("mal", "Email ya usado")
            false
        } catch (e: FirebaseAuthWeakPasswordException) {
            Log.e("mal", "Contraseña débil: ${e.reason}")
            false
        }catch (e: Exception){
            false
        }
    }

    override suspend fun login(correo: String, pswd: String): Boolean {
        return try {
            val id = checkUser(correo,pswd,false).toString()

            id != null
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Log.e("mal", "Credenciales inválidas al iniciar sesión: $correo")
            false
        } catch (e: Exception){
            false
        }
    }

    override suspend fun checkUser(correo: String, pswd: String, creando: Boolean): String?{
        var auth: AuthResult
        if(creando)
            auth = FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo,pswd).await()
        else
            auth = FirebaseAuth.getInstance().signInWithEmailAndPassword(correo,pswd).await()

        return auth.user?.uid
    }

}