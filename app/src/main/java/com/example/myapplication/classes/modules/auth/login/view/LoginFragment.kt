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
import com.example.myapplication.classes.providers.FirebaseTranslator
import com.example.myapplication.databinding.FragmentLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: SignViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLoading()

        binding.btAccessLg.setOnClickListener {
            val email = binding.etEmailLog.text.toString().trim()
            val password = binding.etPswdLog.text.toString().trim()

            // Limpiar errores anteriores
            binding.tilEmailLog.error = null
            binding.tilPswdLog.error = null

            var isValid = true

            if (email.isEmpty()) {
                binding.tilEmailLog.error = getString(R.string.campo_error)
                isValid = false
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tilEmailLog.error = getString(R.string.error_email_invalid)
                isValid = false
            }

            if (password.isEmpty()) {
                binding.tilPswdLog.error = getString(R.string.campo_error)
                isValid = false
            }

            if (isValid) {
                viewModel.viewModelScope.launch {
                    viewModel.addEvent(AuthEvents.CheckAuth(email, password))
                }
            }
        }

        binding.btNewAccount.setOnClickListener {
            viewModel.addEvent(AuthEvents.RegisterTrip)
        }

        viewModel.viewModelScope.launch {
            viewModel.signState.collect { showGlobalError(it) }
        }



    }

    private fun showGlobalError(state: AuthState) {
        state.errorMessage?.let { error ->
            FirebaseTranslator().initTranslate(error,
                onResult = { translatedText ->
                    AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.error_type))
                        .setMessage(translatedText ?: error)
                        .setPositiveButton(R.string.general_ok) { dialog, _ ->
                            dialog.dismiss()
                            viewModel.addEvent(AuthEvents.ClearErrors)
                        }
                        .show()
                },
                onError = {
                    AlertDialog.Builder(requireContext())
                        .setTitle(getString(R.string.error_type))
                        .setMessage(error)
                        .setPositiveButton(R.string.general_ok) { dialog, _ ->
                            dialog.dismiss()
                            viewModel.addEvent(AuthEvents.ClearErrors)
                        }
                        .show()
                })
        }
    }

    private fun observeLoading() {
        viewModel.viewModelScope.launch {
            viewModel.signState.collect { state ->
                binding.progressBarLogin.visibility =
                    if (state.isLoading == true) View.VISIBLE else View.GONE
            }
        }
    }
}