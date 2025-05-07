package com.example.myapplication.classes.modules.login.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.classes.models.login.LoginEvents
import com.example.myapplication.classes.modules.login.viewModel.SignViewModel
import com.example.myapplication.databinding.FragmentRegisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel: SignViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.apply {
            var nombre: String
            var correo: String
            var pswd: String

            btAccessNew.setOnClickListener {
                nombre = etNewName.text.toString()
                correo = etNewEmail.text.toString()
                pswd = etPswdNew.text.toString()

                viewModel.addEvent(LoginEvents.CheckCreation(nombre, correo, pswd))
                CoroutineScope(Dispatchers.Main).launch { delay(1000) }

                etNewName.setText(getString(R.string.white))
                etNewEmail.setText(getString(R.string.white))
                etPswdNew.setText(getString(R.string.white))
            }
        }

        return binding.root
    }


}