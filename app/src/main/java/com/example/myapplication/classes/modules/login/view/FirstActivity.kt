package com.example.myapplication.classes.modules.login.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.classes.models.login.LoginEvents
import com.example.myapplication.classes.modules.login.viewModel.SignViewModel
import com.example.myapplication.databinding.ActivityFirstBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FirstActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstBinding
    private val viewModel: SignViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkSession()

    }
    private fun checkSession(){
        viewModel.addEvent(LoginEvents.CheckSession)
    }
}