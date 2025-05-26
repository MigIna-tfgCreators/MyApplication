package com.example.myapplication.classes.modules.auth.activity.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import com.example.myapplication.classes.modules.auth.activity.model.AuthEvents
import com.example.myapplication.classes.modules.auth.activity.viewModel.SignViewModel
import com.example.myapplication.databinding.ActivitySignBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySignBinding
    private val viewModel: SignViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkSession()

        observeLoading()

    }
    private fun checkSession(){
        viewModel.addEvent(AuthEvents.CheckSession)
    }

    private fun observeLoading(){
        viewModel.viewModelScope.launch {
            viewModel.signState.collect { state ->
                binding.progressBar.visibility = if (state.isLoading == true) View.VISIBLE else View.GONE
            }
        }
    }
}