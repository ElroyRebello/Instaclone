package com.example.instaclone.model

class Posts(
    var description : String = "",
    var time_ms : Long = 0,
    var img_url : String = "",
    var users : User? = null,
    var likes :ArrayList<String>?= null
)