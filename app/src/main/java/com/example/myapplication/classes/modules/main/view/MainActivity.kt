package com.example.myapplication.classes.modules.main.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.classes.modules.main.viewmodel.PeliculasViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: PeliculasViewModel
    //private lateinit var adapterPeliculas: AdapterPeliculas


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[PeliculasViewModel::class.java]
        setupRecyclerView()



    }

    private fun setupRecyclerView() {
//        val layoutManager = GridLayoutManager(this,3)
//        binding.rvPeliculas.layoutManager = layoutManager
//        adapterPeliculas = AdapterPeliculas(this, arrayListOf())
//        binding.rvPeliculas.adapter = adapterPeliculas

    }


}