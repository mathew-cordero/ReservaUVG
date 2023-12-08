package com.example.reservauvg.Navegacion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.example.reservauvg.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Account.newInstance] factory method to
 * create an instance of this fragment.
 */
class Account : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        val imageViewProfile: ImageView = view.findViewById(R.id.imageViewprofile)
        imageViewProfile.setOnClickListener {
            // Show the AlertDialog when the ImageView is clicked
            showAlertDialog()
        }
        // Inflate the layout for this fragment
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Account.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Account().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun showAlertDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        val inflater:LayoutInflater = getLayoutInflater()
        val view:View = inflater.inflate(R.layout.dialog_alert,null)
        alertDialogBuilder.setView(view)
        val dialogo:AlertDialog = alertDialogBuilder.create()
        dialogo.show()

        val botonfoto:Button = view.findViewById(R.id.buttontomar)
        botonfoto.setOnClickListener {
            //aqui agregaremos el boton para tomar foto
            //para mientras solo disminuiremos el dialogo
            dialogo.dismiss()
        }
        val botonlibreria:Button = view.findViewById(R.id.buttonlibrary)
        botonlibreria.setOnClickListener {
            //aqui agregaremos el boton para sacar foto de la libreria.
            //para mientras solo disminuiremos el dialogo
            dialogo.dismiss()
        }


    }

}