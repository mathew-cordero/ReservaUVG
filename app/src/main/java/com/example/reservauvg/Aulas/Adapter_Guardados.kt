package com.example.reservauvg.Aulas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reservauvg.R

class Adapter_Guardados(var mList: List<Guardado>): RecyclerView.Adapter<Adapter_Guardados.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.aula_reservada,viewGroup,false)
        return ViewHolder(v)
        //aqui configuraremos los botones.

    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.titulo.text = mList[i].nombre
        viewHolder.fecha.text = mList[i].fecha
        viewHolder.horarios.text = mList[i].horario
        viewHolder.imagen.setImageResource(mList[i].imagen)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var imagen: ImageView
        var titulo: TextView
        var fecha: TextView
        var horarios: TextView
        var boton_horarios: Button
        var boton_cancelar:Button
        init {
            //inicializamos todos los componentes
            imagen = itemView.findViewById(R.id.item_imagesave)
            titulo = itemView.findViewById(R.id.item_titulesave)
            fecha = itemView.findViewById(R.id.textofecha)
            horarios = itemView.findViewById(R.id.text_horario)
            boton_horarios = itemView.findViewById(R.id.button_horariosguardados)
            boton_cancelar = itemView.findViewById(R.id.button_cancelar)
        }
    }
}