package com.example.myapplication.classes.modules.main.cartelera.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.BuildConfig
import com.example.myapplication.classes.models.API.Pelicula
import com.example.myapplication.classes.services.api.API
import com.example.myapplication.databinding.ItemRvPeliculasBinding

class AdapterPeliculas(
    private var listaPeliculas: List<Pelicula>?,
    private val clickInterface: ClickItemInterface?
): RecyclerView.Adapter<MyViewHolder>(){

    fun actualizarLista(nuevaLista: List<Pelicula>?){
        listaPeliculas = nuevaLista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemRvPeliculasBinding = ItemRvPeliculasBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val pelicula = listaPeliculas?.get(position)
        with(holder.binding){
            Glide.with(ivPoster.context)
                .load("${BuildConfig.BASE_URL_IMAGEN}${pelicula?.poster}")
                .apply(RequestOptions().override(API.IMAGEN_ANCHO, API.IMAGEN_ALTO))
                .into(ivPoster)

            circularProgress.maxProgress = API.MAX_CALIFICATION
            circularProgress.setCurrentProgress(pelicula?.votoPromedio?.toDouble() ?: 0.0)

            cvPelicula.setOnClickListener {
                clickInterface?.onFilmClick(pelicula)
            }
        }
    }

    override fun getItemCount() = listaPeliculas?.size ?: 0

}

class MyViewHolder(val binding: ItemRvPeliculasBinding): RecyclerView.ViewHolder(binding.root)

interface ClickItemInterface {
    fun onFilmClick(pelicula: Pelicula?)
}