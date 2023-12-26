package com.example.reservauvg.Reserva

import android.Manifest
import android.accounts.AccountManager
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.reservauvg.Constants
import com.example.reservauvg.Constants.PREF_ACCOUNT_NAME
import com.example.reservauvg.Constants.REQUEST_ACCOUNT_PICKER
import com.example.reservauvg.Constants.REQUEST_AUTHORIZATION
import com.example.reservauvg.Constants.REQUEST_GOOGLE_PLAY_SERVICES
import com.example.reservauvg.Constants.REQUEST_PERMISSION_GET_ACCOUNTS
import com.example.reservauvg.R
import com.example.reservauvg.backend.API_Calendar
import com.example.reservauvg.components.DatePickerFragment
import com.example.reservauvg.components.TimePickerFragment
import com.example.reservauvg.util.executeAsyncTask
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.EasyPermissions

class Formulario : AppCompatActivity() {
    private lateinit var viewModel:EventoViewModel
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