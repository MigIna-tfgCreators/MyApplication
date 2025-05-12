package com.example.myapplication.classes.modules.main.busqueda.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentBusquedaBinding

class BusquedaFragment : Fragment() {

    private lateinit var binding: FragmentBusquedaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBusquedaBinding.inflate(layoutInflater, container, false)



        return binding.root
    }
}