package com.example.provafirebase01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private lateinit  var mailText : EditText
    private lateinit  var passText : EditText
    private lateinit var logButt : Button
    private lateinit var resetPassButt : Button
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mailText = findViewById(R.id.mailTextLog)
        passText = findViewById(R.id.passTextLog)
        logButt = findViewById(R.id.log2Butt)
        resetPassButt = findViewById(R.id.resetPassButt)
        mAuth = FirebaseAuth.getInstance()

        val loginText : TextView = findViewById(R.id.textView2)
        loginText?.setOnClickListener{
            mailText.setText("marco.borghetti19@gmail.com")
            passText.setText("123456")
        }

        val alertPassReset = AlertDialog.Builder(this)

        logButt.setOnClickListener{
            loginUser()
        }
        resetPassButt.setOnClickListener{



            var messaggio = ""
            var mail = mailText?.text.toString().trim()

            if (TextUtils.isEmpty(mail)) {

                messaggio = "Inserisci la tua mail nel campo mail"
                alertPassReset.setPositiveButton(android.R.string.yes) { dialog, which ->{}}
            }else{

                messaggio = "Vuoi resettare la pass associata all'indirizzo $mail?"
                alertPassReset.setPositiveButton(android.R.string.yes) { dialog, which ->
                    resetPass(mail)
                }

                alertPassReset.setNegativeButton(android.R.string.no) { dialog, which -> {}}

            }

            alertPassReset.setTitle("Pass Dimenticata")
            alertPassReset.setMessage(messaggio)
            alertPassReset.show()

//builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))


        }

    }

    private fun loginUser(){

        var mail = mailText?.text.toString().trim()
        var pass = passText?.text.toString().trim()


        if (TextUtils.isEmpty(mail) || (TextUtils.isEmpty(pass))){

            Toast.makeText(applicationContext, "Coglione mail o pass sono vuote", Toast.LENGTH_LONG).show()

        }
        else {

            mAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(object : OnCompleteListener<AuthResult>{

                override fun onComplete(task: Task<AuthResult>) {
                    if (task.isSuccessful) {
                        if(android.os.Debug.isDebuggerConnected())
                            android.os.Debug.waitForDebugger();

                        val user: FirebaseUser = mAuth.currentUser!!

                        if(user.isEmailVerified) {  //se l'user ha fatto l'autenticazione della mail
                            Toast.makeText(applicationContext, "Sei loggato Bomber!", Toast.LENGTH_LONG).show()
                            toCoso01(user.email.toString())
                        }else
                           Toast.makeText(applicationContext, "Devi cliccare sulla mail di verifica Bomber", Toast.LENGTH_LONG).show()
                    }else
                        Toast.makeText(applicationContext, "Chi cazzo sei? " + task.exception?.message.toString(), Toast.LENGTH_LONG).show()
                }


            })
        }
    }

    private fun resetPass(mail:String){

        mAuth?.sendPasswordResetEmail(mail).addOnCompleteListener(object  : OnCompleteListener<Void>{

            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful)
                    Toast.makeText(applicationContext, "Pass resettata", Toast.LENGTH_LONG).show()
                else
                    Toast.makeText(applicationContext, "Errore col reset: " + task.exception?.message.toString(), Toast.LENGTH_LONG).show()
            }
        })



    }

    private fun toCoso01(user: String){
        val myIntent = Intent(this, CosoActivity01::class.java)
        myIntent.putExtra("user", user);
        startActivity(myIntent)
    }

}