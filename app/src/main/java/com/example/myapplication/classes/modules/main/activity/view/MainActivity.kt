package com.example.myapplication.classes.modules.main.activity.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.NavigationUI
import com.example.myapplication.R
import com.example.myapplication.classes.managers.navHostFragment
import com.example.myapplication.classes.modules.main.activity.viewmodel.MoviesMainViewModel
import com.example.myapplication.classes.modules.main.activity.model.MainEvents
import com.example.myapplication.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MoviesMainViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        navHostFragment?.let {
            NavigationUI.setupWithNavController(binding.navBottom,it.navController)
        }
        menuListener()

    }

    fun menuListener(){

        binding.navBottom.setOnItemSelectedListener { menuItem->

            when(menuItem.itemId){
                R.id.btNowPlaying -> {
                    binding.tvTitle.text = getString(R.string.now_playing_title)
                    viewModel.addEventNavigation(MainEvents.NowPlayingList)
                    true
                }
                R.id.btTop -> {
                    binding.tvTitle.text = getString(R.string.top_title)
                    viewModel.addEventNavigation(MainEvents.TopList)
                    true
                }
                R.id.btSearch -> {
                    binding.tvTitle.text = getString(R.string.search_title)
                    viewModel.addEventNavigation(MainEvents.SearchList)
                    true
                }
                R.id.btFavorites -> {
                    binding.tvTitle.text = getString(R.string.favorites_title)
                    viewModel.addEventNavigation(MainEvents.FavoritesList)
                    true
                }
                R.id.btProfile -> {
                    //viewModel.addEventNavegation(MainEvents.Perfil, null)
                    true
                }
                else -> false
            }
        }
    }

}