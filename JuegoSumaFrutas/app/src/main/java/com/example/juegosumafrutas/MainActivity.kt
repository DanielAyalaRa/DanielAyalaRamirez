package com.example.juegosumafrutas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.juegosumafrutas.clases.Imagen
import com.example.juegosumafrutas.clases.Jugador
import com.example.juegosumafrutas.clases.Podio
import com.example.juegosumafrutas.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    val baseRemota = FirebaseFirestore.getInstance().collection("jugadores")
    var avatar = ArrayList<Imagen>()
    var vectorJugador = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTitle("FrutiSumas")
        supportActionBar?.setIcon(R.mipmap.ic_launcher)
        // Agregamos icono y titulo a la barra de accion
        avatarsLlenar()

        binding.jugar.setOnClickListener {
            var nombre = binding.txtJugador.text.toString()

            if (nombre.equals("")) {
                mensaje("Ingrese su nombre por favor")
                return@setOnClickListener
            } // Validamos que ingrese un nombre


            var otraVentana = Intent(this,Juego::class.java)
            otraVentana.putExtra("nombre",nombre)
            otraVentana.putExtra("imagen",avatar[0].imagen)
            binding.txtJugador.setText("")
            // Agregamos las variables que se mandarian al otro activity
            // nombre del usuario y su avatar
            startActivity(otraVentana)
            // Inicializamos la segunda ventana
        }

        baseRemota.addSnapshotListener { query, error ->
            var vector = ArrayList<Podio>()
            if (error != null) {
                mensaje(error.message!!)
                return@addSnapshotListener
            }
            vectorJugador.clear()

            for (documento in query!!) {
                var jugador = Jugador(this)
                jugador.nombre = documento.getString("jugador").toString()
                jugador.puntuacion = documento.getLong("puntos").toString().toInt()

                vector.add(Podio(jugador.nombre,jugador.puntuacion))
            }
            vector.sortByDescending {
                it.puntuacion
            }// ordenamos de mayor a menos

            (0..vector.size-1).forEach {
                vectorJugador.add("#${it+1}, Puntos: ${vector.get(it).puntuacion}, Jugador: ${vector.get(it).jugador}")
            }
            // lo pasamos como cadena y ya ordenado el ListView
            binding.mejorjugador.adapter = ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,vectorJugador)
        }
    }

    override fun onResume() {
        super.onResume()
        avatarsLlenar()
    }

    private fun avatarAleatorio(lista : ArrayList<Imagen>) {
        lista.shuffle()
    }

    private fun avatarsLlenar() {
        avatar.add(Imagen(R.drawable.brocoli))
        avatar.add(Imagen(R.drawable.elote))
        avatar.add(Imagen(R.drawable.fresa))
        avatar.add(Imagen(R.drawable.limon))
        avatar.add(Imagen(R.drawable.manzana))
        avatar.add(Imagen(R.drawable.naranja))
        avatar.add(Imagen(R.drawable.pina))
        avatar.add(Imagen(R.drawable.platano))
        avatar.add(Imagen(R.drawable.sandia))
        avatar.add(Imagen(R.drawable.uva))
        avatarAleatorio(avatar)
        // Hacemos aleatorio la seleccion de imagenes
        binding.frutaUsuario.setImageResource(avatar[0].imagen)
        // Tomamos el primer elemento ya randomizado
    }

    private fun mensaje (m : String) {
        Toast.makeText(this,m,Toast.LENGTH_LONG).show()
    }
}