package com.example.recycle.Network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @GET("/user/{phone_number}/")
    fun logIn(
        @Path("phone_number")phone_number:String
    ):Call<ResponseBody>

    @FormUrlEncoded
    @POST("/user/")
    fun signUp(
        @Field("name")name:String,
        @Field("phone_number")phone_number:String
    ):Call<ResponseBody>
}