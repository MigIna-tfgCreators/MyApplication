package com.example.myapplication.classes.components

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.classes.models.API.Genre
import com.google.android.material.chip.Chip

class MyCustomChip @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.google.android.material.R.attr.chipStyle
): Chip(context, attrs, defStyleAttr) {

    private var appliedList: List<Int>? = null
    private var actualGenre: Genre? = null
    private var isGenreType: Boolean = false

    init {
        isCheckable = true
        isClickable = true
        isFocusable = true

    }

    fun bind(genre: Genre, appliedList: List<Int>){
        isGenreType = true
        this.actualGenre = genre
        this.appliedList = appliedList

        text = actualGenre?.genreName
        id = actualGenre?.genreId ?: 0
    }

    fun setListenersChecked(apply: Boolean?){

        if(apply!=null) isChecked = apply

        if(isChecked){
            setChipBackgroundColorResource(R.color.lightBlue)
            setTextColor(ContextCompat.getColor(context, R.color.white))
        }
        else{
            setChipBackgroundColorResource(R.color.secundaryCardText)
            setTextColor(ContextCompat.getColor(context, R.color.black))
        }
    }

    fun clear(){
        setListenersChecked(false)
    }

    val existsSection: Boolean
        get() = appliedList?.any { it == id } ?: false

    val isGenre: Boolean
        get() = isGenreType

}