package com.example.myapplication.classes.modules.main.activity.model

sealed class MainEvents {
    object NowPlayingList: MainEvents()
    object TopList: MainEvents()
    object SearchList: MainEvents()
    object FavoritesList: MainEvents()
}