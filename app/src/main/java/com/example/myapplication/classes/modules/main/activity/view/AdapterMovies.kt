package com.example.myapplication.classes.modules.main.activity.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator
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

            updateProgressColor(circularProgress, circularProgress.progress)

            cvMovie.setOnClickListener {
                clickInterface.onFilmClick(movie)
            }

            val isFav = moviesSavedList.contains(movie.movieId)
            setFavCheckView(favCheck, movie, isFav)
        }
    }

    override fun getItemCount() = movieList.size

    fun updateProgressColor(progressView: CircularProgressIndicator, progress: Double) {
        progressView.setProgress(progress, 10.0)

        val color = when {
            progress >= 7.0 -> ContextCompat.getColor(progressView.context, R.color.lightGreen)
            progress >= 5.0 -> ContextCompat.getColor(progressView.context, R.color.stateYellow)
            progress > 3.0 -> ContextCompat.getColor(progressView.context, R.color.stateOrange)
            else -> ContextCompat.getColor(progressView.context, R.color.stateRed)
        }

        progressView.progressColor = color
        progressView.dotColor = color
        progressView.textColor = color

    }

    private fun setFavCheckView(favCheckView: ImageView, selectedMovie: Movie, isFav: Boolean) {
        favCheckView.setImageResource(
            if (isFav) R.drawable.filled_small_star else R.drawable.plus_circle
        )
        favCheckView.setOnClickListener {
            val dialog = ConfirmationFragment(isFav){ confirmed, extraInfo ->
                if (confirmed) {
                    clickInterface.onCheckClick(selectedMovie,extraInfo)

                    if (isFav) {
                        R.drawable.plus_circle
                    }
                    else {
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