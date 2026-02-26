package com.example.zikirmatik

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

// Modeller (Sadece burada tanımlı kalsın)
data class TopluAyetCevap(val data: List<AyetDetay>)
data class AyetDetay(val text: String, val numberInSurah: Int, val surah: SureBilgiDetay)
data class SureBilgiDetay(val englishName: String, val name: String)

// API Interface (Sadece burada tanımlı kalsın)
interface KuranApiService {
    @GET("surah")
    fun sureleriGetir(): Call<SureCevap>

    @GET("ayah/{id}/editions/quran-uthmani,tr.diyanet")
    fun topluAyetGetir(@Path("id") ayetId: Int): Call<TopluAyetCevap>
}