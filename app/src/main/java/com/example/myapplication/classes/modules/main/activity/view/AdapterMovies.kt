package com.example.myapplication.classes.modules.main.activity.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.classes.extensions.valueOrZero
import com.example.myapplication.classes.models.API.Movie
import com.example.myapplication.databinding.ItemRvMovieBinding

class AdapterMovies(
    private var movieList: List<Movie>,
    private val clickInterface: ClickItemInterface
): RecyclerView.Adapter<MyViewHolder>(){

    fun updateList(newList: List<Movie>?){
        if(newList != null)
            movieList = newList
        notifyDataSetChanged()
    }

    private fun getItem(position: Int) = movieList[position]


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemRvMovieBinding = ItemRvMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie = getItem(position)

        with(holder.binding) {
            holder.setUpImage(movie)

            circularProgress.maxProgress = BuildConfig.MAX_RATING
            circularProgress.setCurrentProgress(movie.movieAverageVote?.toDouble().valueOrZero)

            cvMovie.setOnClickListener {
                clickInterface.onFilmClick(movie)
            }
        }
    }

    override fun getItemCount() = movieList.size

}

class MyViewHolder(val binding: ItemRvMovieBinding): RecyclerView.ViewHolder(binding.root){

    fun setUpImage(movie: Movie){
        if(movie.moviePoster.isNullOrEmpty()){
            Glide.with(itemView.context)
                .load(R.drawable.empty_poster)
                .into(binding.ivPoster)
        }
        else {
            val url = "${BuildConfig.BASE_IMAGE_URL}${movie.moviePoster}"
            Glide.with(itemView.context)
                .load(url)
                .into(binding.ivPoster)
        }
    }

}

interface ClickItemInterface {
    fun onFilmClick(movie: Movie)
}