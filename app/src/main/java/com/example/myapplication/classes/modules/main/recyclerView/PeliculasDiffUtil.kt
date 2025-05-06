package com.example.myapplication.classes.modules.main.recyclerView

import androidx.recyclerview.widget.DiffUtil
import com.example.myapplication.classes.models.API.PeliculaModel

class PeliculasDiffUtil(
    private val listaAntigua: List<PeliculaModel>,
    private val listaNueva: List<PeliculaModel>
): DiffUtil.Callback() {
    override fun getOldListSize() = listaAntigua.size

    override fun getNewListSize() = listaNueva.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return listaAntigua[oldItemPosition].id == listaNueva[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return listaAntigua[oldItemPosition] == listaNueva[newItemPosition]
    }
}