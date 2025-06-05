package com.example.myapplication.classes.modules.main.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.classes.extensions.valueOrEmpty
import com.example.myapplication.classes.models.firebase.UserMovieExtraInfo
import com.example.myapplication.classes.modules.main.details.model.DetailsEvent
import com.example.myapplication.classes.modules.main.details.model.DetailsState
import com.example.myapplication.classes.repositories.api.moviesRepository.MoviesRepository
import com.example.myapplication.classes.repositories.firebase.usermovieRepository.UserMovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val repository: MoviesRepository,
    private val firebaseRepository: UserMovieRepository
): ViewModel() {

    private val _movie = MutableStateFlow<DetailsState>(DetailsState())
    val movie: StateFlow<DetailsState> = _movie.asStateFlow()

    fun addDetailsEvent(event: DetailsEvent){
        viewModelScope.launch {
            when(event) {
                is DetailsEvent.ShowDetails -> getDetails(event.id)

                is DetailsEvent.ShowCredits -> getCredits(event.id)

                is DetailsEvent.ShowTrailer -> getTrailer(event.id)

                DetailsEvent.ClearError -> clearError()

                is DetailsEvent.ShowPersonalData -> getPersonalInformation(event.id)

                is DetailsEvent.UpdateData -> updateData(event.vote, event.review, event.id)
            }
        }
    }

    private fun getDetails(id: Int){
        viewModelScope.launch {
            try {
                _movie.value = _movie.value.copy(isLoading = true, error = null)

                val newDetails = repository.getMovieDetails(id)

                _movie.value = _movie.value.copy(actualFilm = newDetails, isLoading = false)
            } catch (e: Exception) {
                _movie.value = _movie.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun getCredits(id: Int){
        viewModelScope.launch {
            try {
                _movie.value = _movie.value.copy(isLoading = true, error = null)

                val newCredits = repository.getMovieCredits(id)

                _movie.value = _movie.value.copy(actualCredits = newCredits, isLoading = false)
            } catch (e: Exception) {
                _movie.value = _movie.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun getTrailer(id: Int){
        viewModelScope.launch {
            try {
                _movie.value = _movie.value.copy(isLoading = true, error = null)

                val list = repository.getYoutubeTrailer(id)

                val video = list.firstOrNull {
                    it.videoSite == "YouTube" && it.videoType == "Trailer"
                }

                if(video!= null)
                    _movie.value = _movie.value.copy(youtubeVideo = video, isLoading = false)

            } catch (e: Exception) {
                _movie.value = _movie.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun clearError() {
        _movie.value = _movie.value.copy(error = null)
    }

    private fun getPersonalInformation(id: Int){
        viewModelScope.launch {
            try {
                _movie.value = _movie.value.copy(isLoading = true, error = null)

                val info = firebaseRepository.getExtraInfo(id)

                _movie.value = _movie.value.copy(extraInfo = info , isLoading = false)

            } catch (e: Exception) {
                _movie.value = _movie.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun updateData(vote: Int, review: String, id: Int){
        viewModelScope.launch {
            try {
                _movie.value = _movie.value.copy(isLoading = true, error = null)

                val afterDate = _movie.value.extraInfo?.ownVoteDate
                val info = UserMovieExtraInfo(vote, afterDate.valueOrEmpty, review)

                firebaseRepository.updateInformation(id,info)

                _movie.value = _movie.value.copy(isLoading = false, extraInfo = info)

            } catch (e: Exception) {
                _movie.value = _movie.value.copy(isLoading = false, error = e.message)
            }
        }
    }

}