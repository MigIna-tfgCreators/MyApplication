package com.example.myapplication.classes.models.main

import androidx.lifecycle.ViewModel


sealed class SelectedCardState {
    object Cartelera: SelectedCardState()
    object Popular: SelectedCardState()
    object Top: SelectedCardState()
}
fun <T: SelectedCardState> ViewModel.handleEvent(event: T, handler: (T) -> Unit){ handler(event) }

val SelectedCardState.toNiceString: String
    get(){

        val resultadoAntes = this.toString().substringBefore("@")
        val resultado = resultadoAntes.substringAfter("$")


        return resultado
    }