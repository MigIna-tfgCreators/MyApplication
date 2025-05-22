package com.example.myapplication.classes.providers

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


abstract class EndlessRecyclerOnScrollListener(
    private val layoutManager: RecyclerView.LayoutManager,
    private val visibleThreshold: Int = 4
): RecyclerView.OnScrollListener() {

    private var previousTotal = 0
    private var loading = true
    private var lastVisibleItem = 0
    private var totalItemCount = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        totalItemCount = layoutManager.itemCount
        lastVisibleItem = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

        if (loading && totalItemCount > previousTotal) {
            loading = false
            previousTotal = totalItemCount

        } else if (!loading && totalItemCount > 0 && lastVisibleItem + visibleThreshold >= totalItemCount) {
            onLoadMore()
            loading = true
        }
    }

    fun resetState() {
        previousTotal = 0
        lastVisibleItem = 0
        totalItemCount = 0
        loading = true
    }

    abstract fun onLoadMore()
}