package com.example.reservauvg.Aulas

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.reservauvg.R
import com.example.reservauvg.Reserva.VistaReserva

class Adapter_Guardados(private val context: Context?,var mList: List<Guardado>): RecyclerView.Adapter<Adapter_Guardados.ViewHolder>() {

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

        viewHolder.boton_cancelar.setOnClickListener {
            showBanneralert()
        }
        viewHolder.imagen.setOnClickListener {
            val intent: Intent =  Intent(context, VistaReserva::class.java)
            context?.startActivity(intent)
        }
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

    private fun showBanneralert() {
        val alertDialogBuilder = AlertDialog.Builder(context!!)
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_delete_forever, null)
        alertDialogBuilder.setView(view)
        val dialogo: AlertDialog = alertDialogBuilder.create()
        dialogo.show()
        val boton_no:Button = view.findViewById(R.id.button_no)
        val boton_si:Button = view.findViewById(R.id.button_si)
        boton_no.setOnClickListener {
            dialogo.dismiss()
        }
        boton_si.setOnClickListener {
            dialogo.dismiss()
        }
    }
}