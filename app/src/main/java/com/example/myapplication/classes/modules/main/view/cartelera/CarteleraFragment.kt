package com.example.myapplication.classes.modules.main.view.cartelera

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.classes.models.API.PeliculaModel
import com.example.myapplication.classes.models.main.APIEvents
import com.example.myapplication.classes.models.main.MainEvents
import com.example.myapplication.classes.modules.main.recyclerView.AdapterPeliculas
import com.example.myapplication.classes.modules.main.recyclerView.ClickItemInterface
import com.example.myapplication.classes.modules.main.viewmodel.PeliculasViewModel
import com.example.myapplication.databinding.FragmentCarteleraBinding
import com.example.myapplication.databinding.FragmentTopBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class CarteleraFragment : Fragment() {

    private lateinit var binding: FragmentCarteleraBinding
    private lateinit var adapter: AdapterPeliculas
    private val viewModel: PeliculasViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCarteleraBinding.inflate(inflater, container, false)

        binding.rvPeliculasTop.layoutManager = GridLayoutManager(requireContext(),3)
        adapter = AdapterPeliculas(
            listaPeliculas = emptyList(),
            clickInterface = object : ClickItemInterface{
                override fun onFilmClick(pelicula: PeliculaModel?) {
                    val bundle = Bundle().apply {
                        putSerializable(getString(R.string.bundle_film),pelicula)
                    }
                    viewModel.addEventNavegation(MainEvents.Detalle, bundle)
                }
            }
        )
        binding.rvPeliculasTop.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.addEventFilms(APIEvents.mostrarListado)

        viewModel.viewModelScope.launch {
            viewModel.uiState.collect { state ->
                adapter.actualizarLista(state.actualList)
            }
        }

    }

}