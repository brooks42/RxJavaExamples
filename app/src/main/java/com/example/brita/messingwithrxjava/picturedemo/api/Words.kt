package com.example.brita.messingwithrxjava.picturedemo.api

import retrofit2.Call
import retrofit2.http.GET

interface Words {

    @GET("/dwyl/english-words/master/words.txt")
    fun getWords(): Call<String>
}