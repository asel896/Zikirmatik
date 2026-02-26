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

        supportActionBar?.title = sureAdi
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.recyclerViewKuran)
        recyclerView.layoutManager = LinearLayoutManager(this)

        sureyiYukle(sureNo)
    }

    private fun sureyiYukle(sureNo: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.alquran.cloud/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(KuranApiService::class.java)

        // Artık 'tumSureyiGetir' kullanarak tüm listeyi çekiyoruz
        api.tumSureyiGetir(sureNo).enqueue(object : Callback<TopluSureCevap> {
            override fun onResponse(call: Call<TopluSureCevap>, response: Response<TopluSureCevap>) {
                if (response.isSuccessful) {
                    val dataList = response.body()?.data
                    if (dataList != null && dataList.size >= 2) {
                        // dataList[0] Arapça, dataList[1] Türkçe Meal
                        recyclerView.adapter = AyetAdapter(dataList[0].ayahs, dataList[1].ayahs)
                    }
                }
            }

            override fun onFailure(call: Call<TopluSureCevap>, t: Throwable) {
                Toast.makeText(this@AyetActivity, "Sure yüklenirken hata oluştu!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}