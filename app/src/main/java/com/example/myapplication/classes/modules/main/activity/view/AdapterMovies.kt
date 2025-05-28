package com.example.myapplication.classes.modules.main.activity.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.classes.extensions.valueOrZero
import com.example.myapplication.classes.models.API.Movie
import com.example.myapplication.classes.models.firebase.UserMovieExtraInfo
import com.example.myapplication.databinding.ItemRvMovieBinding

class AdapterMovies(
    private var movieList: List<Movie>,
    private val clickInterface: ClickItemInterface
): RecyclerView.Adapter<MyViewHolder>(){

    val moviesSavedList = mutableSetOf<Int>()

    fun updateList(newList: List<Movie>?){
        if(newList != null)
            movieList = newList
        notifyDataSetChanged()
    }
    fun setSaved(saved: Set<Int>){
        moviesSavedList.clear()
        moviesSavedList.addAll(saved)
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

            val isFav = moviesSavedList.contains(movie.movieId)
            setFavCheckView(favCheck, movie, isFav)
        }
    }

    override fun getItemCount() = movieList.size

    private fun setFavCheckView(favCheckView: ImageView, selectedMovie: Movie, isFav: Boolean) {
        favCheckView.setImageResource(
            if (isFav) R.drawable.filled_small_star else R.drawable.plus_circle
        )
        favCheckView.setOnClickListener {
            val dialog = ConfirmationFragment(isFav){ confirmed, extraInfo ->
                if (confirmed) {
                    clickInterface.onCheckClick(selectedMovie,extraInfo)

                    if (isFav) {
                        Log.d("AYUDA","DEBERIAS SER CIRCULO")
                        R.drawable.plus_circle
                    }
                    else {
                        Log.d("AYUDA","DEBERIAS SER ESTRELLA")
                        R.drawable.filled_small_star
                    }
                    notifyDataSetChanged()
                }
            }

            val activity = favCheckView.context as? androidx.fragment.app.FragmentActivity
            activity?.supportFragmentManager?.let {
                dialog.show(it, "CONFIRM_ADD_DIALOG")
            }
        }
    }

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
    fun onCheckClick(movie: Movie, extraInfo: UserMovieExtraInfo?)
}