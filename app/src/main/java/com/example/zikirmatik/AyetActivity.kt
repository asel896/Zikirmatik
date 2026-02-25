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
        setContentView(R.layout.activity_kuran) // KuranActivity ile aynı liste yapısını kullanıyoruz

        // MainActivity'den veya SureAdapter'dan gelen verileri alıyoruz
        val sureNo = intent.getIntExtra("SURE_NO", 1)
        val sureAdi = intent.getStringExtra("SURE_ADI") ?: "Sure"

        // Üst başlığı tıkladığımız sure adı yapalım
        supportActionBar?.title = sureAdi
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.recyclerViewKuran)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Verileri internetten çekmeye başla
        ayetleriVeMealleriYukle(sureNo)
    }

    private fun ayetleriVeMealleriYukle(sureNo: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.alquran.cloud/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(KuranApiService::class.java)

        // 1. ADIM: Önce Arapça ayet metinlerini çekiyoruz
        api.ayetleriGetir(sureNo).enqueue(object : Callback<AyetCevap> {
            override fun onResponse(call: Call<AyetCevap>, arapcaResponse: Response<AyetCevap>) {
                if (arapcaResponse.isSuccessful) {
                    val arapcaList = arapcaResponse.body()?.data?.ayahs ?: emptyList()

                    // 2. ADIM: Arapça başarıyla gelirse, hemen Diyanet mealini çekiyoruz
                    api.mealleriGetir(sureNo).enqueue(object : Callback<AyetCevap> {
                        override fun onResponse(call: Call<AyetCevap>, mealResponse: Response<AyetCevap>) {
                            if (mealResponse.isSuccessful) {
                                val mealList = mealResponse.body()?.data?.ayahs ?: emptyList()

                                // 3. ADIM: İki liste de elimizdeyse, Adapter'a verip ekranda gösteriyoruz
                                recyclerView.adapter = AyetAdapter(arapcaList, mealList)
                            }
                        }

                        override fun onFailure(call: Call<AyetCevap>, t: Throwable) {
                            Toast.makeText(this@AyetActivity, "Meal yüklenemedi!", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }

            override fun onFailure(call: Call<AyetCevap>, t: Throwable) {
                Toast.makeText(this@AyetActivity, "Arapça metinler yüklenemedi!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Geri tuşuna basınca sayfayı kapat
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}