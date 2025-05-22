package com.example.myapplication.classes.modules.main.cartelera.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.classes.extensions.textChanges
import com.example.myapplication.classes.models.API.Movie
import com.example.myapplication.classes.modules.main.activity.view.AdapterMovies
import com.example.myapplication.classes.modules.main.activity.view.ClickItemInterface
import com.example.myapplication.classes.modules.main.cartelera.model.NowPlayingEvents
import com.example.myapplication.classes.modules.main.cartelera.viewmodel.NowPlayingViewModel
import com.example.myapplication.classes.modules.main.detalles.view.MovieDetailsFragment
import com.example.myapplication.classes.providers.EndlessRecyclerOnScrollListener
import com.example.myapplication.databinding.FragmentNowPlayingBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NowPlayingFragment : Fragment() {

    private lateinit var binding: FragmentNowPlayingBinding
    private lateinit var adapter: AdapterMovies
    private val viewModel: NowPlayingViewModel by viewModel()
    private lateinit var endlessScrollListener: EndlessRecyclerOnScrollListener


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNowPlayingBinding.inflate(inflater, container, false)

        binding.rvWatchedMovies.layoutManager = GridLayoutManager(requireContext(), 3)
        adapter = AdapterMovies(
            movieList = emptyList(),
            clickInterface = object: ClickItemInterface {
                override fun onFilmClick(movie: Movie) {
                    val bottomSheet = MovieDetailsFragment(movie.movieId)
                    bottomSheet.show(parentFragmentManager, bottomSheet.tag)
                }
            }
        )
        binding.rvWatchedMovies.adapter = adapter

        val layoutManager = binding.rvWatchedMovies.layoutManager as GridLayoutManager

        endlessScrollListener = object: EndlessRecyclerOnScrollListener(layoutManager, visibleThreshold = 6){
            override fun onLoadMore() {
                if(!viewModel.moviesState.value.isLoading) {
                    binding.progressMovieList.visibility = View.VISIBLE
                    if(viewModel.moviesState.value.isSearchMode)
                        viewModel.addEventFilms(NowPlayingEvents.ShowSearchedList(viewModel.moviesState.value.actualQuery))
                    else
                        viewModel.addEventFilms(NowPlayingEvents.ShowList)
                }
            }

        }
        binding.rvWatchedMovies.clearOnScrollListeners()
        binding.rvWatchedMovies.addOnScrollListener(endlessScrollListener)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.addEventFilms(NowPlayingEvents.ShowList)

        viewModel.viewModelScope.launch {
            viewModel.moviesState.collect { state ->
                val movieList = state.actualFilms
                binding.rvWatchedMovies.post {
                    Log.d("NowPlayingFragment", "Actual films size: ${state.actualFilms.size}")
                    adapter.updateList(movieList)
                    binding.progressMovieList.visibility = View.GONE
                }
            }
        }

        viewModel.viewModelScope.launch {
            binding.etFiltrarCartelera.textChanges().debounce(500).map { it.trim() }.distinctUntilChanged().collect { text ->
                if(text.length >= 3){
                    viewModel.addEventFilms(NowPlayingEvents.ShowSearchedList(text))
                }else{
                    endlessScrollListener.resetState()
                    viewModel.addEventFilms(NowPlayingEvents.ResetAll)
                    delay(500)
                    viewModel.addEventFilms(NowPlayingEvents.ShowList)
                }
            }
        }
    }
}