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
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.classes.models.API.Pelicula
import com.example.myapplication.classes.modules.main.cartelera.model.CarteleraEvents
import com.example.myapplication.classes.modules.main.cartelera.viewmodel.CarteleraViewModel
import com.example.myapplication.databinding.FragmentCarteleraBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CarteleraFragment : Fragment() {

    private lateinit var binding: FragmentCarteleraBinding
    private lateinit var adapter: AdapterPeliculas
    private val viewModel: CarteleraViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCarteleraBinding.inflate(inflater, container, false)

        binding.rvPeliculasCartelera.layoutManager = GridLayoutManager(requireContext(), 3)
        adapter = AdapterPeliculas(
            listaPeliculas = emptyList(),
            clickInterface = object : ClickItemInterface {
                override fun onFilmClick(pelicula: Pelicula?) {
                    Log.d("Identificador", "${pelicula?.id}")
                    val bundle = Bundle().apply {
                        putInt(getString(R.string.bundle_film), pelicula?.id ?: 0)
                    }
                    Log.d("Identificador2", "${bundle.getInt(getString(R.string.bundle_film))}")
                    viewModel.addEventFilms(CarteleraEvents.viajeDetalle, bundle)
                    Toast.makeText(requireContext(), pelicula?.nombrePelicula, Toast.LENGTH_SHORT).show()
                }
            }
        )
        binding.rvPeliculasCartelera.adapter = adapter

        binding.rvPeliculasCartelera.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItem = layoutManager.itemCount
                val lastVisible = layoutManager.findLastVisibleItemPosition()

                if(totalItem <= lastVisible+6){
                    binding.progressMovieList.visibility = View.VISIBLE
                    viewModel.addEventFilms(CarteleraEvents.mostrarListado, null)
                }

            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.addEventFilms(CarteleraEvents.mostrarListado, null)

        viewModel.viewModelScope.launch {
            viewModel.movies.collect { state ->
                adapter.actualizarLista(state)
                Log.d("Listado", "${state?.size}")
                binding.progressMovieList.visibility = View.GONE
            }
        }

        binding.etFiltrarCartelera.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()

                if(query.isBlank()) {
                    viewModel.addEventFilms(CarteleraEvents.resetearListado, null)
                }
                else
                    viewModel.addEventFilms(CarteleraEvents.actualizarListado(query), null)
            }

        })
    }
}