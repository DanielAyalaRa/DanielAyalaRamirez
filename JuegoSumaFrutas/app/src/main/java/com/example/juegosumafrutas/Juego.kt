package com.example.juegosumafrutas

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.juegosumafrutas.clases.*
import com.example.juegosumafrutas.databinding.ActivityJuegoBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import kotlinx.coroutines.delay as delay

class Juego : AppCompatActivity() {

    lateinit var binding : ActivityJuegoBinding
    val baseRemota = FirebaseFirestore.getInstance().collection("jugadores")
    var jugador = Jugador(this)
    var audio = ArrayList<Audio>()
    var numeros1 = ArrayList<Numero>()
    var numeros2 = ArrayList<Numero>()
    var operadores = ArrayList<Imagen>()
    var respuesta = 0
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJuegoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        llenarAudio()
        mediaPlayer = MediaPlayer.create(this, audio[2].audio)
        mediaPlayer.start()
        mediaPlayer.isLooping = true
        nuevaPartida(jugador)
        nivel1()

        binding.comprobar.setOnClickListener {
            var res = binding.txtRespuesta.text.toString()

            if (res.equals("")) {
                mensaje("NO HA INGRESADO UNA RESPUESTA")
                return@setOnClickListener
            } // Validamos que ingrese una respuesta

            titulosNivel()
            jugar(res)
        }
    }

    private fun nuevaPartida(jugador : Jugador) {
        setTitle(jugador.nivel)
        // Mandamos el nivel en el que se encuentra el jugador

        jugador.nombre = this.intent.extras!!.getString("nombre")!!
        jugador.imagen = this.intent.extras!!.getInt("imagen")!!
        // extraemos el nombre y el avatar del jugador

        binding.txtJugador.setText("Jugador: ${jugador.nombre}")
        binding.avatar.setImageResource(jugador.imagen)
        binding.txtPuntos.setText("Puntos: ${jugador.puntuacion}")
        // Enviamos los datos de la nueva partida

        if (jugador.vidas == 3) {
            binding.vida1.visibility = View.VISIBLE
            binding.vida2.visibility = View.VISIBLE
            binding.vida3.visibility = View.VISIBLE
        } // mostramos las vidas nuevamente

        llenarCartas()
    }

    private fun jugar(res : String) {
        if (jugador.vidas == 0) {
            mediaPlayer.stop()
            alerta("Jugador: ${jugador.nombre}\nTu puntuación es: ${jugador.puntuacion}")

        }
        if (jugador.puntuacion < 9) {
            nivel1()
            if (respuesta == res.toInt()) {
                correcto()
                unPunto()
            } else {
                incorrecto()
                vidaMenos()
            }
        }
        if (jugador.puntuacion >= 9 && jugador.puntuacion < 19) {
            nivel2()
            if (respuesta == res.toInt()) {
                correcto()
                unPunto()
            } else {
                incorrecto()
                vidaMenos()
            }
        }
        if (jugador.puntuacion >= 19 && jugador.puntuacion < 29) {
            nivel3()
            if (respuesta == res.toInt()) {
                correcto()
                unPunto()
            } else {
                incorrecto()
                vidaMenos()
            }
        }
        if (jugador.puntuacion >= 29 && jugador.puntuacion < 39) {
            nivel4()
            if (respuesta == res.toInt()) {
                correcto()
                unPunto()
            } else {
                incorrecto()
                vidaMenos()
            }
        }
        if (jugador.puntuacion >= 39 && jugador.puntuacion < 49) {
            nivel5()
            if (respuesta == res.toInt()) {
                correcto()
                unPunto()
            } else {
                incorrecto()
                vidaMenos()
            }
        }
        if (jugador.puntuacion >= 49) {
            nivel6()
            if (respuesta == res.toInt()) {
                correcto()
                unPunto()
            } else {
                incorrecto()
                vidaMenos()
            }
        }
    }

    private fun vidaMenos() = GlobalScope.launch {
        runOnUiThread {
            binding.txtRespuesta.setText("")
            jugador.vidas--
            if (jugador.vidas == 2) {
                binding.vida3.visibility = View.INVISIBLE
            }
            if (jugador.vidas == 1) {
                binding.vida2.visibility = View.INVISIBLE
            }
            if (jugador.vidas == 0) {
                binding.vida1.visibility = View.INVISIBLE
                mediaPlayer.reset()
                mediaPlayer.stop()
                alerta("Jugador: ${jugador.nombre}\nTu puntuación es: ${jugador.puntuacion}")
                //TODO Guardamos el jugador y puntuacion en Firebase
            }
        }
        delay(20)
    }

    private fun unPunto() = GlobalScope.launch {
        runOnUiThread {
            binding.txtRespuesta.setText("")
            jugador.puntuacion++
            binding.txtPuntos.setText("Puntos: ${jugador.puntuacion}")
        }
        delay(20L)
    }

    private fun correcto() = GlobalScope.launch {
        var mp = MediaPlayer.create(this@Juego,audio[0].audio)
        runOnUiThread {
            mp.start()
        }
        delay(500)
        runOnUiThread {
            mp.reset()
        }
        delay(20)
    }

    private fun incorrecto() = GlobalScope.launch {
        var mp = MediaPlayer.create(this@Juego,audio[1].audio)
        runOnUiThread {
            mp.start()
        }
        delay(500)
        runOnUiThread {
            mp.reset()
        }
        delay(20)
    }

    private fun nivel1() = GlobalScope.launch {
        runOnUiThread {
            numeros1.shuffle()
            numeros2.shuffle()
            var operador = operadores[0].imagen

            var a = numeros1[0].numero
            var b = numeros2[0].numero
            var c = a+b

            while (c > 10) {
                numeros2.shuffle()
                b = numeros2[0].numero
                c = a+b
            }

            respuesta = c
            binding.icono1.setImageResource(numeros1[0].imagen)
            binding.operador.setImageResource(operador)
            binding.icono2.setImageResource(numeros2[0].imagen)
        }
        delay(20L)
    }

    private fun nivel2() = GlobalScope.launch {
        runOnUiThread {
            numeros1.shuffle()
            numeros2.shuffle()
            var operador = operadores[0].imagen

            var a = numeros1[0].numero
            var b = numeros2[0].numero
            var c = a+b

            respuesta = c
            binding.icono1.setImageResource(numeros1[0].imagen)
            binding.operador.setImageResource(operador)
            binding.icono2.setImageResource(numeros2[0].imagen)
        }
        delay(20L)
    }

    private fun nivel3() = GlobalScope.launch {
        runOnUiThread {
            numeros1.shuffle()
            numeros2.shuffle()
            var operador = operadores[1].imagen

            var a = numeros1[0].numero
            var b = numeros2[0].numero
            var c = a-b

            while (b > a) {
                numeros2.shuffle()
                b = numeros2[0].numero
                c = a-b
            }

            respuesta = c
            binding.icono1.setImageResource(numeros1[0].imagen)
            binding.operador.setImageResource(operador)
            binding.icono2.setImageResource(numeros2[0].imagen)
        }
        delay(20L)
    }

    private fun nivel4() = GlobalScope.launch {
        runOnUiThread {
            var rand = (0..1).random()
            if (rand == 0) {
                nivel2()
            }
            if (rand == 1) {
                nivel3()
            }
        }
    }

    private fun nivel5() = GlobalScope.launch {
        runOnUiThread {
            numeros1.shuffle()
            numeros2.shuffle()
            var operador = operadores[2].imagen

            var a = numeros1[0].numero
            var b = numeros2[0].numero
            var c = a*b

            respuesta = c
            binding.icono1.setImageResource(numeros1[0].imagen)
            binding.operador.setImageResource(operador)
            binding.icono2.setImageResource(numeros2[0].imagen)
        }
        delay(20L)
    }

    private fun nivel6() = GlobalScope.launch {
        runOnUiThread {
            var rand = (0..2).random()
            if (rand == 0) {
                nivel2()
            }
            if (rand == 1) {
                nivel3()
            }
            if (rand == 2) {
                nivel5()
            }
        }
    }

    private fun titulosNivel() {
        if (jugador.puntuacion == 9) {
            jugador.nivel = "Nivel 2 - Sumas Moderadas"
            setTitle(jugador.nivel)
            mensaje(jugador.nivel)
        }
        if (jugador.puntuacion == 19) {
            jugador.nivel = "Nivel 3 - Restas"
            setTitle(jugador.nivel)
            mensaje(jugador.nivel)
        }
        if (jugador.puntuacion == 29) {
            jugador.nivel = "Nivel 4 - Sumas y Restas"
            setTitle(jugador.nivel)
            mensaje(jugador.nivel)
        }
        if (jugador.puntuacion == 39) {
            jugador.nivel = "Nivel 5 - Multiplicaciones"
            setTitle(jugador.nivel)
            mensaje(jugador.nivel)
        }
        if (jugador.puntuacion == 49) {
            jugador.nivel = "Nivel 6 - Sumas, Restas y Multiplicaciones"
            setTitle(jugador.nivel)
            mensaje(jugador.nivel)
        }
    }

    private fun llenarCartas() {
        numeros1.add(Numero(R.drawable.cero,0))
        numeros1.add(Numero(R.drawable.uno,1))
        numeros1.add(Numero(R.drawable.dos,2))
        numeros1.add(Numero(R.drawable.tres,3))
        numeros1.add(Numero(R.drawable.cuatro,4))
        numeros1.add(Numero(R.drawable.cinco,5))
        numeros1.add(Numero(R.drawable.seis,6))
        numeros1.add(Numero(R.drawable.siete,7))
        numeros1.add(Numero(R.drawable.ocho,8))
        numeros1.add(Numero(R.drawable.nueve,9))

        numeros2.add(Numero(R.drawable.cero,0))
        numeros2.add(Numero(R.drawable.uno,1))
        numeros2.add(Numero(R.drawable.dos,2))
        numeros2.add(Numero(R.drawable.tres,3))
        numeros2.add(Numero(R.drawable.cuatro,4))
        numeros2.add(Numero(R.drawable.cinco,5))
        numeros2.add(Numero(R.drawable.seis,6))
        numeros2.add(Numero(R.drawable.siete,7))
        numeros2.add(Numero(R.drawable.ocho,8))
        numeros2.add(Numero(R.drawable.nueve,9))

        operadores.add(Imagen(R.drawable.suma))
        operadores.add(Imagen(R.drawable.resta))
        operadores.add(Imagen(R.drawable.multi))
    }

    private fun alerta(mensaje: String) {
        AlertDialog.Builder(this)
            .setTitle("FIN DEL JUEGO")
            .setMessage(mensaje)
            .setPositiveButton("ACEPTAR") { dialog, which ->
                val datos = hashMapOf(
                    "jugador" to jugador.nombre,
                    "puntos" to jugador.puntuacion,
                    "avatar" to jugador.imagen
                ) // Insertamos los datos del usuario

                baseRemota.add(datos)
                    .addOnSuccessListener {
                        mensaje("Jugador Guardado")
                    }
                mediaPlayer.reset()
                mediaPlayer.stop()
                finish()
            }
            .show()
    }

    private fun mensaje(mensaje : String) {
        Toast.makeText(this,mensaje, Toast.LENGTH_LONG).show()
    }

    private fun llenarAudio() {
        audio.add(Audio(R.raw.ding))
        audio.add(Audio(R.raw.error))
        audio.add(Audio(R.raw.musica))
    }
}