package com.example.reservauvg.Navegacion

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.reservauvg.Auth.Login
import com.example.reservauvg.R
import com.google.firebase.auth.FirebaseAuth

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
        val imagebanner:ImageView = view.findViewById(R.id.imageBanner)
        val prefs = requireContext().getSharedPreferences(getString(R.string.prefsfile), Context.MODE_PRIVATE)
        val usuario:String? = prefs.getString("user", null)

        //obtenemos los widgets
        val textUsuario:TextView = view.findViewById(R.id.nombredecuenta)
        val buttoncerrarsesion:Button = view.findViewById(R.id.closesesionbutton)
        val btncanva:Button = view.findViewById(R.id.button4)
        val btncalendar:Button = view.findViewById(R.id.button5)
        val btncorreo:Button = view.findViewById(R.id.button6)

        //boton de canva
        btncanva.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            val urlcanva:String = "https://uvg.instructure.com"
            intent.data = Uri.parse(urlcanva)
            context?.startActivity(intent)
        }

        btncalendar.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            val urlcale:String = "https://calendar.google.com"
            intent.data = Uri.parse(urlcale)
            context?.startActivity(intent)
        }

        btncorreo.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            val urlcorreo:String = "https://mail.google.com"
            intent.data = Uri.parse(urlcorreo)
            context?.startActivity(intent)
        }

        textUsuario.text = usuario
        imageViewProfile.setOnClickListener {
            // Show the AlertDialog when the ImageView is clicked
            showAlertDialog()
        }

        imagebanner.setOnClickListener {
            showBanneralert()
        }

        buttoncerrarsesion.setOnClickListener {
            val prets  = requireContext().getSharedPreferences(getString(R.string.prefsfile),Context.MODE_PRIVATE).edit()
            prets.clear()
            prets.apply()
            FirebaseAuth.getInstance().signOut()
            val login = Intent(requireContext(), Login::class.java)
            startActivity(login)
            requireActivity().finish()
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

    private fun showBanneralert(){
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        val inflater:LayoutInflater = getLayoutInflater()
        val view:View = inflater.inflate(R.layout.dialog_banner,null)
        alertDialogBuilder.setView(view)
        val dialogo:AlertDialog = alertDialogBuilder.create()
        dialogo.show()
        val imagenicono:ImageView = view.findViewById(R.id.iconImage)
        imagenicono.setOnClickListener {
            showAlertDialog()
        }
    }

}