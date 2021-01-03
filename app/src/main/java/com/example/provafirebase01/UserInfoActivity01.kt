package com.example.provafirebase01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class UserInfoActivity01 : AppCompatActivity() {

    lateinit var  firstNameText: TextView
    lateinit var  lastNameText: TextView
    lateinit var  userNameText: TextView
    lateinit var  upDateInfoButt: Button
    lateinit var  changeMail2Butt: Button
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDB: FirebaseDatabase
    lateinit var firebaseDB_User : DatabaseReference
    lateinit var user2: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info01)

        firstNameText = findViewById(R.id.userShowFirstNameText)
        lastNameText = findViewById(R.id.userShowLastNameText)
        userNameText = findViewById(R.id.userShowUsernameText)
        upDateInfoButt = findViewById(R.id.upDateInfoButt)
        changeMail2Butt = findViewById(R.id.changeMail2Butt)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDB = FirebaseDatabase.getInstance("https://provafirebase01-default-rtdb.europe-west1.firebasedatabase.app/")
        user2 = firebaseAuth.currentUser!!

        firebaseDB_User = firebaseDB.getReference("Users").child(user2.uid)


        firebaseDB_User.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    val fName = snapshot.child("firstName").value as String
                    val lName = snapshot.child("lastName").value as String
                    val uName = snapshot.child("userName").value as String

                    firstNameText.text = fName
                    lastNameText.text = lName
                    userNameText.text = uName
                }else{

                    Toast.makeText(applicationContext, "Dati mancanti", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "onCancelled", Toast.LENGTH_LONG).show()
            }
        })




        upDateInfoButt.setOnClickListener{
            updateUser()
        }
        changeMail2Butt.setOnClickListener{
            cancellaDati()
        }




    }

    fun cancellaDati(){

        firebaseDB_User.setValue(null)

    }



    fun updateUser (){

        val myIntent = Intent(this, CosoActivity01::class.java)
        startActivity(myIntent)


    }


}