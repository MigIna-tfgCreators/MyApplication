package com.example.myapplication.classes.modules.main.personal.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.classes.extensions.textChanges
import com.example.myapplication.classes.models.API.Movie
import com.example.myapplication.classes.models.firebase.UserMovieExtraInfo
import com.example.myapplication.classes.modules.main.activity.view.AdapterMovies
import com.example.myapplication.classes.modules.main.activity.view.ClickItemInterface
import com.example.myapplication.classes.modules.main.details.view.MovieDetailsFragment
import com.example.myapplication.classes.modules.main.now_playing.model.NowPlayingEvents
import com.example.myapplication.classes.modules.main.personal.model.PersonalListEvents
import com.example.myapplication.classes.modules.main.personal.viewmodel.PersonalListViewModel
import com.example.myapplication.classes.providers.EndlessRecyclerOnScrollListener
import com.example.myapplication.databinding.FragmentMoviePersonalBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PersonalFragment: Fragment() {

    private lateinit var binding: FragmentMoviePersonalBinding
    private lateinit var adapter: AdapterMovies
    private val viewModel: PersonalListViewModel by viewModel()
    private lateinit var endlessScrollListener: EndlessRecyclerOnScrollListener


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoviePersonalBinding.inflate(layoutInflater, container, false)

        binding.rvWatchedMovies.layoutManager = GridLayoutManager(requireContext(),3)

        adapter = AdapterMovies(
            movieList = viewModel.moviesState.value.actualMovies,
            clickInterface = object: ClickItemInterface{
                override fun onFilmClick(movie: Movie) {
                    val bottomSheet = MovieDetailsFragment(movie.movieId, true)
                    bottomSheet.show(parentFragmentManager, bottomSheet.tag)
                }

                override fun onCheckClick(movie: Movie, extraInfo: UserMovieExtraInfo?) {
                    movie.let {
                        viewModel.viewModelScope.launch {
                            viewModel.addEventPersonal(PersonalListEvents.HasInPersonal(movie, extraInfo))
                        }
                    }
                }

            }
        )
        binding.rvWatchedMovies.adapter = adapter

        val layoutManager = binding.rvWatchedMovies.layoutManager as GridLayoutManager


        endlessScrollListener = object: EndlessRecyclerOnScrollListener(layoutManager, visibleThreshold = 6){
            override fun onLoadMore() {
                if(!viewModel.moviesState.value.isLoading) {
                    binding.progressPersonalList.visibility = View.VISIBLE
                    if(viewModel.moviesState.value.isSearchMode == true)
                        viewModel.addEventPersonal(PersonalListEvents.ShowSearchedList(viewModel.moviesState.value.actualQuery.toString()))
                    else
                        viewModel.addEventPersonal(PersonalListEvents.ShowPersonalList)

                    binding.progressPersonalList.visibility = View.GONE
                }
            }

        }
        binding.rvWatchedMovies.clearOnScrollListeners()
        binding.rvWatchedMovies.addOnScrollListener(endlessScrollListener)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.addEventPersonal(PersonalListEvents.ShowPersonalList)


        viewModel.viewModelScope.launch {
            viewModel.moviesState.collect { state ->
                val personalList = state.actualPersonalMovies
                binding.rvWatchedMovies.post {
                    Log.d("AYUDA PO FAVO","${personalList?.size}")
                    adapter.updateList(personalList)
                    adapter.setSaved(personalList?.mapNotNull { it.movieId }?.toSet() ?: emptySet())
                    binding.progressPersonalList.visibility = View.GONE
                }
            }
        }


        viewModel.viewModelScope.launch {
            binding.etPersonalListFilter.textChanges().debounce(500).map { it.trim() }.distinctUntilChanged().collect { text ->
                if(text.length >= 3){
                    viewModel.addEventPersonal(PersonalListEvents.ShowSearchedList(text))
                }else{
                    endlessScrollListener.resetState()
                    viewModel.addEventPersonal(PersonalListEvents.ResetAll)
                    delay(500)
                    viewModel.addEventPersonal(PersonalListEvents.ShowPersonalList)
                }
            }
        }

    }

}