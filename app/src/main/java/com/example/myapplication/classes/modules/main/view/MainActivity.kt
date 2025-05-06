package com.example.myapplication.classes.modules.main.view

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.classes.models.main.SelectedCardState
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

        binding.apply {
            cvCartelera.setOnClickListener {
                cambiarUI(SelectedCardState.Cartelera)
            }
            cvTop.setOnClickListener {
                cambiarUI(SelectedCardState.Top)
            }
            cvPopulares.setOnClickListener {
                cambiarUI(SelectedCardState.Popular)
            }
        }
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
            cvCartelera.setCardBackgroundColor(colorCartelera)
            cvPopulares.setCardBackgroundColor(colorPopulares)
            cvTop.setCardBackgroundColor(colorTop)

            binding.tvTitulo.text = seleccionado.toString()
        }
    }
}