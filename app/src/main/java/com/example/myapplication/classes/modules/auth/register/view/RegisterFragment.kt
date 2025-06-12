package com.example.myapplication.classes.modules.auth.register.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.classes.modules.auth.activity.model.AuthEvents
import com.example.myapplication.classes.modules.auth.activity.model.AuthState
import com.example.myapplication.classes.modules.auth.activity.viewModel.SignViewModel
import com.example.myapplication.classes.providers.FirebaseTranslator
import com.example.myapplication.databinding.FragmentRegisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: SignViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLoading()

        binding.btAccessNew.setOnClickListener {
            val name = binding.etNewName.text.toString().trim()
            val email = binding.etNewEmail.text.toString().trim()
            val password = binding.etNewPswd.text.toString().trim()

            // Limpiar errores anteriores
            binding.tilNewName.error = null
            binding.tilNewEmail.error = null
            binding.tilNewPswd.error = null

            var isValid = true

            if (name.isEmpty()) {
                binding.tilNewName.error = getString(R.string.error_name_empty)
                isValid = false
            }

            if (email.isEmpty()) {
                binding.tilNewEmail.error = getString(R.string.error_email_empty)
                isValid = false
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tilNewEmail.error = getString(R.string.error_email_invalid)
                isValid = false
            }

            if (password.length < 6) {
                binding.tilNewPswd.error = getString(R.string.error_password_short)
                isValid = false
            }

            if (isValid) {
                viewModel.viewModelScope.launch {
                    viewModel.addEvent(AuthEvents.CheckCreation(name, email, password))
                }
            }
        }

        binding.btGoBack.setOnClickListener {
            findNavController().navigateUp()
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
                binding.progressBarRegister.visibility =
                    if (state.isLoading == true) View.VISIBLE else View.GONE
            }
        }
    }
}
