package com.example.recycle.Network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    private const val BASEURL = "https://recycle-api-v1.herokuapp.com"
    fun buildService(): Api {
        return Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(Api::class.java)
    }

}