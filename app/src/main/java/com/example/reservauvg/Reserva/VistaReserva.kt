package com.example.reservauvg.Reserva

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.reservauvg.Aulas.Salon
import com.example.reservauvg.Navegacion.Navigation
import com.example.reservauvg.R
import com.google.firebase.storage.FirebaseStorage

class VistaReserva : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getSupportActionBar() != null) {
            getSupportActionBar()?.show();
            getSupportActionBar()?.setDisplayShowHomeEnabled(true);
            getSupportActionBar()?.setIcon(R.drawable.rulogo_decolor); // Reemplaza "tu_logo" con el nombre de tu archivo de imagen
        }
        setContentView(R.layout.activity_vista_reserva)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setIcon(R.drawable.rulogo_decolor)

        val bundle:Bundle? = intent.extras

        val salonMostrar: Salon = Salon(bundle?.getString("nombre")!!,bundle?.getBoolean("disponibilidad")!!,bundle?.getString("imagen")!!,bundle?.getString("link")!!,bundle?.getString("idcalendar")!!,bundle?.getString("ubicacion")!!,bundle?.getString("imagenubicacion")!!)

        //instanciar los widgets
        val reserva_boton:Button = findViewById(R.id.aula_boton_reservar)
        val imagen_aula:ImageView = findViewById(R.id.aula_image)
        val titulo_aula:TextView = findViewById(R.id.aula_titulo)
        val ubicacion_aula:TextView = findViewById(R.id.aula_direccion)
        val web_aula:WebView = findViewById(R.id.aula_web)


        //ahora vamos a configurarlos
        titulo_aula.text = salonMostrar.nombre
        ubicacion_aula.text = salonMostrar.ubicacion
        //configuramos la imagen del aula
        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.getReferenceFromUrl(salonMostrar.imagenubicacion)
        storageReference.downloadUrl
            .addOnSuccessListener {uri ->
                // Utilizar Glide (o cualquier otra biblioteca de carga de imágenes) para cargar y mostrar la imagen
                Glide.with(this@VistaReserva)
                    .load(uri)
                    .into(imagen_aula)

            }.addOnFailureListener {

            }

        //vamos a mostrar el contenido en nuestro webview
        web_aula.webViewClient = WebViewClient()
        web_aula.webChromeClient = WebChromeClient()
        web_aula.settings.javaScriptEnabled = true

        //vamos a obtener el link
        val prefs = getSharedPreferences(getString(R.string.prefsfile), Context.MODE_PRIVATE)
        val tokenuser:String? = prefs.getString("token",null)
        val idcalendario:String = salonMostrar.idcalendar
        val calendarUrl = "https://calendar.google.com/calendar/embed?src=${idcalendario}&ctz=America/Guatemala&mode=AGENDA&token=$tokenuser"
        web_aula.loadUrl(calendarUrl)

        reserva_boton.setOnClickListener {
            val intent = Intent(this@VistaReserva, Formulario::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Manejar el clic en el botón de retroceso
                onBackPressed() // Esta línea invoca el comportamiento predeterminado de retroceso
                return true
            }
            // Agregar más casos según sea necesario
            else -> return super.onOptionsItemSelected(item)
        }
    }
}