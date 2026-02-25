package com.example.zikirmatik

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class KuranActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tabSure: TextView
    private lateinit var tabAyet: TextView
    private lateinit var tabCuz: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kuran)

        initViews()

        // Varsayılan olarak Sureleri yükle
        selectTab(tabSure)
        sureleriYukle()

        tabSure.setOnClickListener {
            selectTab(tabSure)
            sureleriYukle()
        }

        tabAyet.setOnClickListener {
            selectTab(tabAyet)
            recyclerView.adapter = null
            Toast.makeText(this, "Ayetler sekmesi", Toast.LENGTH_SHORT).show()
        }

        tabCuz.setOnClickListener {
            selectTab(tabCuz)
            cuzleriGoster()
        }
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerViewKuran)
        recyclerView.layoutManager = LinearLayoutManager(this)
        tabSure = findViewById(R.id.tabSure)
        tabAyet = findViewById(R.id.tabAyet)
        tabCuz = findViewById(R.id.tabCuz)
        supportActionBar?.hide()
    }

    private fun selectTab(selectedTab: TextView) {
        val tabs = listOf(tabSure, tabAyet, tabCuz)
        for (tab in tabs) {
            tab.setTextColor(Color.parseColor("#80FFFFFF"))
            tab.setBackgroundResource(0)
        }
        selectedTab.setTextColor(Color.WHITE)
        selectedTab.setBackgroundResource(R.drawable.tab_indicator)
    }

    private fun sureleriYukle() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.alquran.cloud/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(KuranApiService::class.java)
        api.sureleriGetir().enqueue(object : Callback<SureCevap> {
            override fun onResponse(call: Call<SureCevap>, response: Response<SureCevap>) {
                if (response.isSuccessful) {
                    val list = response.body()?.data ?: emptyList()
                    recyclerView.adapter = SureAdapter(list)
                }
            }
            override fun onFailure(call: Call<SureCevap>, t: Throwable) {
                Toast.makeText(this@KuranActivity, "Hata!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun cuzleriGoster() {
        val cuzListesi = (1..30).map { "$it. Cüz" }
        recyclerView.adapter = CuzAdapter(cuzListesi) { cuzNo ->
            Toast.makeText(this, "$cuzNo. Cüz seçildi", Toast.LENGTH_SHORT).show()
        }
    }
}