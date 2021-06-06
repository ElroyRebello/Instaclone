package com.example.instaclone

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instaclone.model.Posts
import java.math.BigInteger
import java.security.MessageDigest

class adapter (val context : Context ,val list: List<Posts>):
    RecyclerView.Adapter<adapter.ViewHolder>() {

   class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        val username : TextView = itemView.findViewById(R.id.username)
        val time : TextView = itemView.findViewById(R.id.time)
        val description : TextView = itemView.findViewById(R.id.descreption)
        val img : ImageView = itemView.findViewById(R.id.imageView2)
        val imgciccle : ImageView = itemView.findViewById(R.id.circleimg)
        val likeimg : ImageView = itemView.findViewById(R.id.likes)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.sample,parent,false)

        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPossition = list[position]

        holder.username.text= currentPossition.users?.username
        holder.description.text=currentPossition.description
        holder.time.text=DateUtils.getRelativeTimeSpanString(currentPossition.time_ms)
        Glide.with(context).load(currentPossition.img_url).into(holder.img)
        Glide.with(context).load(currentPossition.users?.username?.let { getProfileUrl(it) }).circleCrop().into(holder.imgciccle)


        holder.likeimg.setOnClickListener {
            holder.likeimg.setImageResource(R.drawable.ic_baseline_offline_bolt_24)


        }


    }

    override fun getItemCount(): Int {
        return  list.size
    }

    private fun getProfileUrl(userId: String): String? {
        var hex = ""
        try {
            val digest: MessageDigest = MessageDigest.getInstance("MD5")
            val hash: ByteArray = digest.digest(userId.toByteArray())
            val bigInt = BigInteger(hash)
            hex = bigInt.abs().toString(16)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "https://www.gravatar.com/avatar/$hex?d=identicon"
    }

}