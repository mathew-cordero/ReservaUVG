package com.example.reservauvg.Navegacion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.reservauvg.R
import com.example.reservauvg.databinding.ActivityMainBinding
import com.example.reservauvg.databinding.ActivityNavigationBinding

class Navigation : AppCompatActivity() {
    private lateinit var binding: ActivityNavigationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //llamamos primero a home para presentarlo a el primero
        replace(Home())
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replace(Home())
                R.id.account -> replace(Account())
                R.id.save -> replace(Save())
                else ->{

                }
            }
            true
        }

    }

    private fun replace(fragment:Fragment){
        val fragmentmanager = supportFragmentManager
        val fragmenttransaction = fragmentmanager.beginTransaction()
        fragmenttransaction.replace(R.id.frame_layout,fragment)
        fragmenttransaction.commit()
    }
}