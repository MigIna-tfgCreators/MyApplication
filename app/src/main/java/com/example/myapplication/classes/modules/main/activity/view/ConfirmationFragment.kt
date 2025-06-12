package com.example.myapplication.classes.modules.main.activity.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R
import com.example.myapplication.classes.extensions.valueOrNoReview
import com.example.myapplication.classes.models.API.Movie
import com.example.myapplication.classes.models.firebase.UserMovieExtraInfo
import com.example.myapplication.databinding.FragmentConfirmationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate

class ConfirmationFragment(
    private val isFav: Boolean,
    private val onDismissResult: (Boolean, UserMovieExtraInfo) -> Unit
): DialogFragment() {

    private lateinit var binding: FragmentConfirmationBinding

    private lateinit var stars: List<ImageView>
    var rating = 0f
    private var aceptChange: Boolean = false
    private var review: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentConfirmationBinding.inflate(inflater, container, false)

        stars = listOf(
            binding.star1,
            binding.star2,
            binding.star3,
            binding.star4,
            binding.star5
        )

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUp()

        stars.forEachIndexed { index, star ->
            star.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    val isLeft = event.x < v.width / 2
                    rating = if (isLeft) index + 0.5f else index + 1f
                    updateStars(rating)
                }
                true
            }
        }


        updateStars(rating)

        binding.btAddValoration.setOnClickListener {
            review = binding.etReview.text.toString()
            aceptChange = true
            dismiss()
        }
        binding.btClearValoration.setOnClickListener {
            rating = 0f
            review = ""
            binding.etReview.setText("")
            updateStars(rating)
        }
        binding.btNotErase.setOnClickListener {
            aceptChange = false
            dismiss()
        }


    }
    private fun updateStars(rating: Float) {
        for (i in stars.indices) {
            val value = i + 1
            when {
                rating >= value -> stars[i].setImageResource(R.drawable.star_valoration)
                rating >= value - 0.5f -> stars[i].setImageResource(R.drawable.half_star_valoration)
                else -> stars[i].setImageResource(R.drawable.not_filled_star)
            }
        }
    }
    private fun setUp(){
        binding.apply {
            if(isFav){
                tvExplicationQuestion.text = "¿Eliminar de mi lista?"

                addContainer.visibility = View.GONE
                btClearValoration.visibility = View.GONE

                btAddValoration.setText("Eliminar")
            }
            else{
                tvExplicationQuestion.text = "¿Añadir a mi lista?"

                addContainer.visibility = View.VISIBLE
                btClearValoration.visibility = View.VISIBLE

                btAddValoration.setText("Añadir")

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        val extraInfo = UserMovieExtraInfo(
            ownVote = (rating*2).toInt(),
            ownVoteDate = LocalDate.now().toString(),
            userReview = review.valueOrNoReview
        )

        onDismissResult(aceptChange, extraInfo)
    }

}