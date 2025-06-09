package com.example.myapplication.classes.modules.auth.register.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.classes.modules.auth.activity.model.AuthEvents
import com.example.myapplication.classes.modules.auth.activity.model.AuthState
import com.example.myapplication.classes.modules.auth.activity.viewModel.SignViewModel
import com.example.myapplication.databinding.FragmentRegisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment: Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: SignViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLoading()

        binding.btAccessNew.setOnClickListener {
            viewModel.viewModelScope.launch {
                binding.apply {
                    viewModel.addEvent(AuthEvents.CheckCreation(etNewName.text.toString(), etNewEmail.text.toString(), etNewPswd.text.toString()))
                }
                clearData()
            }

        }

        viewModel.viewModelScope.launch {
            viewModel.signState.collect { showError(it) }
        }
    }

    private fun clearData(){
        binding.apply {
            etNewName.setText(getString(R.string.white))
            etNewEmail.setText(getString(R.string.white))
            etNewPswd.setText(getString(R.string.white))
        }
    }

    private fun showError(state: AuthState){
        state.errorMessage?.let { error ->
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
                binding.progressBarRegister.visibility = if (state.isLoading == true) View.VISIBLE else View.GONE
            }
        }
    }
}