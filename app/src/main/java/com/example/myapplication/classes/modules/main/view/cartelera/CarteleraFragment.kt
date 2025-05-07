package com.example.myapplication.classes.modules.main.view.cartelera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentCarteleraBinding

class CarteleraFragment : Fragment() {

    private lateinit var binding: FragmentCarteleraBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCarteleraBinding.inflate(inflater, container, false)



        return binding.root
    }

}