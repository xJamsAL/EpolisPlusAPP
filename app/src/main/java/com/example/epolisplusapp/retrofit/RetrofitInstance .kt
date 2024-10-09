package com.example.epolisplusapp.retrofit

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.epolisplusapp.api.MainApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance(context: Context) {

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
private val chuckerInterceptor = ChuckerInterceptor.Builder(context).build()

    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .addInterceptor(chuckerInterceptor)
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://epolisplus.uz/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: MainApi.ApiService by lazy {
        retrofit.create(MainApi.ApiService::class.java)
    }
}
