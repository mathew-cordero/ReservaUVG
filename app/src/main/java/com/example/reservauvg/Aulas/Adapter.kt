package com.example.reservauvg.Aulas

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reservauvg.R
import com.example.reservauvg.Reserva.Formulario
import com.example.reservauvg.Reserva.VistaReserva


class Adapter(private val context: Context?, var mList: List<Salon>): RecyclerView.Adapter<Adapter.ViewHolder>(){


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.aula_layout,viewGroup,false)
        return ViewHolder(v)

        //aqui configuraremos los botones.

    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.titulo.text = mList[i].nombre
        //vemos que disponibilidad tiene
        var disponibilidad:String
        if(mList[i].disponibilidad){
            disponibilidad = "SI" //si esta disponible
        }else{
            disponibilidad = "NO" //si no esta diponible
        }
        viewHolder.disponibilidad.text = "Disponible: "+disponibilidad
        viewHolder.imagen.setImageResource(mList[i].imagen)

        //Imagen de nuestro cardview
        viewHolder.imagen.setOnClickListener {
            val intent:Intent =  Intent(context,VistaReserva::class.java)
            context?.startActivity(intent)
        }
        //Ir al formulario de reserva
        viewHolder.boton_reserva.setOnClickListener {
            val intent:Intent = Intent(context, Formulario::class.java)
            context?.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setFilteredList(mList: List<Salon>){
        this.mList = mList
        notifyDataSetChanged()
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