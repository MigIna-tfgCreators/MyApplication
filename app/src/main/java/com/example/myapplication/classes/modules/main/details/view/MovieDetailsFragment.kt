package com.example.myapplication.classes.modules.main.details.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.classes.modules.main.details.model.DetailsEvent
import com.example.myapplication.classes.modules.main.details.model.DetailsState
import com.example.myapplication.classes.modules.main.details.viewmodel.DetailsViewModel
import com.example.myapplication.classes.extensions.extractClaveYoutube
import com.example.myapplication.classes.extensions.valueOrEmpty
import com.example.myapplication.classes.extensions.valueOrNoReview
import com.example.myapplication.classes.extensions.valueOrZero
import com.example.myapplication.databinding.FragmentMovieDetailsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailsFragment(
    private val movieId: Int,
    private val isPersonalMovie: Boolean
): BottomSheetDialogFragment() {

    private lateinit var binding: FragmentMovieDetailsBinding
    private val viewModel: DetailsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        viewModel.addDetailsEvent(DetailsEvent.ShowDetails(movieId))
        viewModel.addDetailsEvent(DetailsEvent.ShowCredits(movieId))
        viewModel.addDetailsEvent(DetailsEvent.ShowTrailer(movieId))
        if(isPersonalMovie)
            viewModel.addDetailsEvent(DetailsEvent.ShowPersonalData(movieId))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.viewModelScope.launch {
            viewModel.movie.collect { state ->

                binding.progressBar.visibility = if(state.isLoading) View.VISIBLE else View.GONE

                if(state.error != null)
                    state.error.let { errorMsg ->
                        AlertDialog.Builder(requireContext())
                            .setTitle(getString(R.string.error_type))
                            .setMessage(errorMsg)
                            .setPositiveButton(R.string.general_ok) { dialog, _ -> dialog.dismiss() }
                            .show()

                        viewModel.addDetailsEvent(DetailsEvent.ClearError)
                    }

                if (isPersonalMovie) {
                    if (state.actualFilm != null && state.extraInfo != null) {
                        setUI(state)
                    }
                } else {
                    if (state.actualCredits != null && state.actualFilm != null && state.youtubeVideo.videoKey != null) {
                        setUI(state)
                    }
                }

            }
        }

        binding.btBackDetails.setOnClickListener {
            dismiss()
        }

    }

    fun setUI(state: DetailsState?){
        binding.apply {
            val movie = state?.actualFilm
            val credits = state?.actualCredits
            val video = state?.youtubeVideo

            tvMovieTitleDetails.setText(movie?.movieTitle)

            Glide.with(requireContext())
                .load("${BuildConfig.BASE_IMAGE_URL}${movie?.moviePoster}")
                .into(ivMoviePosterDetails)

            val url = video.let {
                "https://www.youtube.com/watch?v=${it?.videoKey}"
            }
            Log.d("Identificador","URL -> $url")

            youtubePlayerView.addYouTubePlayerListener(object: AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(url.extractClaveYoutube.valueOrEmpty,0f)
                }
            })

            val genresNames = movie?.movieGenres?.joinToString(", "){ it.genreName }


            if(!isPersonalMovie){
                tvMovieGenreDetails.setText("${getString(R.string.genres_text_details)} $genresNames")
                starContainer.visibility = View.GONE
                tvMovieDescriptionDetails.setText(movie?.movieDescription)

                val cast = credits?.cast?.take(3)?.joinToString("\n"){
                    "${it.nameCast} como ${it.characterCast}"
                }
                tvMovieCast.setText(cast)

                val director = credits?.crew?.firstOrNull{ it.jobCrew == getString(R.string.director)}
                val writer = credits?.crew?.firstOrNull{ it.jobCrew == getString(R.string.writer) || it.jobCrew == getString(R.string.screenplay)}
                director.let {
                    tvDirector.setText(
                        if(director!= null)
                            "${getString(R.string.director_points)}  ${it?.nameCrew}"
                        else
                            "${getString(R.string.director_points)} desconocido"
                    )
                }
                writer.let {
                    tvScreenWriter.setText(
                        if (writer!= null)
                            "${getString(R.string.screenwriter_points)}  ${it?.nameCrew}"
                        else
                            "${getString(R.string.screenwriter_points)} desconocido"
                    )
                }
            }
            else{
                val information = viewModel.movie.value.extraInfo

                information.apply {
                    tvMovieGenreDetails.setText("Reseña: ${this?.userReview.valueOrNoReview}")
                    tvMovieDescriptionDetails.setText("${getString(R.string.genres_text_details)} $genresNames")
                    tvScreenWriter.setText("La añadiste a tu lista el  ${this?.ownVoteDate}")
                }
                val linearLayout = tvMovieGenreDetails.parent as ViewGroup
                linearLayout.removeView(tvMovieGenreDetails)
                linearLayout.addView(tvMovieGenreDetails, linearLayout.indexOfChild(tvMovieDescriptionDetails) + 1)

                tvDirector.visibility = View.GONE
                tvMovieCast.visibility = View.GONE
                starContainer.visibility = View.VISIBLE
                val list = listOf(star1,star2,star3, star4, star5)
                updateStars(information?.ownVote?.toFloat().valueOrZero, list)

                var flagChecked = false

                btEditData.setOnClickListener {
                    if(!flagChecked){

                    }
                    else{

                    }
                    flagChecked = true
                }

            }

        }
    }
    private fun updateStars(rating: Float, stars: List<ImageView>) {
        Log.d("STARSRATE",rating.toString())
        val valoration = rating/2
        for (i in stars.indices) {
            val star = stars[i]
            val position = i + 1

            when {
                valoration >= position -> {
                    star.setImageResource(R.drawable.star_valoration)
                }
                valoration >= position - 0.5 -> {
                    star.setImageResource(R.drawable.half_star_valoration)
                }
                else -> {
                    star.setImageResource(R.drawable.not_filled_star)
                }
            }
        }
    }
}