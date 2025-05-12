package com.example.myapplication.classes.modules.main.activity.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.NavigationUI
import com.example.myapplication.R
import com.example.myapplication.classes.managers.navHostFragment
import com.example.myapplication.classes.modules.main.activity.viewmodel.PeliculasViewModel
import com.example.myapplication.classes.modules.main.activity.model.MainEvents
import com.example.myapplication.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: PeliculasViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        navHostFragment?.let {
            NavigationUI.setupWithNavController(binding.botonesNavegacion,it.navController)
        }
        menuListener()

    }

    fun menuListener(){

        binding.botonesNavegacion.setOnItemSelectedListener { menuItem->

            when(menuItem.itemId){
                R.id.botonCartelera -> {
                    binding.tvTitulo.text = "Cartelera"
                    viewModel.addEventNavegation(MainEvents.ListaCartelera, null)
                    true
                }
                R.id.botonTop -> {
                    binding.tvTitulo.text = "Top"
                    viewModel.addEventNavegation(MainEvents.ListaTop, null)
                    true
                }
                R.id.botonBusqueda -> {
                    binding.tvTitulo.text = "Búsqueda"
                    viewModel.addEventNavegation(MainEvents.ListaBusqueda, null)
                    true
                }
                R.id.botonFavoritos -> {
                    binding.tvTitulo.text = "Favoritos"
                    viewModel.addEventNavegation(MainEvents.ListaFavoritos, null)
                    true
                }
                R.id.botonPerfil -> {
                    //viewModel.addEventNavegation(MainEvents.Perfil, null)
                    true
                }
                else -> false
            }
        }
    }

}