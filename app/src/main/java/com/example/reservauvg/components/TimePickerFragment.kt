package com.example.reservauvg.components

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.example.reservauvg.R
import java.util.Calendar

class TimePickerFragment(val listener:(String) -> Unit):DialogFragment(),TimePickerDialog.OnTimeSetListener {

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        var horadeldia:String = hourOfDay.toString()
        var minutoseleccionado:String = minute.toString()

        if(hourOfDay<=9){
            horadeldia = "0$horadeldia"
        }
        if(minute<=9){
           minutoseleccionado = "0$minutoseleccionado"
        }
        listener("$horadeldia:$minutoseleccionado")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val picker = TimePickerDialog(activity as Context, R.style.PickerTheme, this, hour, minute, true)
        return picker
    }
}