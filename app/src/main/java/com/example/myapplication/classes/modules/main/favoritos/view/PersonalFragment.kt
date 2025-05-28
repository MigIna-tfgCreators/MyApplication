package com.example.myapplication.classes.modules.main.favoritos.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentMoviePersonalBinding

class PersonalFragment: Fragment() {

    private lateinit var binding: FragmentMoviePersonalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoviePersonalBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

}