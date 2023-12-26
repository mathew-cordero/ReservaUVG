package com.example.reservauvg.Reserva

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EventoViewModel : ViewModel() {
    private val _idcalendar = MutableLiveData<String>()
    val idcalendar: LiveData<String>
        get() = _idcalendar

    private val _nombre = MutableLiveData<String>()
    val nombre: LiveData<String>
        get() = _nombre

    fun setIdcalendar(value: String) {
        _idcalendar.value = value
    }

    fun setNombre(value: String) {
        _nombre.value = value
    }
}
