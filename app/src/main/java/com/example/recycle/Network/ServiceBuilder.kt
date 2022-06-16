package com.example.recycle.Network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {

    private val client = OkHttpClient.Builder().build()
    fun getInstance():Retrofit{
        return  Retrofit.Builder()
            .baseUrl("https://recycle-api-v1.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
    fun init():Api
    {
        return getInstance().create(Api::class.java)
    }
}