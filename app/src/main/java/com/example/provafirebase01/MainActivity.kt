package com.example.provafirebase01

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    private lateinit var signButt : Button
    private lateinit var logButt : Button

    private lateinit var mailText : TextView
    private lateinit var passText : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance();

        signButt = findViewById(R.id.signButton)
        logButt = findViewById(R.id.logButt)

        mailText = findViewById(R.id.mailText)
        passText = findViewById(R.id.passText)

        signButt.setOnClickListener{

            registerNewUser()
        }

        logButt.setOnClickListener{

            toLoginAct()
        }
    }

    private fun toLoginAct(){

        val myIntent = Intent(this, LoginActivity::class.java)
        startActivity(myIntent)
    }

   private fun registerNewUser(){

      val mail :String = mailText.text.toString().trim()
      val pass :String = passText.text.toString().trim()

       if (TextUtils.isEmpty(mail) || TextUtils.isEmpty(pass))
           Toast.makeText(applicationContext, "La mail è vuota coglione", Toast.LENGTH_LONG).show()

       else{            //createUserWithEmailAndPassword è una funzione di firebase, l'object arriva da firebase. il this in addOnCompl indica il thread
           mAuth?.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(
               this,
               object : OnCompleteListener<AuthResult> {

                   override fun onComplete(task: Task<AuthResult>) { //task contiene il risultato della registrazione da parte di firebase

                       if (task.isSuccessful) {

                           val user: FirebaseUser = mAuth!!.currentUser!!
                          println("Account creato ${user.email}")

                           user.sendEmailVerification().addOnCompleteListener(object : OnCompleteListener<Void>{        //questo per verificare la mail
                               override fun onComplete(task: Task<Void>) {
                                   if (task.isSuccessful) {
                                       Toast.makeText(applicationContext, "La mail id verifica inviata a $mail", Toast.LENGTH_LONG).show()
                                       toLoginAct()

                                   }else
                                       Toast.makeText(applicationContext, "Problema con la mail di autenticazione: " + task.exception?.message.toString(), Toast.LENGTH_LONG).show()
                               }

                           })

                       }else
                           Toast.makeText(applicationContext, "Minchia un casino, " + task.exception?.message.toString(), Toast.LENGTH_LONG).show()
                   }

               })
       }

   }



}