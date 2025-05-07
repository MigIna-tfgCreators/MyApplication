package com.example.myapplication.classes.modules.main.recyclerView

import androidx.recyclerview.widget.DiffUtil
import com.example.myapplication.classes.models.API.PeliculaModel

class PeliculasDiffUtil(
    private val listaAntigua: List<PeliculaModel>?,
    private val listaNueva: List<PeliculaModel>?
): DiffUtil.Callback() {
    override fun getOldListSize(): Int{
        return if(listaAntigua != null)
            listaAntigua.size
        else
            0
    }

    override fun getNewListSize(): Int{
        return if(listaNueva != null)
            listaNueva.size
        else
            0
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return listaAntigua?.get(oldItemPosition)?.id == listaAntigua?.get(newItemPosition)?.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return listaAntigua?.get(oldItemPosition) == listaNueva?.get(newItemPosition)
    }
}