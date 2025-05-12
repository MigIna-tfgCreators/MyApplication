package com.example.myapplication.configurations

val String.userName: String
    get(){ return this.substringBefore("@").firstMayus }

val String.firstMayus: String
    get(){

        val usuario = this.substringBefore("@")
        val delimitadores = listOf('.', '_', '-')

        // Usamos un StringBuilder para reconstruir el string con capitalización por partes
        val resultado = StringBuilder()
        var capitalizar = true

        for (caracter in usuario) {
            if (caracter in delimitadores) {
                capitalizar = true
                resultado.append(caracter)
            } else {
                resultado.append(
                    if (capitalizar) caracter.uppercaseChar()
                    else caracter
                )
                capitalizar = false
            }
        }

        return resultado.toString()
    }
val String.extractClaveYoutube: String
    get() = Regex("v=([^&]+)").find(this)?.groupValues?.get(1) ?: ""
