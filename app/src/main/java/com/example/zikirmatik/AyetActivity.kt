package com.example.zikirmatik

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AyetActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kuran)

        val sureNo = intent.getIntExtra("SURE_NO", 1)
        val sureAdi = intent.getStringExtra("SURE_ADI") ?: "Sure"

        // ActionBar ayarları
        supportActionBar?.title = sureAdi
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.recyclerViewKuran)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Yeni Batch sistemini kullanan fonksiyonu çağırıyoruz
        ayetleriYukle(sureNo)
    }

    private fun ayetleriYukle(sureNo: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.alquran.cloud/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(KuranApiService::class.java)

        // DİKKAT: Artık surah/{no}/editions/... yapısını kullanan bir metodumuz olmalı
        // Eğer KuranApiService'de sadece tek ayet için (ayah/{id}) varsa,
        // Tüm sureyi çekmek için KuranApiService'e şu satırı eklemelisin:

        /* @GET("surah/{no}/editions/quran-uthmani,tr.diyanet")
        fun tumSureyiGetir(@Path("no") sureNo: Int): Call<TopluSureCevap>
        */

        // Şimdilik KuranApiService'deki mevcut yapıya göre hata vermemesi için
        // isimlendirmeyi manuel düzeltiyoruz.
        // Eğer KuranApiService'de 'topluAyetGetir' varsa onu çağırırız.

        api.topluAyetGetir(sureNo).enqueue(object : Callback<TopluAyetCevap> {
            override fun onResponse(call: Call<TopluAyetCevap>, response: Response<TopluAyetCevap>) {
                if (response.isSuccessful) {
                    val dataList = response.body()?.data
                    if (dataList != null && dataList.size >= 2) {
                        // Burada dataList[0] Arapça sureyi, dataList[1] Türkçe meali içerir
                        // AyetAdapter'ını bu yeni yapıya göre güncellemen gerekebilir

                        // Örnek: recyclerView.adapter = AyetAdapter(dataList[0].ayahs, dataList[1].ayahs)
                    }
                }
            }

            override fun onFailure(call: Call<TopluAyetCevap>, t: Throwable) {
                Toast.makeText(this@AyetActivity, "Bağlantı hatası!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}