package com.example.myapplication.classes.modules.main.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.classes.models.API.PeliculaModel
import com.example.myapplication.classes.services.network.API
import com.example.myapplication.databinding.ItemRvPeliculasBinding

class ApadterPeliculas(
    var listaPeliculas: List<PeliculaModel>
): RecyclerView.Adapter<MyViewHolder>(){

    fun actualizarLista(nuevaLista: List<PeliculaModel>){
        val peliculasDiff = PeliculasDiffUtil(listaPeliculas, nuevaLista)
        val result = DiffUtil.calculateDiff(peliculasDiff)
        listaPeliculas = nuevaLista
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemRvPeliculasBinding = ItemRvPeliculasBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val pelicula = listaPeliculas[position]
        with(holder.binding){
            Glide.with(ivPoster.context)
                .load("${API.BASE_URL_IMAGEN}${pelicula.poster}")
                .apply(RequestOptions().override(API.IMAGEN_ANCHO, API.IMAGEN_ALTO))
                .into(ivPoster)

            circularProgress.maxProgress = API.MAX_CALIFICATION
            circularProgress.setCurrentProgress(pelicula.votoPromedio.toDouble())

            cvPelicula.setOnClickListener {

            }

        }
    }

    override fun getItemCount() = listaPeliculas.size

}



class MyViewHolder(val binding: ItemRvPeliculasBinding): RecyclerView.ViewHolder(binding.root)