package com.example.reservauvg.Aulas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reservauvg.R

class Adapter: RecyclerView.Adapter<Adapter.ViewHolder>(){

    val titules = arrayOf("CIT 210","CIT 323", "CIT 525")
    val disponible = arrayOf("SI","NO","NO")
    val images = intArrayOf(R.drawable.imagen,R.drawable.imagen,R.drawable.imagen)
    //colocamos el oncreate
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.aula_layout,viewGroup,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.titulo.text = titules[i]
        viewHolder.disponibilidad.text = "Disponible: "+disponible[i]
        viewHolder.imagen.setImageResource(images[i])
    }

    override fun getItemCount(): Int {
        return titules.size
    }

    //clase para construir el viewholder
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var imagen:ImageView
        var titulo:TextView
        var disponibilidad:TextView
        var boton_reserva: Button
        var boton_horarios: Button
        init {
            imagen = itemView.findViewById(R.id.item_image)
            titulo = itemView.findViewById(R.id.item_titule)
            disponibilidad= itemView.findViewById(R.id.textdisponible)
            boton_reserva =  itemView.findViewById(R.id.button_reservar)
            boton_horarios = itemView.findViewById(R.id.button_horarios)
        }
    }

}