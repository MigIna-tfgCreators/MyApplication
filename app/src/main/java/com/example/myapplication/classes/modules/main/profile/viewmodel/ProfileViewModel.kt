package com.example.myapplication.classes.modules.main.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.classes.modules.main.profile.model.ProfileEvents
import com.example.myapplication.classes.modules.main.profile.model.ProfileState
import com.example.myapplication.classes.modules.main.profile.routing.ProfileRoutingInterface
import com.example.myapplication.classes.repositories.firebase.authUserRepository.AuthRepository
import com.example.myapplication.classes.repositories.firebase.usermovieRepository.UserMovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: AuthRepository,
    private val moviesRepository: UserMovieRepository,
    private val routing: ProfileRoutingInterface
): ViewModel() {

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()



    fun addEvent(event: ProfileEvents){
        viewModelScope.launch {
            when(event) {
                ProfileEvents.GetPersonalInformation -> getInformation()
                ProfileEvents.LogOut -> logout()
            }
        }
    }

    private fun getInformation(){
        viewModelScope.launch {
            try{
                if(_profileState.value.isLoading) return@launch
                _profileState.value = _profileState.value.copy(isLoading = true)

                val information = moviesRepository.getPersonalInformation()
                val name = userRepository.session()

                _profileState.value = _profileState.value.copy(isLoading = false, userName = name, averageVotes = information[1], totalPersonalFilms = information[0].toInt())


            } catch(e: Exception){
                _profileState.value = _profileState.value.copy(isLoading = false, error = e.message)
            }

        }
    }

    private fun logout(){
        viewModelScope.launch {
            userRepository.logout()
            routing.navigateToStart()
        }
    }





}