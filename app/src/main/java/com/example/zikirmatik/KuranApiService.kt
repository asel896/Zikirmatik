package com.example.zikirmatik

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

// SADECE BU DOSYAYA ÖZGÜ MODELLER KALSIN
data class TopluAyetCevap(val data: List<AyetDetay>)
data class AyetDetay(val text: String, val numberInSurah: Int, val surah: SureBilgiDetay)

data class TopluSureCevap(val data: List<SureIcerik>)
data class SureIcerik(val ayahs: List<AyetDetay>)

//data class SureBilgiDetay(val englishName: String, val name: String)


// KuranApiService.kt dosyanın içinde şu kısmı bul ve number satırını ekle:
data class SureBilgiDetay(
    val number: Int, // Bu satırı ekle (Sürenin 1, 2, 3... gibi numarası)
    val englishName: String,
    val name: String
)

// API INTERFACE
interface KuranApiService {
    @GET("surah")
    fun sureleriGetir(): Call<SureCevap> // SureCevap başka dosyadan gelecek

    @GET("ayah/{id}/editions/quran-uthmani,tr.diyanet")
    fun topluAyetGetir(@Path("id") ayetId: Int): Call<TopluAyetCevap>

    @GET("surah/{no}/editions/quran-uthmani,tr.diyanet")
    fun tumSureyiGetir(@Path("no") sureNo: Int): Call<TopluSureCevap>
}