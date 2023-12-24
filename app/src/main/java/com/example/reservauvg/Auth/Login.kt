package com.example.reservauvg.Auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.example.reservauvg.Constants.GOOGLE_SING_IN
import com.example.reservauvg.Navegacion.Navigation
import com.example.reservauvg.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getSupportActionBar() != null) {
            getSupportActionBar()?.hide();
        }
        setContentView(R.layout.activity_login)
        val iniciarSesion:Button = findViewById(R.id.botoninicio)


        iniciarSesion.setOnClickListener {
            val googleConfig = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(
                getString(R.string.default_web_client_id)
            ).requestEmail().build()
            val googleClient = GoogleSignIn.getClient(this,googleConfig)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, GOOGLE_SING_IN)

        }
        //iniciar sesion
        sesion()



    }

    private fun sesion() {
        val prets  = getSharedPreferences(getString(R.string.prefsfile), Context.MODE_PRIVATE)
        val email:String? = prets.getString("email",null)
        val user:String? = prets.getString("user",null)
        val token:String? = prets.getString("token",null)
        if(email!=null && user!=null && token!=null){
            showHome(email,user,token)
        }

    }

    private fun showHome(email:String,user:String,token:String){
        val homeIntent = Intent(this@Login, Navigation::class.java).apply {
            putExtra("email",email)
            putExtra("user",user)
            putExtra("token",token)
        }
        startActivity(homeIntent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== GOOGLE_SING_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if(account!=null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken,null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if(it.isSuccessful){
                            if(account.email.toString().contains("uvg.edu.gt")){
                                showHome(account.email?:"",account.givenName?:"",account.idToken?:"")
                            }else{
                                showAlertErroAcount()
                            }


                        }else{
                            showAlert()
                            Log.d("Error google","Error en VALIDAR CREDENCIALES")
                        }
                    }

                }
            }catch (e: ApiException){
                showAlert()
                Log.d("Eror en firebase",e.toString())

            }


        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertErroAcount(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error de Cuenta")
        builder.setMessage("No puede iniciar sesion con otra cuenta que no sea de la Universidad del Valle de Guatemala")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}