package com.example.instaclone

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.instaclone.model.Posts
import com.example.instaclone.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class addpost : AppCompatActivity() {

    var CODE=1234
    lateinit var img:ImageView
    var signinuser: User? =null

    var photouri: Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addpost)

        var tvdesc: EditText = findViewById(R.id.desc)
          img = findViewById(R.id.imageView)
        var submit: Button = findViewById(R.id.Submit)
        var addimg: Button = findViewById(R.id.addimg)

        val firestorecb = FirebaseFirestore.getInstance()
        val storageReference = FirebaseStorage.getInstance().reference

        firestorecb.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid as String)
            .get()
            .addOnSuccessListener { snap->
                signinuser = snap.toObject(User::class.java)
            }




        addimg.setOnClickListener {
            val imgpiccer = Intent(Intent.ACTION_GET_CONTENT)
            imgpiccer.type="image/*"

                startActivityForResult(imgpiccer,CODE)



        }

        submit.setOnClickListener {
            if(photouri== null)
            {  Toast.makeText(this,"choose img",Toast.LENGTH_SHORT).show()
            return@setOnClickListener}
            if(tvdesc.text.isBlank())
            { Toast.makeText(this,"enter desc",Toast.LENGTH_SHORT).show()
            return@setOnClickListener}
            if(signinuser == null)
            {     Toast.makeText(this,"Sign in to uploaad img",Toast.LENGTH_SHORT).show()
            return@setOnClickListener}
             submit.isEnabled=false

            val photoref = storageReference.child("img/${System.currentTimeMillis()}-photp.jpg")
            val photouploaduri = photouri as Uri
            photoref.putFile(photouploaduri)
                .continueWithTask { photoUploadtask ->
                    photoref.downloadUrl


                }.continueWithTask { downloadurltask ->

                    val post = Posts(
                        tvdesc.text.toString(),
                        System.currentTimeMillis(),
                        downloadurltask.result.toString(),
                        signinuser
                    )
                    firestorecb.collection("posts").add(post)

                }.addOnCompleteListener { postcrestiontask->
                    submit.isEnabled=true

                    if(!postcrestiontask.isSuccessful)
                    {
                        Toast.makeText(this,"submit failed",Toast.LENGTH_SHORT).show()
                    }
                    tvdesc.text.clear()
                    img.setImageResource(0)
                    Toast.makeText(this,"Worked",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,userposts::class.java)

                    intent.putExtra(USERNAME, signinuser?.username)
                    startActivity(intent)
                    finish()

                }


        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== CODE)
        {
            if(resultCode==Activity.RESULT_OK)
            {

                    photouri = data?.data
                img.setImageURI(photouri)

            }
            else{
                Toast.makeText(this,"cancled img piccer",Toast.LENGTH_LONG).show()
            }
        }

    }


}