package com.example.provafirebase01

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.*

class Storage_Activity01 : AppCompatActivity() {

    lateinit var uploadButt : Button

    lateinit var imageStorageView : ImageView
    lateinit var imageUri : Uri//? = null
    lateinit var store : FirebaseStorage
    lateinit var imageRef : StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage_01)

        uploadButt = findViewById(R.id.loadStorageImage)
        imageStorageView = findViewById(R.id.storageImageView)

        store = FirebaseStorage.getInstance()
        imageRef = store.reference.child("Images01")

        imageStorageView.setOnClickListener{
            pickFromGallery()
        }

        uploadButt.setOnClickListener{

            uploadImage()
        }
    }


    private fun pickFromGallery(){

        val galleria = Intent()
        galleria.type = "image/*"   // la /* vuol dire prendi tutti i tipi di immagine
        galleria.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(galleria, 888)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 888 && resultCode == Activity.RESULT_OK && data != null){ //il requestcode è impostato in pickFromGallery

            val defaultImgUri = Uri.parse("android.resource://com.example.provafirebase01/drawable/img01")

            imageUri  = (data.data?: defaultImgUri)// as Uri

            imageStorageView.setImageURI(imageUri)

           /* try {
                val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                imageStorageView.setImageBitmap(imageBitmap)
            }catch(error:IOException){
                Toast.makeText(applicationContext, "Un casino col file: " + error.message.toString(), Toast.LENGTH_LONG).show()
            }*/

        }
    }

    private fun uploadImage(){

        //if(imageUri != null){

        val ref = imageRef.child(UUID.randomUUID().toString()) //UUID.randomUUID() genera un random UUID

        ref.putFile(imageUri).addOnCompleteListener(object : OnCompleteListener<UploadTask.TaskSnapshot> {

            override fun onComplete(task0: Task<UploadTask.TaskSnapshot>) {
                if (task0.isSuccessful) {
                    Toast.makeText(applicationContext, "Immagine caricata", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(applicationContext, "Un casino col caricamento: " + task0.exception?.message.toString(), Toast.LENGTH_LONG).show()
                }
            }

        })
        //}


    }




    fun goBack(view: View){

        val myIntent = Intent(this, CosoActivity01::class.java)
        startActivity(myIntent)
    }
}