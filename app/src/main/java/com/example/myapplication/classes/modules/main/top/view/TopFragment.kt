package com.example.myapplication.classes.modules.main.top.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.classes.models.API.Movie
import com.example.myapplication.classes.modules.main.activity.view.AdapterMovies
import com.example.myapplication.classes.modules.main.activity.view.ClickItemInterface
import com.example.myapplication.classes.modules.main.activity.viewmodel.MoviesMainViewModel
import com.example.myapplication.databinding.FragmentTopBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class TopFragment : Fragment() {

    private lateinit var binding: FragmentTopBinding
    private lateinit var adapter: AdapterMovies
    private val viewModel: MoviesMainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopBinding.inflate(inflater, container, false)

        binding.rvPeliculasTop.layoutManager = GridLayoutManager(requireContext(), 3)
        adapter = AdapterMovies(
            movieList = emptyList(),
            clickInterface = object: ClickItemInterface {
                override fun onFilmClick(movie: Movie) {
                    val bundle = Bundle().apply {
                        putSerializable(getString(R.string.bundle_film), movie)
                    }
                    //viewModel.addEventNavegation(MainEvents.Detalle, bundle)
                }
            }
        )
        binding.rvPeliculasTop.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//        viewModel.(APIEvents.mostrarListadoTop)
//
//        viewModel.viewModelScope.launch {
//            viewModel .collect { state ->
//                adapter.actualizarLista(state.actualList)
//            }
//        }

    }
}