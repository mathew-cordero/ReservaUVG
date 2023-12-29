package com.example.reservauvg.Reserva

import android.Manifest
import android.accounts.AccountManager
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.reservauvg.Aulas.GetEventModel
import com.example.reservauvg.Constants.PREF_ACCOUNT_NAME
import com.example.reservauvg.Constants.REQUEST_ACCOUNT_PICKER
import com.example.reservauvg.Constants.REQUEST_AUTHORIZATION
import com.example.reservauvg.Constants.REQUEST_GOOGLE_PLAY_SERVICES
import com.example.reservauvg.Constants.REQUEST_PERMISSION_GET_ACCOUNTS
import com.example.reservauvg.R
import com.example.reservauvg.components.DatePickerFragment
import com.example.reservauvg.components.TimePickerFragment
import com.example.reservauvg.databinding.FragmentReservarBinding
import com.example.reservauvg.util.executeAsyncTask
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.textfield.TextInputEditText
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.DateTime
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withContext
import pub.devrel.easypermissions.EasyPermissions
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ReservarFragment : Fragment() {
    // TODO: Rename and change types of parameters


    private var mCredential: GoogleAccountCredential? = null //hesabımıza erişim için
    private var mService: Calendar? = null //Takvime erişim için
    var mProgress: ProgressDialog? = null
    private lateinit var horainitextView:TextInputEditText
    private lateinit var horafintextView:TextInputEditText
    private lateinit var fechatextView: TextInputEditText
    private lateinit var textcarnet:TextInputEditText
    private lateinit var textpersonas:TextInputEditText
    private lateinit var textnombre:TextInputEditText
    private lateinit var idcalendario: String
    private lateinit var nombreaula:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initCredentials()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_reservar, container, false)
        idcalendario = activity?.intent?.extras?.getString("idcalendar")!!
        nombreaula = activity?.intent?.extras?.getString("nombre")!!

        initView(view)
        return view
    }

    private fun initCredentials() {
        mCredential = GoogleAccountCredential.usingOAuth2(
            requireContext(),
            arrayListOf(CalendarScopes.CALENDAR)
        )
            .setBackOff(ExponentialBackOff())
        initCalendarBuild(mCredential)
    }
    private fun initCalendarBuild(credential: GoogleAccountCredential?) {
        val transport = AndroidHttp.newCompatibleTransport()
        val jsonFactory = GsonFactory.getDefaultInstance()
        mService = Calendar.Builder(
            transport, jsonFactory, credential
        )
            .setApplicationName("ReservaUVG")
            .build()
    }

    private fun initView(view:View) {
        mProgress = ProgressDialog(requireContext())
        mProgress!!.setMessage("Loading...")
        val buttonAsignar:Button = view.findViewById(R.id.button_asignar)
        horainitextView = view.findViewById(R.id.form_hora_inicio)
        horafintextView = view.findViewById(R.id.form_hora_final)
        fechatextView = view.findViewById(R.id.form_fecha)
        val iconbtncalendar: ImageView = view.findViewById(R.id.form_fecha_icon)
        val iconbtnhrinicio: ImageView = view.findViewById(R.id.form_hora_inicio_icon)
        val iconbtnhrfinal: ImageView = view.findViewById(R.id.form_hora_final_icon)
        textcarnet = view.findViewById(R.id.form_carnet)
        textpersonas = view.findViewById(R.id.form_nombre)
        textnombre = view.findViewById(R.id.form_nombre)
        buttonAsignar.setOnClickListener {
            buttonAsignar.isEnabled = false
            getResultsFromApi()
            buttonAsignar.isEnabled = true
        }

        iconbtncalendar.setOnClickListener {
            showDatePickerDialog()
        }

        iconbtnhrinicio.setOnClickListener {
            showTimePickerDialog(true)
        }

        iconbtnhrfinal.setOnClickListener {
            showTimePickerDialog(false)
        }
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(requireFragmentManager(), "datePicker")
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

    private fun showTimePickerDialog(horaseleccionada:Boolean){
        val timePicker: TimePickerFragment
        //si es verdadero entonces se selecciona la hora inicial
        if(horaseleccionada){
            timePicker = TimePickerFragment { onTimeSelectedinicial(it) }
        }
        //caso contrario se usara la hora final.
        else{
            timePicker = TimePickerFragment { onTimeSelectedfinal(it) }
        }
        timePicker.show(requireFragmentManager(), "timePicker")

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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_GOOGLE_PLAY_SERVICES -> if (resultCode != Activity.RESULT_OK) {
                showAler("This app requires Google Play Services. Please install " + "Google Play Services on your device and relaunch this app.")
            } else {
                getResultsFromApi()
            }
            REQUEST_ACCOUNT_PICKER -> if (resultCode == Activity.RESULT_OK && data != null &&
                data.extras != null
            ) {
                val accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
                if (accountName != null) {
                    val settings = this.activity?.getPreferences(Context.MODE_PRIVATE)
                    val editor = settings?.edit()
                    editor?.putString(PREF_ACCOUNT_NAME, accountName)
                    editor?.apply()
                    mCredential!!.selectedAccountName = accountName
                    getResultsFromApi()
                }
            }
            REQUEST_AUTHORIZATION -> if (resultCode == Activity.RESULT_OK) {
                getResultsFromApi()
            }
        }
    }

    private fun getResultsFromApi() {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices()
        } else if (mCredential!!.selectedAccountName == null) {
            chooseAccount()
        } else if (!isDeviceOnline()) {
           "No network connection available."
        } else {
            makeRequestTask()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    //Google consolea erişim izni olup olmadıgına bakıyoruz
    private fun isGooglePlayServicesAvailable(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(requireContext())
        return connectionStatusCode == ConnectionResult.SUCCESS
    }

    //Cihazın Google play servislerini destekleyip desteklemediğini kontrol ediyor
    private fun acquireGooglePlayServices() {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(requireContext())
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode)
        }
    }

    fun showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode: Int) {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val dialog = apiAvailability.getErrorDialog(
            this,
            connectionStatusCode,
            REQUEST_GOOGLE_PLAY_SERVICES
        )
        dialog?.show()
    }

    private fun isDeviceOnline(): Boolean {
        val connMgr =
            this.activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun chooseAccount() {
        if (EasyPermissions.hasPermissions(
                requireContext(), Manifest.permission.GET_ACCOUNTS
            )
        ) {
            val accountName = this.activity?.getPreferences(Context.MODE_PRIVATE)
                ?.getString(PREF_ACCOUNT_NAME, null)
            if (accountName != null) {
                mCredential!!.selectedAccountName = accountName
                getResultsFromApi()
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                    mCredential!!.newChooseAccountIntent(),
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


    private fun makeRequestTask() {
        var mLastError: Exception? = null

        lifecycleScope.executeAsyncTask(
            onStart = {
                mProgress!!.show()
            },
            doInBackground = {
                try {
                    setDataFromCalendar(sumarry = "El salon $nombreaula es reservado por Mathew", description = "El estudiante"+ textnombre.text.toString()+" [carnet: "+textcarnet.text.toString()+"] reservo el salon $nombreaula para "+ textpersonas.text.toString()+" personas", horainicio = horainitextView.text.toString(), horafinal = horafintextView.text.toString(), fecha = fechatextView.text.toString(),idcalendario)
                } catch (e: Exception) {
                    mLastError = e
                    lifecycleScope.cancel()
                    null
                }
            },
            onPostExecute = { output ->
                mProgress!!.hide()
                Log.d("Salon Reservado exitosamente", output!!)
            },
            onCancelled = {
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
                    } else {
                        Log.d("Error","The following error occurred:\n" + mLastError!!.message)


                    }
                } else {
                    Log.d("Error","Request cancelled.")

                }
            }
        )
    }
    fun setDataFromCalendar(sumarry:String,description:String,horainicio:String,horafinal:String,fecha:String,calendarioid:String):String?{
        var myinfo:String
        try {
            Log.d("Google proc", "se comenzó")
            val event = Event()
                .setSummary(sumarry)
                .setLocation("Ciudad de Guatemala, Edificio CIT Universidad del Valle")
                .setDescription(description)
            val startDateTime = DateTime(fecha+"T"+horainicio+":00"+"-06:00")
            val start = EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("America/Guatemala")
            event.start = start

            val endDateTime = DateTime(fecha+"T"+horafinal+":00"+"-06:00")
            val end = EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("America/Guatemala")
            event.end = end
            val insertado = mService!!.events().insert(calendarioid,event).execute()
            Log.d("Google proc", "Evento insertado: ${insertado.htmlLink}")
            var myinfo = insertado.id
            myinfo= myinfo+","+insertado.htmlLink
            return myinfo

        } catch (e: UserRecoverableAuthIOException) {
            Log.d("Google error 2", e.message.toString())
            Log.d("Google error 2", e.stackTraceToString())
            return "nulo"
        } catch (e: IOException) {
            Log.d("Google error", e.message.toString())
            Log.d("Google error", e.stackTraceToString())
            return "nulo"
        }

        return myinfo

    }

    private fun showAler(Alerta:String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error ")
        builder.setMessage(Alerta)
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun getDataFromCalendar(): MutableList<GetEventModel> {
        val now = DateTime(System.currentTimeMillis())
        val eventStrings = ArrayList<GetEventModel>()

        try {
            Log.d("Google proc", "se comenzó")
            val events = mService!!.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute()
            Log.d("Google proc", "Se obtuvieron los eventos")
            val items = events.items
            Log.d("Google proc", "Se creo items")

            for (event in items) {
                var start = event.start.dateTime
                if (start == null) {
                    start = event.start.date
                }

                eventStrings.add(
                    GetEventModel(
                        summary = event.summary,
                        startDate = start.toString()
                    )
                )
            }
            return eventStrings

        } catch (e: UserRecoverableAuthIOException) {
            startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION)
            Log.d("Google error 2", e.message.toString())
            Log.d("Google error 2", e.stackTraceToString())
        } catch (e: IOException) {
            Log.d("Google error", e.message.toString())
            Log.d("Google error", e.stackTraceToString())

        }
        return eventStrings
    }

}