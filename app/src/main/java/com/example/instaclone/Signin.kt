package com.example.instaclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class Signin : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var googleSignInClient : GoogleSignInClient
    var  RC_SIGN_IN=123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val tvem : TextView = findViewById(R.id.editTextTextEmailAddress)
        val tvps : TextView = findViewById(R.id.editTextTextPassword2)
        val signin : TextView = findViewById(R.id.SignINem)
        val google : TextView = findViewById(R.id.google)
        // Initialize Firebase Auth

        auth = FirebaseAuth.getInstance()


                signin.setOnClickListener {

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

                    auth.signInWithEmailAndPassword(email, password)

                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(baseContext, "Authenticad done",
                                    Toast.LENGTH_SHORT).show()
                                val intent = Intent(this,MainActivity::class.java)
                                startActivity(intent)

                            } else {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()

                            }
                        }
                }

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        google.setOnClickListener {
            signIn()
        }

    }


    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Toast.makeText(baseContext, "Authenticad done",
                    Toast.LENGTH_SHORT).show()
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(baseContext, "not Authenticad done",
                    Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    Toast.makeText(baseContext, "Authenticad done",
                        Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "norrrrrr Authenticad done",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

}