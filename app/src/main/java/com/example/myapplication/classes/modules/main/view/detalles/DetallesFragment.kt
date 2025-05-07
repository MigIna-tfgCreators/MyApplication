package com.example.myapplication.classes.modules.main.view.detalles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.databinding.FragmentDetallesBinding

class DetallesFragment : Fragment() {

    private lateinit var binding: FragmentDetallesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetallesBinding.inflate(inflater, container, false)


        return binding.root
    }
}