package com.example.myapplication.classes.modules.main.now_playing.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.classes.extensions.textChanges
import com.example.myapplication.classes.extensions.valueOrFalse
import com.example.myapplication.classes.models.API.Movie
import com.example.myapplication.classes.models.firebase.UserMovieExtraInfo
import com.example.myapplication.classes.modules.main.activity.view.AdapterMovies
import com.example.myapplication.classes.modules.main.activity.view.ClickItemInterface
import com.example.myapplication.classes.modules.main.now_playing.model.NowPlayingEvents
import com.example.myapplication.classes.modules.main.now_playing.viewmodel.NowPlayingViewModel
import com.example.myapplication.classes.modules.main.details.view.MovieDetailsFragment
import com.example.myapplication.classes.providers.EndlessRecyclerOnScrollListener
import com.example.myapplication.databinding.FragmentNowPlayingBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NowPlayingFragment : Fragment() {

    private lateinit var binding: FragmentNowPlayingBinding
    private lateinit var adapter: AdapterMovies
    private val viewModel: NowPlayingViewModel by viewModel()
    private lateinit var endlessScrollListener: EndlessRecyclerOnScrollListener
    private lateinit var bottomSheet: MovieDetailsFragment


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNowPlayingBinding.inflate(inflater, container, false)


        adapter = AdapterMovies(
            movieList = viewModel.moviesState.value.actualMovies,
            clickInterface = object: ClickItemInterface{
                override fun onFilmClick(movie: Movie) {
                    viewModel.viewModelScope.launch {
                        viewModel.addEventFilms(NowPlayingEvents.CheckMovie(movie))

                        val state = viewModel.moviesState.first { it.isInPersonalList != null }

                        val isFav = state.isInPersonalList

                        bottomSheet = MovieDetailsFragment(movie.movieId, isFav.valueOrFalse)
                        bottomSheet.show(parentFragmentManager, bottomSheet.tag)

                        viewModel.addEventFilms(NowPlayingEvents.ResetAll)
                    }
                }

                override fun onCheckClick(movie: Movie, extraInfo: UserMovieExtraInfo?) {
                    movie.let {
                        viewModel.viewModelScope.launch {
                            Log.d("TESTEANDOOO","kjhfksdhfsdk")
                            viewModel.addEventFilms(NowPlayingEvents.HasInPersonal(movie, extraInfo))
                        }
                    }
                }

            }
        )

        binding.rvWatchedMovies.adapter = adapter
        binding.rvWatchedMovies.layoutManager = GridLayoutManager(requireContext(), 3)

        val layoutManager = binding.rvWatchedMovies.layoutManager as GridLayoutManager

        endlessScrollListener = object: EndlessRecyclerOnScrollListener(layoutManager, visibleThreshold = 6){
            override fun onLoadMore() {
                if(!viewModel.moviesState.value.isLoading) {
                    binding.progressMovieList.visibility = View.VISIBLE
                    if(viewModel.moviesState.value.isSearchMode == true)
                        viewModel.addEventFilms(NowPlayingEvents.ShowSearchedList(viewModel.moviesState.value.actualQuery.toString()))
                    else
                        binding.progressMovieList.visibility = View.GONE
                }
            }

        }
        binding.rvWatchedMovies.clearOnScrollListeners()
        binding.rvWatchedMovies.addOnScrollListener(endlessScrollListener)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.addEventFilms(NowPlayingEvents.ShowAllList)

        viewModel.viewModelScope.launch {
            viewModel.moviesState.collect { state ->
                val movieList = state.actualMovies
                val personalList = state.actualPersonalMovies
                binding.rvWatchedMovies.post {
                    Log.d("AYUDA PO FAVO","${personalList?.size}")
                    adapter.updateList(movieList)
                    adapter.setSaved(personalList?.mapNotNull { it.movieId }?.toSet() ?: emptySet())
                    binding.progressMovieList.visibility = View.GONE
                }
            }
        }

        viewModel.viewModelScope.launch {
            binding.etNowPlayingFilter.textChanges().debounce(500).map { it.trim() }.distinctUntilChanged().collect { text ->
                if(text.length >= 3){
                    viewModel.addEventFilms(NowPlayingEvents.ShowSearchedList(text))
                }else{
                    endlessScrollListener.resetState()
                    viewModel.addEventFilms(NowPlayingEvents.ResetAll)
                    delay(500)
                    viewModel.addEventFilms(NowPlayingEvents.ShowAllList)
                }
            }
        }
    }
}