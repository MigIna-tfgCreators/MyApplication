package com.example.myapplication.classes.modules.main.top.view

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.classes.extensions.textChanges
import com.example.myapplication.classes.extensions.valueOrFalse
import com.example.myapplication.classes.models.API.Movie
import com.example.myapplication.classes.models.firebase.UserMovieExtraInfo
import com.example.myapplication.classes.modules.main.activity.model.GeneralMovieState
import com.example.myapplication.classes.modules.main.activity.model.MainEvents
import com.example.myapplication.classes.modules.main.activity.view.AdapterMovies
import com.example.myapplication.classes.modules.main.activity.view.ClickItemInterface
import com.example.myapplication.classes.modules.main.activity.viewmodel.MoviesMainViewModel
import com.example.myapplication.classes.modules.main.details.view.MovieDetailsFragment
import com.example.myapplication.classes.modules.main.now_playing.model.NowPlayingEvents
import com.example.myapplication.classes.modules.main.profile.model.ProfileEvents
import com.example.myapplication.classes.modules.main.profile.model.ProfileState
import com.example.myapplication.classes.modules.main.top.model.TopRatedEvents
import com.example.myapplication.classes.modules.main.top.viewmodel.TopRatedViewModel
import com.example.myapplication.classes.providers.EndlessRecyclerOnScrollListener
import com.example.myapplication.databinding.FragmentTopBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TopFragment : Fragment() {

    private lateinit var binding: FragmentTopBinding
    private lateinit var adapter: AdapterMovies
    private lateinit var endlessScrollListener: EndlessRecyclerOnScrollListener
    private lateinit var bottomSheet: MovieDetailsFragment
    private val viewModel: TopRatedViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopBinding.inflate(inflater, container, false)

        binding.rvTopMovies.layoutManager = GridLayoutManager(requireContext(), 3)
        adapter = AdapterMovies(
            movieList = viewModel.moviesState.value.actualMovies,
            clickInterface = object: ClickItemInterface {
                override fun onFilmClick(movie: Movie) {
                    viewModel.viewModelScope.launch {
                        viewModel.addEvent(TopRatedEvents.CheckMovie(movie))

                        val state = viewModel.moviesState.first { it.isInPersonalList != null }

                        val isFav = state.isInPersonalList

                        bottomSheet = MovieDetailsFragment(movie.movieId, isFav.valueOrFalse)
                        bottomSheet.show(parentFragmentManager, bottomSheet.tag)

                        viewModel.addEvent(TopRatedEvents.ResetAll)
                    }
                }

                override fun onCheckClick(movie: Movie, extraInfo: UserMovieExtraInfo?) {
                    viewModel.viewModelScope.launch {
                        viewModel.addEvent(TopRatedEvents.HasInPersonal(movie, extraInfo))
                    }
                }
            }
        )
        binding.rvTopMovies.adapter = adapter

        val layoutManager = binding.rvTopMovies.layoutManager as GridLayoutManager
        endlessScrollListener = object : EndlessRecyclerOnScrollListener(layoutManager, visibleThreshold = 6) {
            override fun onLoadMore() {
                if (!viewModel.moviesState.value.isLoading) {
                    if (viewModel.moviesState.value.isSearchMode == true) {
                        viewModel.addEvent(
                            TopRatedEvents.ShowSearchedList(
                                viewModel.moviesState.value.actualQuery.orEmpty()
                            )
                        )
                    }
                    else
                        viewModel.addEvent(TopRatedEvents.ShowAllList)
                }
            }
        }
        binding.rvTopMovies.clearOnScrollListeners()
        binding.rvTopMovies.addOnScrollListener(endlessScrollListener)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.addEvent(TopRatedEvents.ShowAllList)

        viewModel.viewModelScope.launch {
            viewModel.moviesState.collect { state ->
                val movieList = state.actualMovies
                val personalList = state.actualPersonalMovies
                showError(state)
                binding.rvTopMovies.post {
                    Log.d("TOP_LIST_DEBUG", "Personales: ${personalList?.size}")
                    adapter.updateList(movieList)
                    adapter.setSaved(personalList?.mapNotNull { it.movieId }?.toSet() ?: emptySet())
                }
            }
        }

        viewModel.viewModelScope.launch {
            binding.etFilterTop.textChanges().debounce(500).map { it.trim() }.distinctUntilChanged()
                .collect { text ->
                    if (text.length >= 3) {
                        viewModel.addEvent(TopRatedEvents.ShowSearchedList(text))
                    } else {
                        endlessScrollListener.resetState()
                        viewModel.addEvent(TopRatedEvents.ResetAll)
                        delay(500)
                        viewModel.addEvent(TopRatedEvents.ShowAllList)
                    }
                }
        }
    }

    private fun showError(state: GeneralMovieState){
        state.error?.let { error ->
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.error_type))
                .setMessage(error)
                .setPositiveButton(R.string.general_ok) { dialog, _ ->
                    dialog.dismiss()
                    viewModel.addEvent(TopRatedEvents.ResetAll)
                    viewModel.addEvent(TopRatedEvents.ShowAllList)
                }
                .show()
        }
    }
}