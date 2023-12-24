package com.example.reservauvg.backend

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.reservauvg.Constants.REQUEST_GOOGLE_PLAY_SERVICES
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.DateTime
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pub.devrel.easypermissions.EasyPermissions
import java.io.IOException

class API_Calendar(val contextprovider: Context) {

    private var mCredential: GoogleAccountCredential? = null
    private var mService: Calendar? = null
    init {
        initCredentials()

    }
    fun setNameAccount(name:String){
        mCredential!!.selectedAccountName = name
    }

    fun googleaccountcredential():GoogleAccountCredential?{
        return mCredential
    }

    fun getNameAccount():String?{
        return mCredential!!.selectedAccountName
    }

    private fun initCredentials() {
        mCredential = GoogleAccountCredential.usingOAuth2(
            contextprovider,
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

    fun setDataFromCalendar(sumarry:String,description:String,horainicio:String,horafinal:String,fecha:String,calendarioid:String):String?{
        try {
            Log.d("Google proc", "se comenzó")
            val event = Event()
                .setSummary(sumarry)
                .setLocation("Ciudad de Guatemala, Universidad del Valle")
                .setDescription(description)
            val startDateTime = DateTime(fecha+"T"+horainicio+"-06:00")
            val start = EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("America/Guatemala")
            event.start = start
            val endDateTime = DateTime(fecha+"T"+horafinal+"-06:00")
            val end = EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("America/Guatemala")
            event.end = end

            //devolvemos la informacion
                val insertado = mService!!.events().insert(calendarioid,event).execute()
                Log.d("Google proc", "Evento insertado: ${insertado.htmlLink}")
                var myinfo = insertado.id
                myinfo= myinfo+","+insertado.htmlLink
                return myinfo //este es el efento insertado.


        } catch (e: UserRecoverableAuthIOException) {

            Log.d("Google error 2", e.message.toString())
            Log.d("Google error 2", e.stackTraceToString())
            return "nulo"
        } catch (e: IOException) {
            Log.d("Google error", e.message.toString())
            Log.d("Google error", e.stackTraceToString())
            return "nulo"
        }

    }

}