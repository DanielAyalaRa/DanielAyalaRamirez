package com.example.juegosumafrutas.clases

import android.content.Context
import com.example.juegosumafrutas.R

class Jugador (puntero : Context){
    var p = puntero
    var nombre = ""
    var imagen = R.drawable.manzana
    var puntuacion = 0
    var vidas = 3
    var nivel = "Nivel 1 - Sumas Básicas"

    fun contenido() : String {
        return "Jugador: ${nombre}, Puntuación: ${puntuacion}"
    }
}