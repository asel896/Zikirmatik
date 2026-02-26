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
    private val dinamikListe = mutableListOf<FavoriAyet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kuran)

        initViews()

        // İlk açılışta Sureler sekmesini göster
        selectTab(tabSure)
        sureleriYukle()

        // --- SEKME TIKLAMA OLAYLARI ---

        tabSure.setOnClickListener {
            selectTab(tabSure)
            sureleriYukle()
        }

        tabAyet.setOnClickListener {
            selectTab(tabAyet)
            dinamikAyetleriYukle()
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

    // 1. SEKME: TÜM SURELERİ YÜKLE
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
                Toast.makeText(this@KuranActivity, "Sureler yüklenemedi", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 2. SEKME: RASTGELE 10 AYET (ARAPÇA + TÜRKÇE MEAL)
    private fun dinamikAyetleriYukle() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.alquran.cloud/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(KuranApiService::class.java)
        dinamikListe.clear() // Her tıklandığında eski ayetleri temizle

        // 10 adet rastgele ayet ID'si (Toplam 6236 ayet arasından)
        val rastgeleIdler = List(10) { (1..6236).random() }

        for (id in rastgeleIdler) {
            api.topluAyetGetir(id).enqueue(object : Callback<TopluAyetCevap> {
                override fun onResponse(call: Call<TopluAyetCevap>, response: Response<TopluAyetCevap>) {
                    if (response.isSuccessful) {
                        val veriler = response.body()?.data
                        if (veriler != null && veriler.size >= 2) {
                            // index 0: quran-uthmani (Arapça)
                            // index 1: tr.diyanet (Türkçe Meal)
                            val arapca = veriler[0].text
                            val meal = veriler[1].text
                            val bilgi = "${veriler[1].surah.englishName}, ${veriler[1].numberInSurah}"

                            dinamikListe.add(FavoriAyet(arapca, meal, bilgi))

                            // Her yeni ayet geldiğinde adapter'ı tazele
                            recyclerView.adapter = FavoriAyetAdapter(dinamikListe)
                        }
                    }
                }
                override fun onFailure(call: Call<TopluAyetCevap>, t: Throwable) {
                    // Hata olursa sessizce devam et
                }
            })
        }
    }

    // 3. SEKME: CÜZ LİSTESİ (SABİT)
    private fun cuzleriGoster() {
        val cuzAdlari = (1..30).map { "$it. Cüz" }
        recyclerView.adapter = CuzAdapter(cuzAdlari) { cuzNo ->
            Toast.makeText(this, "$cuzNo. Cüz seçildi", Toast.LENGTH_SHORT).show()
        }
    }
}