package com.example.myapplication.classes.modules.login.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.R
import com.example.myapplication.classes.models.login.LoginEvents
import com.example.myapplication.classes.modules.login.viewModel.SignViewModel
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
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.apply {
            var correo: String
            var pswd: String

            btAccessLg.setOnClickListener {
                correo = etCorreoLog.text.toString()
                pswd = etPswdLog.text.toString()

                viewModel.addEvent(LoginEvents.CheckLogin(correo, pswd))

                CoroutineScope(Dispatchers.Main).launch { delay(1000) }

                etCorreoLog.setText(getString(R.string.white))
                etPswdLog.setText(getString(R.string.white))
            }
            btNewAccount.setOnClickListener {
                viewModel.addEvent(LoginEvents.RegisterTrip)
            }
        }

        return binding.root
    }


}