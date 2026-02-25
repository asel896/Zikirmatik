package com.example.zikirmatik

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface KuranApiService {
    @GET("surah")
    fun sureleriGetir(): Call<SureCevap>

    @GET("surah/{no}") // Arapça metin için
    fun ayetleriGetir(@Path("no") sureNo: Int): Call<AyetCevap>

    @GET("surah/{no}/tr.diyanet") // Diyanet meali için
    fun mealleriGetir(@Path("no") sureNo: Int): Call<AyetCevap>

}