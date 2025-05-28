package com.example.myapplication.classes.modules.main.busqueda.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.classes.extensions.textChanges
import com.example.myapplication.classes.extensions.valueOrEmpty
import com.example.myapplication.classes.models.API.Movie
import com.example.myapplication.classes.models.firebase.UserMovieExtraInfo
import com.example.myapplication.classes.modules.main.activity.view.AdapterMovies
import com.example.myapplication.classes.modules.main.activity.view.ClickItemInterface
import com.example.myapplication.classes.modules.main.busqueda.model.SearchEvents
import com.example.myapplication.classes.modules.main.busqueda.viewmodel.SearchViewModel
import com.example.myapplication.classes.modules.main.detalles.view.MovieDetailsFragment
import com.example.myapplication.classes.providers.EndlessRecyclerOnScrollListener
import com.example.myapplication.databinding.FragmentSearchBinding
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class BusquedaFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: AdapterMovies
    private lateinit var endlessScrollListener: EndlessRecyclerOnScrollListener
    private val viewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)

        adapter = AdapterMovies(
            movieList = emptyList(),
            clickInterface = object: ClickItemInterface{
                override fun onFilmClick(movie: Movie) {
                    val bottomSheet = MovieDetailsFragment(movie.movieId)
                    bottomSheet.show(parentFragmentManager, bottomSheet.tag)
                }

                override fun onCheckClick(movie: Movie, extraInfo: UserMovieExtraInfo?) {
                    movie.let {
                        viewModel.viewModelScope.launch {

                        }
                    }
                }

            }
        )

        binding.recyclerViewMovieList.adapter = adapter
        binding.recyclerViewMovieList.layoutManager = GridLayoutManager(requireContext(), 3)

        val layoutManager = binding.recyclerViewMovieList.layoutManager as GridLayoutManager

        endlessScrollListener = object: EndlessRecyclerOnScrollListener(layoutManager, visibleThreshold = 6) {
            override fun onLoadMore() {
                if(!viewModel.searchState.value.isLoading){
                    binding.progressMovieBar.visibility = View.VISIBLE
                    if(viewModel.searchState.value.isSearchMode == true)
                        viewModel.addEvent(SearchEvents.SearchMovies(viewModel.searchState.value.actualQuery.toString()))
                    else
                        viewModel.addEvent(SearchEvents.GetFilterList)
                }
            }
        }
        binding.recyclerViewMovieList.clearOnScrollListeners()
        binding.recyclerViewMovieList.addOnScrollListener(endlessScrollListener)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.addEvent(SearchEvents.GetListGenres)

        binding.filterButton.setOnClickListener {
            binding.searchEditText.setText("")
            viewModel.viewModelScope.launch {
                val bottomSheet = FiltersFragment(viewModel.searchState.value.genresList, viewModel.searchState.value.genresListApplied,
                    viewModel.searchState.value.order.valueOrEmpty, viewModel.searchState.value.dates.valueOrEmpty,
                    onFilterSelected = { selectedGenres, datesSelected, selectedOrder ->

                        endlessScrollListener.resetState()
                        viewModel.addEvent(SearchEvents.ResetAll)
                        viewModel.addEvent(SearchEvents.ApplyFilters(selectedGenres, datesSelected, selectedOrder))
                        viewModel.addEvent(SearchEvents.GetFilterList)
                    }
                )
                bottomSheet.show(parentFragmentManager, bottomSheet.tag)
            }
        }

        viewModel.viewModelScope.launch {
            viewModel.searchState.collect { result ->
                val movieList = result.actualMovies
                binding.recyclerViewMovieList.post {
                    adapter.updateList(movieList)
                    binding.progressMovieBar.visibility = View.GONE
                }
            }
        }

        viewModel.viewModelScope.launch {
            binding.searchEditText.textChanges().debounce(500).map { it.trim() }.distinctUntilChanged()
                .collect { text ->
                    if(text.length >= 3){
                        viewModel.addEvent(SearchEvents.SearchMovies(text))
                    }else{
                        endlessScrollListener.resetState()
                        viewModel.addEvent(SearchEvents.ClearMovies)
                    }
                }
        }

    }

}