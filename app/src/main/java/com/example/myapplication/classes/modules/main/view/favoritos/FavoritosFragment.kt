package com.example.myapplication.classes.modules.main.view.favoritos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
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