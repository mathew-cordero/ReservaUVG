package com.example.reservauvg.Auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.reservauvg.Navegacion.Navigation
import com.example.reservauvg.R

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }
        setContentView(R.layout.activity_login)
        val iniciarSesion:Button = findViewById(R.id.botoninicio)
        iniciarSesion.setOnClickListener {
            //vamos a la navegacion
            val intent = Intent(this@Login, Navigation::class.java)
            startActivity(intent)
        }
    }
}