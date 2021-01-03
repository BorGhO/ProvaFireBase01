package com.example.provafirebase01

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity01 : AppCompatActivity() {

    private lateinit var firstNameText : EditText
    private lateinit var lastNameText: EditText
    private lateinit var userNameText: EditText
    private lateinit var updateButt: Button

    private lateinit var firebaseAuth : FirebaseAuth
    private lateinit var firebaseDB : FirebaseDatabase      //questo definisce il DB
    private lateinit var firebaseDB_Users : DatabaseReference   //questo una tabella del DB (forse)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile01)

        firstNameText = findViewById<EditText>(R.id.firstNomeText)
        lastNameText = findViewById<EditText>(R.id.lastNomeText)
        userNameText = findViewById<EditText>(R.id.userNomeText)
        updateButt = findViewById<Button>(R.id.updateUserButt)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDB = FirebaseDatabase.getInstance("https://provafirebase01-default-rtdb.europe-west1.firebasedatabase.app/")

        val user: FirebaseUser = firebaseAuth.currentUser!!
        println("USER: "+user.uid)

        firebaseDB_Users = firebaseDB.getReference("Users")//.child(user.uid)

        updateButt.setOnClickListener{
            saveUserInfo()
        }
    }

    fun goBack(view: View){

        val myIntent = Intent(this, CosoActivity01::class.java)
        startActivity(myIntent)
    }

    private fun saveUserInfo(){

        val firstName = firstNameText.text.toString().trim()
        val lastName = lastNameText.text.toString().trim()
        val userName = userNameText.text.toString().trim()

        if(TextUtils.isEmpty(firstName))
            Toast.makeText(applicationContext, "Dio cane metti il nome", Toast.LENGTH_LONG).show()

        //TODO fallo anche con gli altri campi

        val userInfo = HashMap<String, Any>()
        userInfo.put("firstName", firstName)
        userInfo.put("lastName", lastName)
        userInfo.put("userName", userName)

        val user: FirebaseUser = firebaseAuth.currentUser!!
        println("USER Button: "+user)

        firebaseDB_Users.child(user.uid).setValue(userInfo).addOnCompleteListener(       //user.uid viene usato per definire nuove sottotabelle di Users
            object : OnCompleteListener<Void> {
                override fun onComplete(task01: Task<Void>) {
                    if (android.os.Debug.isDebuggerConnected())
                        android.os.Debug.waitForDebugger()
                    //Toast.makeText(applicationContext, "Entra", Toast.LENGTH_LONG).show()
                    if (task01.isSuccessful)
                        Toast.makeText(applicationContext, "Informazione aggiornata", Toast.LENGTH_LONG).show()
                    else
                        Toast.makeText(applicationContext, "Errore in scrittura: " + task01.exception?.message.toString(), Toast.LENGTH_LONG).show()
                }
            })

    }


}