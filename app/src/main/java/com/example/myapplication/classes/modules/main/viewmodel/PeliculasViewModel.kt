package com.example.myapplication.classes.modules.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.configurations.core.API
import com.example.myapplication.classes.models.API.PeliculaModel
import com.example.myapplication.configurations.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PeliculasViewModel: ViewModel() {

    private var _listaPelicuas = MutableLiveData<List<PeliculaModel>>() //Permite modificar la informacion de la lista
    val listaPeliculas: LiveData<List<PeliculaModel>> = _listaPelicuas //Se va a cosumir en las listas y no se puede mdifiar

    fun obtenerCartelera(){
        viewModelScope.launch(Dispatchers.IO){
            val response = RetrofitClient.webService.obtenerCartelera(API.API_KEY)
            withContext(Dispatchers.Main){
                _listaPelicuas.value = response.body()!!.resultados.sortedByDescending { it.votoPromedio }
            }
        }
    }

    fun obtenerPopulares(){
        viewModelScope.launch(Dispatchers.IO){
            val response = RetrofitClient.webService.obtenerPopulares(API.API_KEY)
            withContext(Dispatchers.Main){
                _listaPelicuas.value = response.body()!!.resultados.sortedByDescending { it.votoPromedio }
            }
        }
    }
}