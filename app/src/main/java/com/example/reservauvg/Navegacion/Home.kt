package com.example.reservauvg.Navegacion

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reservauvg.Aulas.Adapter
import com.example.reservauvg.Aulas.Salon
import com.example.reservauvg.R
import com.example.reservauvg.backend.GetAllClassroom
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var searchView: SearchView
    private var mList = ArrayList<Salon>()
    private lateinit var adapter:Adapter
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

        //obtenemos todas las clases.
        GetAllClassroom(){ clasesObtenidas ->
            mList = clasesObtenidas as ArrayList<Salon>
            Log.e("Firebaserespuesta", mList[0].nombre)
            adapter.setFilteredList(mList)
            adapter.notifyDataSetChanged()
        }

        
        val view:View =inflater.inflate(R.layout.fragment_home, container, false)
        val milistadeAulas = view.findViewById<RecyclerView>(R.id.listadeAulas)
        searchView = view.findViewById(R.id.searchView)




        adapter = Adapter(context,mList)
        milistadeAulas.layoutManager = LinearLayoutManager(context)
        milistadeAulas.adapter=adapter



        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }


        })
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    //esta funcion modifica la lista


    private fun filterList(query: String?) {

        if (query != null) {
            val filteredList = ArrayList<Salon>()
            for (i in mList) {
                if (i.nombre.lowercase(Locale.ROOT).contains(query)) {
                    filteredList.add(i)
                }
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(context, "Podrias volver a", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setFilteredList(filteredList)
            }
        }
    }
}