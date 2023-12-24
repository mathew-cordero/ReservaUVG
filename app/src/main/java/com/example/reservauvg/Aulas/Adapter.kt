package com.example.reservauvg.Aulas

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.reservauvg.R
import com.example.reservauvg.Reserva.Formulario
import com.example.reservauvg.Reserva.VistaReserva
import com.google.firebase.storage.FirebaseStorage
import com.bumptech.glide.Glide


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

        //vamos a definir la imagen
        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.getReferenceFromUrl(mList[i].imagen)
        storageReference.downloadUrl
            .addOnSuccessListener {uri ->
                // Utilizar Glide (o cualquier otra biblioteca de carga de imágenes) para cargar y mostrar la imagen
                Glide.with(context!!)
                    .load(uri)
                    .into(viewHolder.imagen)

        }.addOnFailureListener {

            }



        //Imagen de nuestro cardview
        viewHolder.imagen.setOnClickListener {
            val intent:Intent =  Intent(context,VistaReserva::class.java).apply {
                putExtra("nombre",mList[i].nombre)
                putExtra("link",mList[i].link)
                putExtra("imagenubicacion",mList[i].imagenubicacion)
                putExtra("ubicacion",mList[i].ubicacion)
                putExtra("disponibilidad",mList[i].disponibilidad)
                putExtra("idcalendar",mList[i].idcalendar)
                putExtra("imagen",mList[i].imagen)

            }
            context?.startActivity(intent)
        }
        //Ir al formulario de reserva
        viewHolder.boton_reserva.setOnClickListener {
            val intent:Intent = Intent(context, Formulario::class.java).apply {
                putExtra("idcalendar",mList[i].idcalendar)
            }
            context?.startActivity(intent)
        }

        viewHolder.boton_horarios.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(mList[i].link)
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