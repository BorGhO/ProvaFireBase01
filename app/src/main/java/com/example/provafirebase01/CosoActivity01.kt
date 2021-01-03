package com.example.provafirebase01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class CosoActivity01 : AppCompatActivity() {

    private lateinit var userText : TextView
    private lateinit var changeMailButt : Button
    private lateinit var toUserInfoButt : Button
    private lateinit var confirmMailButt : Button
    private lateinit var cancelMailButt : Button
    private lateinit var toStorageButt : Button
    private lateinit var toDBButt  : Button
    private lateinit  var mailText : EditText
    private lateinit  var passText : EditText
    private lateinit  var oldText : EditText
    private lateinit  var changeLay : LinearLayout
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coso01)

        userText = findViewById(R.id.userTextView)
        changeMailButt = findViewById(R.id.changeMailButt)
        toUserInfoButt = findViewById(R.id.toUserInfoButt)
        confirmMailButt = findViewById(R.id.confirmChangeMailButt)
        cancelMailButt = findViewById(R.id.cancelChangeMailButt)
        toStorageButt = findViewById(R.id.toStorageButt)
        toDBButt = findViewById(R.id.toDBButt)
        mailText = findViewById(R.id.mailTextChange)
        passText = findViewById(R.id.passTextChange)
        oldText = findViewById(R.id.oldTextChange)
        changeLay = findViewById(R.id.changeLay)

        mAuth = FirebaseAuth.getInstance()

        userText.text = intent.getStringExtra("user") ?: "Chi cazzo sei?"

        changeMailButt.setOnClickListener {

            changeLay.visibility = VISIBLE
        }

        toUserInfoButt.setOnClickListener {

            toUser()
        }
        cancelMailButt.setOnClickListener {

            changeLay.visibility = INVISIBLE
        }

        confirmMailButt.setOnClickListener {

            changeMail()
        }

        toStorageButt.setOnClickListener {

            toStorage()
        }
        toDBButt.setOnClickListener {

            toProfileActivity01()
        }

    }

    private fun toStorage(){

        val myIntent = Intent(this, Storage_Activity01::class.java)
        startActivity(myIntent)
    }

    private fun toUser(){

        val myIntent = Intent(this, UserInfoActivity01::class.java)
        startActivity(myIntent)
    }

    private fun changeMail(){

        val mail = mailText.text.toString().trim()
        val oldMail = userText.text.toString().trim() //questo lo prendo dal textfield che a suo volta lo prende dell'intent, ma potrei anche prenderlo da user
        val pass = passText.text.toString().trim()
        val oldPass = oldText.text.toString().trim()

        val user: FirebaseUser = mAuth.currentUser!!
        val userInfo = EmailAuthProvider.getCredential(oldMail, oldPass) //questo impacchetta mail e pass

        user.reauthenticate(userInfo).addOnCompleteListener(object : OnCompleteListener<Void>{  //riautenticazione, con la vecchia mail e pass

            override fun onComplete(taskAuth: Task<Void>) {

                if(taskAuth.isSuccessful){

                    user.updateEmail(mail).addOnCompleteListener(object : OnCompleteListener<Void> {    //cambio mail

                        override fun onComplete(taskMail: Task<Void>) {
                            if (taskMail.isSuccessful) {
                                user.updatePassword(pass).addOnCompleteListener(object : OnCompleteListener<Void> {     //cambio pass

                                    override fun onComplete(taskPass: Task<Void>) {

                                        if (taskPass.isSuccessful)

                                            Toast.makeText(applicationContext, "Mail e pass cambiate Bomber " , Toast.LENGTH_LONG).show()
                                        else
                                            Toast.makeText(applicationContext, "Errore con la nuova pass: " + taskPass.exception?.message.toString(), Toast.LENGTH_LONG).show()
                                    }
                                })
                            } else
                                Toast.makeText(applicationContext, "Errore con la nuova mail: " + taskMail.exception?.message.toString(), Toast.LENGTH_LONG).show()


                        }
                    })
                }else
                    Toast.makeText(applicationContext, "Errore di autenticazione: " + taskAuth.exception?.message.toString(), Toast.LENGTH_LONG).show()

            }


        })
    }

    private fun toProfileActivity01(){
        val myIntent = Intent(this, ProfileActivity01::class.java)
        startActivity(myIntent)
    }
}