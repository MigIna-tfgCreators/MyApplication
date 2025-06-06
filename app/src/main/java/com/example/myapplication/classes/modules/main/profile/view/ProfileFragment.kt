package com.example.myapplication.classes.modules.main.profile.view

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.classes.extensions.valueOrZero
import com.example.myapplication.classes.modules.main.profile.model.ProfileEvents
import com.example.myapplication.classes.modules.main.profile.model.ProfileState
import com.example.myapplication.classes.modules.main.profile.viewmodel.ProfileViewModel
import com.example.myapplication.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.math.BigDecimal
import java.math.RoundingMode

class ProfileFragment: DialogFragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModel()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext(), R.style.SlideInDialogTheme)
        dialog.setContentView(R.layout.fragment_profile)

        dialog.window?.apply {
            setLayout(
                (resources.displayMetrics.widthPixels * 0.5).toInt(),
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setGravity(Gravity.END)
            setWindowAnimations(R.style.SlideDialogAnimation)
            setBackgroundDrawableResource(R.drawable.background)

            addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            setDimAmount(0.5f)
        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)

        viewModel.addEvent(ProfileEvents.GetPersonalInformation)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {
            viewModel.addEvent(ProfileEvents.LogOut)
        }

        binding.btBackDetails.setOnClickListener {
            dismiss()
        }

        viewModel.viewModelScope.launch {
            viewModel.profileState.collect { state ->
                setUp(state)

                if (state.isLoading)
                    binding.progressProfile.visibility = View.VISIBLE
                else
                    binding.progressProfile.visibility = View.GONE
            }
        }
    }

    private fun setUp(state: ProfileState) {
        binding.apply {
            tvUsername.text = "Bienvenido/a ${state.userName}"

            tvWatchedMovies.text = state.totalPersonalFilms?.toString() ?: "0"

            val valorationAverage = state.averageVotes.valueOrZero

            val correctedValoration =
                if (!valorationAverage.isNaN() && !valorationAverage.isInfinite()) {
                    BigDecimal(valorationAverage).setScale(2, RoundingMode.HALF_UP).toString()
                } else {
                    "0.00"
                }

            tvFavoriteMovies.text = correctedValoration
        }
    }
}