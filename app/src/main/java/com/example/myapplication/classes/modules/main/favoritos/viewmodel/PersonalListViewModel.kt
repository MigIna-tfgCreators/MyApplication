package com.example.myapplication.classes.modules.main.favoritos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.classes.modules.main.activity.model.GeneralMovieState
import com.example.myapplication.classes.modules.main.favoritos.model.PersonalListEvents
import com.example.myapplication.classes.repositories.firebase.usermovieRepository.UserMovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PersonalListViewModel(
    private val firebaseRepository: UserMovieRepository
): ViewModel(){

    private val _moviesState = MutableStateFlow<GeneralMovieState>(GeneralMovieState())
    val moviesState: StateFlow<GeneralMovieState> = _moviesState.asStateFlow()

    private var lastQuery = ""
    private var actualPage= 1

    fun addEventPersonal(event: PersonalListEvents){
        viewModelScope.launch {
            when(event){
                is PersonalListEvents.AddPersonalMovie -> TODO()
                is PersonalListEvents.QuitPersonalMovie -> TODO()
                PersonalListEvents.ResetAll -> TODO()
                PersonalListEvents.ShowPersonalList -> TODO()
                is PersonalListEvents.ShowSearchedList -> TODO()
            }
        }
    }

}