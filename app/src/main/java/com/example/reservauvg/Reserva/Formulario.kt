package com.example.reservauvg.Reserva

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.example.reservauvg.R
import com.example.reservauvg.components.DatePickerFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Formulario : AppCompatActivity() {
    private lateinit var fechatextView: TextInputEditText
    private var fechaagendada:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getSupportActionBar() != null) {
            getSupportActionBar()?.show();
            getSupportActionBar()?.setDisplayShowHomeEnabled(true);
            getSupportActionBar()?.setIcon(R.drawable.rulogo_decolor); // Reemplaza "tu_logo" con el nombre de tu archivo de imagen
        }
        setContentView(R.layout.activity_formulario)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setIcon(R.drawable.rulogo_decolor)

        //vamos a llamar a los widgets
        val iconbtncalendar: ImageView = findViewById(R.id.form_fecha_icon)
        val iconbtnhrinicio: ImageView = findViewById(R.id.form_hora_inicio_icon)
        val iconbtnhrfinal:ImageView = findViewById(R.id.form_hora_final_icon)
        fechatextView = findViewById(R.id.form_fecha)

        //vamos a hacer los setonclick de las imagenes
        iconbtncalendar.setOnClickListener {
            showDatePickerDialog()
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

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    private fun onDateSelected(year: Int, month: Int, day: Int) {

        var daymodify: String = day.toString()
        var monthmodify:String = (month+1).toString()

        if(day<=9){
            daymodify = "0$daymodify"
        }
        if((month+1)<=9){
            monthmodify = "0$monthmodify"
        }
        val formattedDate = "$year-$monthmodify-$daymodify"
        // Convierte la cadena en un objeto Editable
        val editableText = Editable.Factory.getInstance().newEditable(formattedDate)
        // Establece el texto en el TextInputEditText
        fechatextView.text = editableText
    }
}