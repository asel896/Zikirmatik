package com.example.zikirmatik

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog // Eklendi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SadakaActivity : AppCompatActivity() {

    private var secilenTutar: Double = 0.0
    private lateinit var adapter: SadakaAdapter
    private var sadakaListesi = mutableListOf<SadakaKaydi>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_sadaka)

        val editNiyet = findViewById<EditText>(R.id.editNiyet)
        val editOzelTutar = findViewById<EditText>(R.id.editOzelTutar)
        val btn10 = findViewById<Button>(R.id.btn10TL)
        val btn20 = findViewById<Button>(R.id.btn20TL)
        val btn50 = findViewById<Button>(R.id.btn50TL)
        val btnKaydet = findViewById<Button>(R.id.btnKaydet)
        val recyclerView = findViewById<RecyclerView>(R.id.rvSadakaListesi)

        verileriYukle()

        // ADAPTER BURADA GÜNCELLENDİ
        adapter = SadakaAdapter(sadakaListesi) { position ->
            // Liste elemanına tıklandığında bu blok çalışır
            kaydiSil(position)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btn10.setOnClickListener { secilenTutar = 10.0; editOzelTutar.text.clear(); Toast.makeText(this, "10 TL seçildi", Toast.LENGTH_SHORT).show() }
        btn20.setOnClickListener { secilenTutar = 20.0; editOzelTutar.text.clear(); Toast.makeText(this, "20 TL seçildi", Toast.LENGTH_SHORT).show() }
        btn50.setOnClickListener { secilenTutar = 50.0; editOzelTutar.text.clear(); Toast.makeText(this, "50 TL seçildi", Toast.LENGTH_SHORT).show() }

        btnKaydet.setOnClickListener {
            val niyetText = editNiyet.text.toString().trim()
            val ozelTutarText = editOzelTutar.text.toString().trim()

            if (ozelTutarText.isNotEmpty()) {
                secilenTutar = ozelTutarText.toDoubleOrNull() ?: 0.0
            }

            if (niyetText.isEmpty()) {
                Toast.makeText(this, "Niyetinizi yazın", Toast.LENGTH_SHORT).show()
            } else if (secilenTutar <= 0.0) {
                Toast.makeText(this, "Geçerli bir tutar belirleyin", Toast.LENGTH_SHORT).show()
            } else {
                val yeniKayit = SadakaKaydi(niyet = niyetText, tutar = secilenTutar)
                sadakaListesi.add(0, yeniKayit)

                verileriKaydet()
                adapter.notifyItemInserted(0)
                recyclerView.scrollToPosition(0)

                editNiyet.text.clear()
                editOzelTutar.text.clear()
                secilenTutar = 0.0
                Toast.makeText(this, "Günlüğe kaydedildi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // SİLME İŞLEMİ FONKSİYONU
    private fun kaydiSil(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Kaydı Sil")
            .setMessage("Bu niyet kaydını silmek istediğinize emin misiniz?")
            .setPositiveButton("Evet") { _, _ ->
                sadakaListesi.removeAt(position)
                verileriKaydet() // Hafızayı güncelle
                adapter.notifyItemRemoved(position)
                // Listenin geri kalanının pozisyonlarını güncellemesi için:
                adapter.notifyItemRangeChanged(position, sadakaListesi.size)
                Toast.makeText(this, "Silindi", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("İptal", null)
            .show()
    }

    private fun verileriKaydet() {
        val sharedPref = getSharedPreferences("SadakaHafizasi", MODE_PRIVATE)
        val json = Gson().toJson(sadakaListesi)
        sharedPref.edit().putString("sadaka_listesi", json).apply()
    }

    private fun verileriYukle() {
        val sharedPref = getSharedPreferences("SadakaHafizasi", MODE_PRIVATE)
        val json = sharedPref.getString("sadaka_listesi", null)
        if (json != null) {
            val type = object : TypeToken<MutableList<SadakaKaydi>>() {}.type
            sadakaListesi = Gson().fromJson(json, type)
        }
    }
}