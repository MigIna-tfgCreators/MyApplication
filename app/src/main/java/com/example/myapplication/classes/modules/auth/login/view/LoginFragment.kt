package com.example.myapplication.classes.modules.auth.login.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.classes.modules.auth.activity.model.AuthEvents
import com.example.myapplication.classes.modules.auth.activity.model.AuthState
import com.example.myapplication.classes.modules.auth.activity.viewModel.SignViewModel
import com.example.myapplication.databinding.FragmentLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment: Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: SignViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLoading()

        binding.btAccessLg.setOnClickListener {
            viewModel.viewModelScope.launch {
                binding.apply {
                    viewModel.addEvent(AuthEvents.CheckAuth(etEmailLog.text.toString(), etPswdLog.text.toString()))
                }
                clearData()
            }
        }

        binding.btNewAccount.setOnClickListener {
            viewModel.addEvent(AuthEvents.RegisterTrip)
        }

        viewModel.viewModelScope.launch {
            viewModel.signState.collect { showError(it) }
        }
    }

    private fun clearData(){
        binding.apply {
            etEmailLog.setText(getString(R.string.white))
            etPswdLog.setText(getString(R.string.white))
        }
    }

    private fun showError(state: AuthState){
        if (!isAdded || context == null) return
        if(!state.errorMessage.isNullOrBlank())
            state.errorMessage.let { error ->
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.error_type))
                    .setMessage(error)
                    .setPositiveButton(R.string.general_ok) { dialog, _ ->
                        dialog.dismiss()
                        viewModel.addEvent(AuthEvents.ClearErrors)
                    }
                    .show()
            }
    }

    private fun observeLoading(){
        viewModel.viewModelScope.launch {
            viewModel.signState.collect { state ->
                binding.progressBarLogin.visibility = if (state.isLoading == true) View.VISIBLE else View.GONE
            }
        }
    }

}