package com.example.reservauvg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.example.reservauvg.Auth.Login

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.splashscreen)

        //CREAMOS LA ANIMACION
        val animacion = AnimationUtils.loadAnimation(this,R.anim.desplazamientoarriba)
        val animacio2 = AnimationUtils.loadAnimation(this,R.anim.desplazamientoabajo)
        val imagenlogo:ImageView = findViewById(R.id.logo)
        val imagentitulo:ImageView = findViewById(R.id.titulo)

        imagenlogo.setAnimation(animacio2)
        imagentitulo.setAnimation(animacion)

        Handler().postDelayed(object : Runnable {
            override fun run() {
                // Código que se ejecutará después del retardo
                val intent = Intent(this@MainActivity, Login::class.java)
                startActivity(intent)
                finish() // Cierra la actividad actual para que no vuelva cuando presiones el botón de retroceso
            }
        }, 2000)


    }
}