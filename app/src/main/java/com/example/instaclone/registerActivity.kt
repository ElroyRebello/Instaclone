package com.example.instaclone

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class registerActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val tvem : TextView= findViewById(R.id.editTextTextEmailAddress2)
        val tvps : TextView= findViewById(R.id.editTextTextPassword)
        val regis : TextView= findViewById(R.id.Register)
        val signin : TextView= findViewById(R.id.SignIN)
        // Initialize Firebase Auth

        auth = FirebaseAuth.getInstance()

        signin.setOnClickListener {
            val intent = Intent(this,Signin::class.java)
            startActivity(intent)

        }

        regis.setOnClickListener{
            val email = tvem.text.toString()
            val password = tvps.text.toString()
            if (email.isEmpty()) {
                tvem.error = "Enter u r email"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                tvps.error = "Enter u r pass"
                return@setOnClickListener
            }



            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(
                            baseContext, "Regidtered",
                            Toast.LENGTH_SHORT
                        ).show()


                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
        }



        }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }


    }






