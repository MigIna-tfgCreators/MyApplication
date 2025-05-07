package com.example.myapplication.classes.modules.main.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.NavigationUI
import com.example.myapplication.R
import com.example.myapplication.classes.managers.navHostFragment
import com.example.myapplication.classes.models.main.MainEvents
import com.example.myapplication.classes.models.main.SelectedCardState
import com.example.myapplication.classes.models.main.toNiceString
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.classes.modules.main.viewmodel.PeliculasViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

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

    private fun cambiarUI (seleccionado: SelectedCardState){

        var colorCartelera = resources.getColor(R.color.azul_200)
        var colorPopulares= resources.getColor(R.color.azul_200)
        var colorTop= resources.getColor(R.color.azul_200)

        when(seleccionado) {
            SelectedCardState.Cartelera -> colorCartelera = resources.getColor(R.color.verde_200)

            SelectedCardState.Popular -> colorPopulares = resources.getColor(R.color.verde_200)

            SelectedCardState.Top -> colorTop = resources.getColor(R.color.verde_200)

        }
        binding.apply {

            binding.tvTitulo.text = seleccionado.toNiceString
        }
    }
    fun menuListener(){

        binding.botonesNavegacion.setOnItemSelectedListener { menuItem->

            when(menuItem.itemId){
                R.id.botonCartelera -> {
                    binding.tvTitulo.text = "Cartelera"
                    viewModel.addEvent(MainEvents.ListaCartelera, null)
                    true
                }
                R.id.botonTop -> {
                    binding.tvTitulo.text = "Top"
                    viewModel.addEvent(MainEvents.ListaTop, null)
                    true
                }
                R.id.botonBusqueda -> {
                    binding.tvTitulo.text = "Búsqueda"
                    viewModel.addEvent(MainEvents.ListaBusqueda, null)
                    true
                }
                R.id.botonFavoritos -> {
                    binding.tvTitulo.text = "Favoritos"
                    viewModel.addEvent(MainEvents.ListaFavoritos, null)
                    true
                }
                R.id.botonPerfil -> {
                    viewModel.addEvent(MainEvents.Perfil, null)
                    true
                }
                else -> false
            }
        }
    }

}


