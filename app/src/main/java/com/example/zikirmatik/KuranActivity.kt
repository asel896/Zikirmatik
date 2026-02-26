package com.example.zikirmatik

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class KuranActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tabSure: TextView
    private lateinit var tabAyet: TextView
    private lateinit var tabCuz: TextView
    private lateinit var editSearch: EditText

    // Son okunan kartı için tanımlamalar
    private lateinit var cardSonOkunan: CardView
    private lateinit var txtSonOkunan: TextView
    private lateinit var btnDevamEt: Button

    private val dinamikListe = mutableListOf<FavoriAyet>()

    private val turkceSureIsimleri = mapOf(
        1 to "Fatiha", 2 to "Bakara", 3 to "Al-i İmran", 4 to "Nisa", 5 to "Maide",
        6 to "En'am", 7 to "A'raf", 8 to "Enfal", 9 to "Tevbe", 10 to "Yunus",
        11 to "Hud", 12 to "Yusuf", 13 to "Ra'd", 14 to "İbrahim", 15 to "Hicr",
        16 to "Nahl", 17 to "İsra", 18 to "Kehf", 19 to "Meryem", 20 to "Taha",
        21 to "Enbiya", 22 to "Hac", 23 to "Mü'minun", 24 to "Nur", 25 to "Furkan",
        26 to "Şuara", 27 to "Neml", 28 to "Kasas", 29 to "Ankebut", 30 to "Rum",
        31 to "Lokman", 32 to "Secde", 33 to "Ahzab", 34 to "Sebe", 35 to "Fatır",
        36 to "Yasin", 37 to "Saffat", 38 to "Sad", 39 to "Zümer", 40 to "Mü'min",
        41 to "Fussilet", 42 to "Şura", 43 to "Zuhruf", 44 to "Duhan", 45 to "Casiye",
        46 to "Ahkaf", 47 to "Muhammed", 48 to "Fetih", 49 to "Hucurat", 50 to "Kaf",
        51 to "Zariyat", 52 to "Tur", 53 to "Necm", 54 to "Kamer", 55 to "Rahman",
        56 to "Vakıa", 57 to "Hadid", 58 to "Mücadele", 59 to "Haşr", 60 to "Mümtehine",
        61 to "Saff", 62 to "Cuma", 63 to "Münafıkun", 64 to "Teğabun", 65 to "Talak",
        66 to "Tahrim", 67 to "Mülk", 68 to "Kalem", 69 to "Hakka", 70 to "Mearic",
        71 to "Nuh", 72 to "Cin", 73 to "Müzzemmil", 74 to "Müddessir", 75 to "Kıyame",
        76 to "İnsan", 77 to "Mürselat", 78 to "Nebe", 79 to "Naziat", 80 to "Abese",
        81 to "Tekvir", 82 to "İnfitar", 83 to "Mutaffifin", 84 to "İnşikak", 85 to "Büruc",
        86 to "Tarık", 87 to "A'la", 88 to "Ğaşiye", 89 to "Fecr", 90 to "Beled",
        91 to "Şems", 92 to "Leyl", 93 to "Duha", 94 to "İnşirah", 95 to "Tin",
        96 to "Alak", 97 to "Kadir", 98 to "Beyyine", 99 to "Zilzal", 100 to "Adiyat",
        101 to "Karia", 102 to "Tekasür", 103 to "Asr", 104 to "Hümeze", 105 to "Fil",
        106 to "Kureyş", 107 to "Maun", 108 to "Kevser", 109 to "Kafirun", 110 to "Nasr",
        111 to "Mesed", 112 to "İhlas", 113 to "Felak", 114 to "Nas"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kuran)

        initViews()
        setupSearch()

        // Sayfa açıldığında sekmeyi seç ve verileri yükle
        selectTab(tabSure)
        sureleriYukle()

        // Kaldığım yeri kontrol et ve göster
        sonOkunaniKontrolEt()

        tabSure.setOnClickListener {
            selectTab(tabSure)
            sureleriYukle()
            editSearch.visibility = View.VISIBLE
            sonOkunaniKontrolEt() // Sureler sekmesine dönünce kartı tekrar kontrol et
        }

        tabAyet.setOnClickListener {
            selectTab(tabAyet)
            dinamikAyetleriYukle()
            editSearch.visibility = View.GONE
            cardSonOkunan.visibility = View.GONE // Diğer sekmelerde gizle
        }

        tabCuz.setOnClickListener {
            selectTab(tabCuz)
            cuzleriGoster()
            editSearch.visibility = View.GONE
            cardSonOkunan.visibility = View.GONE // Diğer sekmelerde gizle
        }
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerViewKuran)
        recyclerView.layoutManager = LinearLayoutManager(this)
        tabSure = findViewById(R.id.tabSure)
        tabAyet = findViewById(R.id.tabAyet)
        tabCuz = findViewById(R.id.tabCuz)
        editSearch = findViewById(R.id.editSearch)

        cardSonOkunan = findViewById(R.id.cardSonOkunan)
        txtSonOkunan = findViewById(R.id.txtSonOkunanSure)
        btnDevamEt = findViewById(R.id.btnDevamEt)

        supportActionBar?.hide()
    }

    private fun sonOkunaniKontrolEt() {
        val sharedPref = getSharedPreferences("ZikirmatikPref", MODE_PRIVATE)
        val sonSureNo = sharedPref.getInt("SON_SURE_NO", -1)
        val sonSureAdi = sharedPref.getString("SON_SURE_ADI", "")

        if (sonSureNo != -1 && !sonSureAdi.isNullOrEmpty()) {
            cardSonOkunan.visibility = View.VISIBLE
            txtSonOkunan.text = "Son Okunan: $sonSureAdi"

            btnDevamEt.setOnClickListener {
                val intent = Intent(this, AyetActivity::class.java)
                intent.putExtra("SURE_NO", sonSureNo)
                intent.putExtra("SURE_ADI", sonSureAdi)
                startActivity(intent)
            }
        } else {
            cardSonOkunan.visibility = View.GONE
        }
    }

    private fun setupSearch() {
        editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val adapter = recyclerView.adapter
                if (adapter is SureAdapter) {
                    adapter.filter(s.toString())
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun selectTab(selectedTab: TextView) {
        listOf(tabSure, tabAyet, tabCuz).forEach {
            it.setTextColor(Color.parseColor("#80FFFFFF"))
            it.setBackgroundResource(0)
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
                Toast.makeText(this@KuranActivity, "Bağlantı hatası!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun dinamikAyetleriYukle() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.alquran.cloud/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(KuranApiService::class.java)

        dinamikListe.clear()
        val rastgeleIdler: List<Int> = List(10) { (1..6236).random() }

        rastgeleIdler.forEach { id ->
            api.topluAyetGetir(id).enqueue(object : Callback<TopluAyetCevap> {
                override fun onResponse(call: Call<TopluAyetCevap>, response: Response<TopluAyetCevap>) {
                    if (response.isSuccessful) {
                        val veriler = response.body()?.data
                        if (veriler != null && veriler.size >= 2) {
                            val arapca = veriler[0].text
                            val meal = veriler[1].text

                            val sureNo = veriler[1].surah.number
                            val turkceSureAdi = turkceSureIsimleri[sureNo] ?: veriler[1].surah.englishName
                            val bilgi = "$turkceSureAdi, ${veriler[1].numberInSurah}"

                            dinamikListe.add(FavoriAyet(arapca, meal, bilgi))
                            recyclerView.adapter = FavoriAyetAdapter(dinamikListe)
                        }
                    }
                }
                override fun onFailure(call: Call<TopluAyetCevap>, t: Throwable) {}
            })
        }
    }

    private fun cuzleriGoster() {
        val cuzAdlari = (1..30).map { "$it. Cüz" }
        recyclerView.adapter = CuzAdapter(cuzAdlari) { }
    }

    // Uygulamaya geri dönüldüğünde son okunanı güncellemek için
    override fun onResume() {
        super.onResume()
        if (tabSure.currentTextColor == Color.WHITE) {
            sonOkunaniKontrolEt()
        }
    }
}