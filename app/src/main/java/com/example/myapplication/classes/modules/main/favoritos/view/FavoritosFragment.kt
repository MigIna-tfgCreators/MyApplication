package com.example.myapplication.classes.modules.main.favoritos.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentFavoritosBinding

class FavoritosFragment : Fragment() {

    private lateinit var binding: FragmentFavoritosBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritosBinding.inflate(layoutInflater, container, false)



        return binding.root
    }

}