package com.example.zikirmatik

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url
import java.util.*

// 1. Veri Modelleri
data class IcerikListesi(val gunluk_icerikler: List<GunlukVeri>)
data class GunlukVeri(
    val ayet: String,
    val ayetKaynak: String,
    val hadis: String,
    val hadisKaynak: String,
    val soz: String,
    val sozKaynak: String
)

// 2. Retrofit Arayüzü
interface IslamaApi {
    @GET
    fun verileriGetir(@Url url: String): Call<IcerikListesi>
}

class TakvimActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_takvim)


        // XML Tanımlamaları
        val txtAyet = findViewById<TextView>(R.id.txtGununAyeti)
        val txtHadis = findViewById<TextView>(R.id.txtGununHadisi)
        val txtSoz = findViewById<TextView>(R.id.txtGununSozu)

        // Retrofit Kurulumu
        val retrofit = Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(IslamaApi::class.java)

        // Kendi Raw Linkini Buraya Yapıştır
        val rawUrl = "https://raw.githubusercontent.com/asel896/Zikirmatik/main/icerik.json"

        // İnternetten Veri Çekme İşlemi
        api.verileriGetir(rawUrl).enqueue(object : Callback<IcerikListesi> {
            override fun onResponse(call: Call<IcerikListesi>, response: Response<IcerikListesi>) {
                if (response.isSuccessful) {
                    val tumListe = response.body()?.gunluk_icerikler

                    if (!tumListe.isNullOrEmpty()) {
                        // Günün tarihine göre seçim (Yılın kaçıncı günü)
                        val takvim = Calendar.getInstance()
                        val bugun = takvim.get(Calendar.DAY_OF_YEAR)

                        // Mod alarak 60 maddelik listeden seçim yapıyoruz
                        val secilenIndex = bugun % tumListe.size
                        val veri = tumListe[secilenIndex]

                        // Ekrana Yazdırma
                        txtAyet.text = "“${veri.ayet}”\n\n— ${veri.ayetKaynak}"
                        txtHadis.text = "“${veri.hadis}”\n\n— ${veri.hadisKaynak}"
                        txtSoz.text = "“${veri.soz}”\n\n— ${veri.sozKaynak}"
                    }
                }
            }

            override fun onFailure(call: Call<IcerikListesi>, t: Throwable) {
                // İnternet hatası durumunda mesaj
                txtAyet.text = "Veriler yüklenemedi. Lütfen internetinizi kontrol edin."
                txtHadis.text = "Sabır, kurtuluşun anahtarıdır."
                txtSoz.text = "Hayat devam ediyor..."
                Toast.makeText(this@TakvimActivity, "Bağlantı Hatası!", Toast.LENGTH_SHORT).show()
            }
        })
    }
}