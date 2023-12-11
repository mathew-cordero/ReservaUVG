package com.example.reservauvg.Navegacion

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reservauvg.Aulas.Adapter
import com.example.reservauvg.Aulas.Adapter_Guardados
import com.example.reservauvg.Aulas.Guardado
import com.example.reservauvg.Aulas.Salon
import com.example.reservauvg.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Save.newInstance] factory method to
 * create an instance of this fragment.
 */
class Save : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var mList = ArrayList<Guardado>()
    private lateinit var adapter: Adapter_Guardados
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
        val view:View =inflater.inflate(R.layout.fragment_save, container, false)
        val milistadeAulas = view.findViewById<RecyclerView>(R.id.listareservados)
        addDataToList()
        adapter = Adapter_Guardados(context,mList)
        milistadeAulas.layoutManager = LinearLayoutManager(context)
        milistadeAulas.adapter=adapter
        return view
    // Inflate the layout for this fragment

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Save.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Save().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun addDataToList() {

        mList.add(Guardado("CIT 210","2023-08-12","15:00-16:00",R.drawable.imagen))
        mList.add(Guardado("CIT 310","2023-09-11","10:00-01:00",R.drawable.imagen))


    }
}