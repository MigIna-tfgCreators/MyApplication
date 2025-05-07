package com.example.myapplication.classes.modules.main.view.top

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.classes.models.API.PeliculaModel
import com.example.myapplication.classes.models.main.MainEvents
import com.example.myapplication.classes.modules.main.recyclerView.AdapterPeliculas
import com.example.myapplication.classes.modules.main.recyclerView.ClickItemInterface
import com.example.myapplication.classes.modules.main.viewmodel.PeliculasViewModel
import com.example.myapplication.databinding.FragmentTopBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class TopFragment : Fragment() {

    private lateinit var binding: FragmentTopBinding
    private lateinit var adapter: AdapterPeliculas
    private val viewModel: PeliculasViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopBinding.inflate(inflater, container, false)

        binding.rvPeliculasTop.layoutManager = GridLayoutManager(requireContext(),3)
        adapter = AdapterPeliculas(
            listaPeliculas = viewModel.uiState.value.actualList,
            clickInterface = object : ClickItemInterface{
                override fun onFilmClick(pelicula: PeliculaModel?) {
                    val bundle = Bundle().apply {
                        putSerializable(getString(R.string.bundle_film),pelicula)
                    }
                    viewModel.addEvent(MainEvents.Detalle, bundle)
                }
            }
        )
        binding.rvPeliculasTop.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.viewModelScope.launch {
            viewModel.uiState.collect { state ->
                adapter.actualizarLista(state.actualList)
            }
        }

    }
}