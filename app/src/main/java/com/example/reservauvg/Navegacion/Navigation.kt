package com.example.reservauvg.Navegacion

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.reservauvg.R
import com.example.reservauvg.databinding.ActivityMainBinding
import com.example.reservauvg.databinding.ActivityNavigationBinding
import com.google.android.material.navigation.NavigationView

class Navigation : AppCompatActivity() {
    private lateinit var binding: ActivityNavigationBinding
    private lateinit var toogle:ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {

        if (getSupportActionBar() != null) {
            getSupportActionBar()?.show();
            getSupportActionBar()?.setDisplayShowHomeEnabled(true);
            getSupportActionBar()?.setIcon(R.drawable.rulogo_decolor); // Reemplaza "tu_logo" con el nombre de tu archivo de imagen
        }

        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //var toolbar:Toolbar? = findViewById(R.id.toolbar)
        val drawerLayout:DrawerLayout = findViewById(R.id.drawerlayout)
        val navView:NavigationView = findViewById(R.id.nav_view)
        toogle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toogle)
        toogle.syncState()

        //setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setIcon(R.drawable.rulogo_decolor)

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

        //usar los botones del navigationdrawer
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_darkmode-> Toast.makeText(applicationContext,"Dark Mode",Toast.LENGTH_SHORT).show()
                R.id.nav_about ->Toast.makeText(applicationContext,"Sobre",Toast.LENGTH_SHORT).show()
                R.id.nav_conditions ->Toast.makeText(applicationContext,"Condiciones",Toast.LENGTH_SHORT).show()
                R.id.nav_politics ->Toast.makeText(applicationContext,"Politicas",Toast.LENGTH_SHORT).show()
                R.id.logout->Toast.makeText(applicationContext,"SALIR",Toast.LENGTH_SHORT).show()
                R.id.nav_notify->Toast.makeText(applicationContext,"NOTIFICACIONES ACTIVADAS",Toast.LENGTH_SHORT).show()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toogle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}


