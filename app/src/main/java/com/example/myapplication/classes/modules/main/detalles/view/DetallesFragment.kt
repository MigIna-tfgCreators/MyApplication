package com.example.myapplication.classes.modules.main.detalles.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.classes.modules.main.detalles.model.DetailsEvent
import com.example.myapplication.classes.modules.main.detalles.model.DetailsState
import com.example.myapplication.classes.modules.main.detalles.viewmodel.DetallesViewModel
import com.example.myapplication.classes.services.network.API
import com.example.myapplication.configurations.extractClaveYoutube
import com.example.myapplication.databinding.FragmentDetallesBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetallesFragment : Fragment() {

    private lateinit var binding: FragmentDetallesBinding
    private val viewModel: DetallesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetallesBinding.inflate(inflater, container, false)

        val id = arguments?.getInt(getString(R.string.bundle_film))
        Log.d("Identificador1.5","${id.toString()} -> $id")

        viewModel.addDetailsEvent(DetailsEvent.mostrarDetalle(id ?: 0))
        viewModel.addDetailsEvent(DetailsEvent.mostrarCreditos(id ?: 0))
        viewModel.addDetailsEvent(DetailsEvent.mostrarTrailer(id ?: 0))


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.viewModelScope.launch {
            viewModel.movie.collect { state ->
                if(state?.actualCredits != null && state.actualFilm != null && state.youtubeUrl != null){
                    binding.progressBar.visibility = View.GONE
                    setUI(state)
                }
                else
                    binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    fun setUI(elemento: DetailsState?){
        binding.apply {
            val movie = elemento?.actualFilm
            val credits = elemento?.actualCredits
            val url = elemento?.youtubeUrl

            tvTituloDetalle.setText(movie?.title)
            tvDescripcionDetalle.setText(movie?.overview)

            val nombresGeneros = movie?.genres?.joinToString(", "){ it.name }
            tvGeneroDetalle.setText("Genero(s): $nombresGeneros")

            Glide.with(requireContext())
                .load("${API.BASE_URL_IMAGEN}${movie?.posterPath}")
                .into(ivPosterDetalle)

            val reparto = credits?.cast?.take(3)?.joinToString("\n"){
                "${it.nombre} como ${it.personaje}"
            }
            tvReparto.setText(reparto)

            val director = credits?.crew?.firstOrNull{ it.trabajo == "Director"}
            val guionista = credits?.crew?.firstOrNull{ it.trabajo == "Writer" || it.trabajo == "Screenplay"}
            director.let {
                tvDirector.setText("Director ${it?.nombre}")
            }
            guionista.let {
                tvGuionista.setText("Guionista: ${it?.nombre}")
            }

            Log.d("Identificador","URL -> $url")

            youtubePlayerView.addYouTubePlayerListener(object: AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(url?.extractClaveYoutube ?: "Trailer no disponible",0f)
                }
            })

        }
    }
}