package com.example.brita.messingwithrxjava.picturedemo.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object RestClient {

    val retrofit: Retrofit

    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        this.retrofit = Retrofit.Builder().baseUrl("https://raw.githubusercontent.com")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(client)
            .build()
    }
}