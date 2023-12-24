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
import kotlinx.coroutines.cancel
import pub.devrel.easypermissions.EasyPermissions

class Formulario : AppCompatActivity() {
    private lateinit var fechatextView: TextInputEditText
    private lateinit var horainitextView:TextInputEditText
    private lateinit var horafintextView:TextInputEditText
    private var fechaagendada:String = ""
    private lateinit var apicalendario:API_Calendar
    private lateinit var textcarnet:TextInputEditText
    private lateinit var textpersonas:TextInputEditText
    private lateinit var idcalendario:String
    var mProgress: ProgressDialog? = null

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

        val bundle:Bundle? = intent.extras
        idcalendario = bundle?.getString("idcalendar")!!
        //declaramos la variable apicalendario
        apicalendario = API_Calendar(this)


        //vamos a llamar a los widgets
        val iconbtncalendar: ImageView = findViewById(R.id.form_fecha_icon)
        val iconbtnhrinicio: ImageView = findViewById(R.id.form_hora_inicio_icon)
        val iconbtnhrfinal:ImageView = findViewById(R.id.form_hora_final_icon)
        val botonreservar:Button = findViewById(R.id.button_asignar)
        horainitextView = findViewById(R.id.form_hora_inicio)
        horafintextView = findViewById(R.id.form_hora_final)
        fechatextView = findViewById(R.id.form_fecha)
        textcarnet = findViewById(R.id.form_carnet)
        textpersonas = findViewById(R.id.form_nombre)
        //vamos a hacer los setonclick de las imagenes
        iconbtncalendar.setOnClickListener {
            showDatePickerDialog()
        }

        iconbtnhrinicio.setOnClickListener {
            showTimePickerDialog(true)
        }

        iconbtnhrfinal.setOnClickListener {
            showTimePickerDialog(false)
        }
        botonreservar.setOnClickListener {
            getResultsFromApi()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_GOOGLE_PLAY_SERVICES -> if (resultCode != Activity.RESULT_OK) {
                // vamos a mostrar dialogo "This app requires Google Play Services. Please install " + "Google Play Services on your device and relaunch this app."
            } else {
                getResultsFromApi()
            }
            REQUEST_ACCOUNT_PICKER -> if (resultCode == Activity.RESULT_OK && data != null) {
                val prefs = getSharedPreferences(getString(R.string.prefsfile), Context.MODE_PRIVATE)
                val accountName:String? = prefs.getString("email", null)
                if (accountName != null) {
                    val settings = this.getPreferences(Context.MODE_PRIVATE)
                    val editor = settings?.edit()
                    editor?.putString(PREF_ACCOUNT_NAME, accountName)
                    editor?.apply()
                    apicalendario.setNameAccount(accountName)
                    getResultsFromApi()
                }
            }
            REQUEST_AUTHORIZATION -> if (resultCode == Activity.RESULT_OK) {
                getResultsFromApi()
            }
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

    private fun showTimePickerDialog(horaseleccionada:Boolean){
        val timePicker:TimePickerFragment
        //si es verdadero entonces se selecciona la hora inicial
        if(horaseleccionada){
            timePicker = TimePickerFragment { onTimeSelectedinicial(it) }
        }
        //caso contrario se usara la hora final.
        else{
            timePicker = TimePickerFragment { onTimeSelectedfinal(it) }
        }
        timePicker.show(supportFragmentManager, "timePicker")

    }

    private fun onTimeSelectedinicial(it: String) {
        val formattedhora = "$it"
        val editableText = Editable.Factory.getInstance().newEditable(formattedhora)
        horainitextView.text = editableText
    }

    private fun onTimeSelectedfinal(it: String){
        val formattedhora = "$it"
        val editableText = Editable.Factory.getInstance().newEditable(formattedhora)
        horafintextView.text = editableText
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

    fun showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode: Int) {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val dialog = apiAvailability.getErrorDialog(
            this,
            connectionStatusCode,
            Constants.REQUEST_GOOGLE_PLAY_SERVICES
        )
        dialog?.show()
    }

    //cuenta que verifica google playservices.
    private fun isGooglePlayServicesAvailable(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this)
        return connectionStatusCode == ConnectionResult.SUCCESS
    }
    //metodo que verifica si tenes cuenta de googleservices
    private fun acquireGooglePlayServices() {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode)
        }
    }

    //metodo que verifica si estas online
    private fun isDeviceOnline(): Boolean {
        val connMgr =
            this?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun getResultsFromApi() {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices()
        } else if (!isDeviceOnline()) {
            showAlertoffline()
        } else if (apicalendario.getNameAccount() == null) {
            chooseAccount()
        }else {
            makeRequestTask()
        }
    }

    private fun chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS
            )
        ) {
            val accountName = this.getPreferences(Context.MODE_PRIVATE)
                ?.getString(PREF_ACCOUNT_NAME, null)
            if (accountName != null) {
                apicalendario.setNameAccount(accountName)
                getResultsFromApi()
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                    apicalendario.googleaccountcredential()!!.newChooseAccountIntent(),
                    REQUEST_ACCOUNT_PICKER
                )
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                this,
                "This app needs to access your Google account (via Contacts).",
                REQUEST_PERMISSION_GET_ACCOUNTS,
                Manifest.permission.GET_ACCOUNTS
            )
        }
    }

    private fun showAlertoffline() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error falta de coneccion")
        builder.setMessage("Se ha producido un error no hay conexion online")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun makeRequestTask() {
        var mLastError: Exception? = null
        lifecycleScope.executeAsyncTask(
            onStart = {
                mProgress!!.show()
            },doInBackground = {
                try {
                    val prefs = getSharedPreferences(getString(R.string.prefsfile), Context.MODE_PRIVATE)
                    val accountEmail:String? = prefs.getString("email", null)
                    val accountName:String? = prefs.getString("user",null)
                    val sumarry = "RESERVA SALON $accountEmail"
                    val carnet = textcarnet.text.toString()
                    val personas = textpersonas.text.toString()
                    val horainicio = horainitextView.text.toString()
                    val horafinal = horafintextView.text.toString()
                    val fecha = fechatextView.text.toString()
                    val description = "El estudiante $accountName [carnet: $carnet correo:$accountEmail] reservo el salon $accountName para $personas"
                    apicalendario.setDataFromCalendar(sumarry = sumarry, description = description, horainicio = horainicio, horafinal = horafinal, fecha = fecha, calendarioid = idcalendario )
                } catch (e: Exception) {
                    mLastError = e
                    lifecycleScope.cancel()
                    null
                }
            },onPostExecute = { output ->
                mProgress!!.hide()

            },onCancelled = {
                mProgress!!.hide()
                if (mLastError != null) {
                    if (mLastError is GooglePlayServicesAvailabilityIOException) {
                        showGooglePlayServicesAvailabilityErrorDialog(
                            (mLastError as GooglePlayServicesAvailabilityIOException)
                                .connectionStatusCode
                        )
                    } else if (mLastError is UserRecoverableAuthIOException) {
                        this.startActivityForResult(
                            (mLastError as UserRecoverableAuthIOException).intent,
                            REQUEST_AUTHORIZATION
                        )
                    } else { //muestra el error
                            //"The following error occurred:\n" + mLastError!!.message
                    }
                } else { //dice si la request fue cancellada.
                    //Request cancelled
                }
            }
        )
    }


}