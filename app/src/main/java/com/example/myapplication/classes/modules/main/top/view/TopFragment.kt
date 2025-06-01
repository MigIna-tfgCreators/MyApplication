package com.example.myapplication.classes.modules.main.top.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.classes.extensions.textChanges
import com.example.myapplication.classes.models.API.Movie
import com.example.myapplication.classes.models.firebase.UserMovieExtraInfo
import com.example.myapplication.classes.modules.main.activity.view.AdapterMovies
import com.example.myapplication.classes.modules.main.activity.view.ClickItemInterface
import com.example.myapplication.classes.modules.main.details.view.MovieDetailsFragment
import com.example.myapplication.classes.modules.main.top.model.TopRatedEvents
import com.example.myapplication.classes.modules.main.top.viewmodel.TopRatedViewModel
import com.example.myapplication.classes.providers.EndlessRecyclerOnScrollListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TopFragment : Fragment() {

    private val viewModel: TopRatedViewModel by viewModel()

    private lateinit var etFilterTop: EditText
    private lateinit var rvPeliculasTop: RecyclerView
    private lateinit var adapter: AdapterMovies
    private lateinit var endlessScrollListener: EndlessRecyclerOnScrollListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_top, container, false)

        etFilterTop = view.findViewById(R.id.etFilterTop)
        rvPeliculasTop = view.findViewById(R.id.rvPeliculasTop)

        rvPeliculasTop.layoutManager = GridLayoutManager(requireContext(), 3)
        adapter = AdapterMovies(
            movieList = viewModel.moviesState.value.actualMovies,
            clickInterface = object : ClickItemInterface {
                override fun onFilmClick(movie: Movie) {
                    val bottomSheet = MovieDetailsFragment(movie.movieId)
                    bottomSheet.show(parentFragmentManager, bottomSheet.tag)
                }

                override fun onCheckClick(movie: Movie, extraInfo: UserMovieExtraInfo?) {
                    viewModel.viewModelScope.launch {
                        viewModel.addEvent(TopRatedEvents.HasInPersonal(movie, extraInfo))
                    }
                }
            }
        )
        rvPeliculasTop.adapter = adapter

        val layoutManager = rvPeliculasTop.layoutManager as GridLayoutManager
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
                }
            }
        }
        rvPeliculasTop.clearOnScrollListeners()
        rvPeliculasTop.addOnScrollListener(endlessScrollListener)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.addEvent(TopRatedEvents.ShowAllList)

        viewModel.viewModelScope.launch {
            viewModel.moviesState.collect { state ->
                val movieList = state.actualMovies
                val personalList = state.actualPersonalMovies
                rvPeliculasTop.post {
                    Log.d("TOP_LIST_DEBUG", "Personales: ${personalList?.size}")
                    adapter.updateList(movieList)
                    adapter.setSaved(personalList?.mapNotNull { it.movieId }?.toSet() ?: emptySet())
                }
            }
        }

        viewModel.viewModelScope.launch {
            etFilterTop.textChanges()
                .debounce(500)
                .map { it.trim() }
                .distinctUntilChanged()
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
}
