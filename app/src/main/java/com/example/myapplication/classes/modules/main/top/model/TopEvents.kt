package com.example.myapplication.classes.modules.main.top.model


sealed class TopEvents {
    object showAllList: TopEvents()
    object ResetAll: TopEvents()
    data class ShowSearchedList(val query: String): TopEvents()
}
