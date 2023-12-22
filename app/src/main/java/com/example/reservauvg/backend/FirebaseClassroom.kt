package com.example.reservauvg.backend

import android.util.Log
import com.example.reservauvg.Aulas.Salon
import com.google.firebase.firestore.FirebaseFirestore

fun GetAllClassroom(callback: (List<Salon>) -> Unit){

    val db = FirebaseFirestore.getInstance()
    val collectionReference = db.collection("salones")
    // Realiza la consulta para obtener todos los documentos en la colección
    collectionReference.get()
        .addOnSuccessListener { querySnapshot ->
            val listaAulas = mutableListOf<Salon>()
            for (document in querySnapshot) {
                val data = document.data

                if (data != null) {
                    val nombre = data["nombre"] as String
                    val dis = data["disponibilidad"] as Boolean
                    val link = data["link"] as String
                    val idcalendar = data["idcalendar"] as String
                    val image = data["imagen"] as String
                    val ubica = data["ubicacion"] as String
                    val imageubicacion = data["imagenubicacion"] as String
                    //las aulas se guardan en una lista.
                    listaAulas.add(Salon(nombre = nombre, disponibilidad = dis, imagen = image, link = link, idcalendar = idcalendar, ubicacion = ubica, imagenubicacion = imageubicacion))
                    //Log.d("se agrego",listaAulas[0].link)
                }
            }
            callback(listaAulas)
        }
        .addOnFailureListener { e ->
            Log.e("FirestoreError", "Error al obtener documentos: $e")
            callback(emptyList())
        }

}
