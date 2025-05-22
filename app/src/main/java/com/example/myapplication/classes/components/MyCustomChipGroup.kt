package com.example.myapplication.classes.components

import android.content.Context
import android.util.AttributeSet
import com.example.myapplication.R
import com.google.android.material.chip.ChipGroup


class MyCustomChipGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ChipGroup(context, attrs, defStyleAttr) {

    fun setDefault(){
        this.clearCheck()
        for (i in 0 until this.childCount) {
            val chip = this.getChildAt(i)
            if (chip is MyCustomChip) chip.clear()
        }
    }
    val hasGenreChips: Boolean
        get() = (0 until childCount).mapNotNull { getChildAt(it) as MyCustomChip }
            .any{ it.isGenre }

    fun applyOrder(orderApplied: String?) {

        if (orderApplied.isNullOrBlank()) return

        for (i in 0 until childCount) {
            val chip = getChildAt(i) as? MyCustomChip ?: continue
            when(orderApplied){
                context.getString(R.string.popularity) -> {
                    if(i == 0){
                        chip.setListenersChecked(true)
                        break
                    }
                }

                context.getString(R.string.release_date) -> {
                    if(i != 0){
                        chip.setListenersChecked(true)
                        break
                    }
                }

                context.getString(R.string.ascender) -> {
                    if(i == 0){
                        chip.setListenersChecked(true)
                        break
                    }
                }
                context.getString(R.string.descender) -> {
                    if(i != 0){
                        chip.setListenersChecked(true)
                        break
                    }
                }
            }
        }
    }
}