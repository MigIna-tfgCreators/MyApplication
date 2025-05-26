package com.example.myapplication.classes.modules.auth.activity.viewModel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.classes.modules.auth.activity.model.AuthEvents
import com.example.myapplication.classes.modules.auth.activity.model.AuthState
import com.example.myapplication.classes.modules.auth.activity.routing.RegisterRoutingInterface
import com.example.myapplication.classes.providers.ContextProviderInterface
import com.example.myapplication.classes.repositories.authUserRepository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignViewModel(
    private val repository: AuthRepository,
    private val routing: RegisterRoutingInterface,
    private val contextProvider: ContextProviderInterface
): ViewModel() {

    private val _signState = MutableStateFlow<AuthState>(AuthState())
    val signState: StateFlow<AuthState> = _signState.asStateFlow()

    fun addEvent(event: AuthEvents){
        when(event) {
            is AuthEvents.CheckCreation -> checkCreateUser(event.name,event.email, event.pswd)
            is AuthEvents.CheckAuth -> checkLoginUser(event.email, event.pswd)
            AuthEvents.CheckSession -> checkSession()
            AuthEvents.RegisterTrip -> toLoginScreen()
            AuthEvents.ClearErrors -> clearError()
        }
    }

    private fun checkCreateUser(name: String, email: String, pswd: String){
        viewModelScope.launch {
            _signState.value = _signState.value.copy(isLoading = true, errorMessage = null)
            try{
                val response = repository.register(name, email, pswd)

                if(response)
                    checkSession()
                else
                    throw Exception(contextProvider.currentActivity?.getString(R.string.error_message_create))

            }catch(e: Exception){
                _signState.value = _signState.value.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }

    private fun checkLoginUser(email: String, pswd: String){
        viewModelScope.launch {
            _signState.value = _signState.value.copy(isLoading = true, errorMessage = null)
            try {
                val response = repository.login(email, pswd)

                if (response)
                    checkSession()
                else
                    throw Exception(contextProvider.currentActivity?.getString(R.string.error_message_general))
            } catch (e: Exception) {
                _signState.value = _signState.value.copy(isLoading = false, errorMessage = e.message ?: contextProvider.currentActivity?.getString(R.string.error_message_login))
            }
        }
    }

    private fun checkSession(){
        viewModelScope.launch {
            _signState.value = _signState.value.copy(isLoading = true, errorMessage = null)
            try{
                val response = repository.session()

                if(response != null)
                    acceptAccess(response)
                else {
                    routing.navigateToLogin()
                    _signState.value = _signState.value.copy(isLoading = false)
                }
            } catch (e: Exception) {
                _signState.value = _signState.value.copy(isLoading = false)
            }
        }
    }
    private fun acceptAccess(response: String){
        viewModelScope.launch {
            var bundle = Bundle().apply {
                putString(contextProvider.currentActivity?.getString(R.string.bundle_email), response)
            }
            _signState.value = _signState.value.copy(isLoading = false)
            routing.navigateToMain(bundle)
        }
    }

    private fun toLoginScreen() = routing.navigateToRegister()


    private fun clearError() {
        _signState.value = _signState.value.copy(errorMessage = null, isLoading = false)
    }

}