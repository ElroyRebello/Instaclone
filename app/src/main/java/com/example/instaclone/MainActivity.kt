package com.example.instaclone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instaclone.model.Posts
import com.example.instaclone.model.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
 val USERNAME="USERNAME"
open class MainActivity : AppCompatActivity() {

    lateinit var postlist : MutableList<Posts>
    lateinit var adapter : adapter

    var signinuser: User? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        postlist= mutableListOf()
        var rv : RecyclerView = findViewById(R.id.rvr)
        var fv : FloatingActionButton = findViewById(R.id.floatingActionButton2)
        adapter= adapter(this,postlist)
        rv.adapter=adapter
        rv.layoutManager= LinearLayoutManager( this)



        val firestorecb = FirebaseFirestore.getInstance()
        var postsreference = firestorecb.collection("posts")
            .orderBy("time_ms",Query.Direction.DESCENDING)

        firestorecb.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid as String)
            .get()
            .addOnSuccessListener { snap->
                signinuser = snap.toObject(User::class.java)
            }

        val username = intent.getStringExtra(USERNAME)
        if(username!=null)
        {   supportActionBar?.title =username
           postsreference= postsreference.whereEqualTo("users.username",username)
        }

        postsreference.addSnapshotListener { value, error ->
            if (error!= null || value == null)
            {
                Log.e("el","game xalaoi",error)
            }
              val postslists = value?.toObjects(Posts::class.java)
            postlist.clear()
            if (postslists != null) {
                postlist.addAll(postslists)
            }
            adapter.notifyDataSetChanged()



        }

        fv.setOnClickListener {
            val intent = Intent(this,addpost::class.java)


            startActivity(intent)
        }



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.profile, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.profile -> {
                //logout

                val intent = Intent(this,userposts::class.java)

                intent.putExtra(USERNAME, signinuser?.username)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}


