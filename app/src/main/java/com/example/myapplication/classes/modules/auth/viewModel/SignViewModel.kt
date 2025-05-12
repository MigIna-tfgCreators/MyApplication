package com.example.myapplication.classes.modules.auth.viewModel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.classes.models.commonErrors.showErrs.DialogErrsInterface
import com.example.myapplication.classes.modules.auth.model.LoginEvents
import com.example.myapplication.classes.modules.auth.routing.RegisterRoutingInterface
import com.example.myapplication.classes.providers.ContextProviderInterface
import com.example.myapplication.classes.repositories.authUserRepository.AuthRepository
import kotlinx.coroutines.launch

class SignViewModel(
    private val repository: AuthRepository,
    private val routing: RegisterRoutingInterface,
    private val errors: DialogErrsInterface,
    private val contextProvider: ContextProviderInterface
): ViewModel() {
    fun addEvent(evento: LoginEvents){
        when(evento) {
            is LoginEvents.CheckCreation -> checkCreateUser(evento.nombre,evento.correo, evento.pswd)
            is LoginEvents.CheckLogin -> checkLoginUser(evento.correo, evento.pswd)
            LoginEvents.CheckSession -> checkSession()
            LoginEvents.RegisterTrip -> toLoginScreen()
        }
    }
    private fun checkCreateUser(nombre: String, correo: String, pswd: String){
        viewModelScope.launch {
            if(repository.register(nombre, correo, pswd))
                checkSession()
            else
                errors.showErrorRegister()
        }
    }

    private fun checkLoginUser(correo: String, pswd: String){
        viewModelScope.launch {
            if(repository.login(correo, pswd))
                checkSession()
            else
                errors.showErrorLogin()
        }
    }

    private fun checkSession(){
        viewModelScope.launch {
            val respuesta = repository.session()
            if(respuesta != null)
                acceptAccess(respuesta)
            else
                routing.navigateToLogin()
        }
    }
    private fun acceptAccess(respuesta: String){
        viewModelScope.launch {
            var bundle = Bundle().apply {
                putString(contextProvider.currentActivity?.getString(R.string.bundle_email), respuesta)
            }
            routing.navigateToMain(bundle)
        }
    }
    private fun toLoginScreen(){
        routing.navigateToRegister()
    }

}