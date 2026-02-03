package com.example.zikirmatik

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ZikirListeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zikir_liste)

        val listView = findViewById<ListView>(R.id.listViewZikirler)

        // Eklediğimiz 10 Zikir
        val zikirler = arrayOf(
            "+ Kendin Yaz...",
            "Sübhanallah", "Elhamdülillah", "Lâ ilâhe illallâh",
            "Allâhü ekber", "Lâ havle velâ kuvvete illâ billâh",
            "Estağfirullah", "Sübhanallahi ve bi-hamdihi",
            "Sübhânallâhi velhamdülillâhi velâ ilâhe illallahü vallâhü ekber",
            "Allahümme salli alâ Muhammed", "Hasbünallâhu ve ni'mel vekîl"
        )

        val adapter = ArrayAdapter(this, R.layout.zikir_satir_item, zikirler)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent()
            intent.putExtra("secilen_zikir", zikirler[position])
            setResult(RESULT_OK, intent)
            finish() // Sayfayı kapat ve geri dön
        }

        // onCreate içine ekle:
        val editOzelZikir = findViewById<EditText>(R.id.editOzelZikirYaz)
        val btnEkle = findViewById<Button>(R.id.btnOzelZikirEkle)

// 1. Mevcut Listeye Tıklama (Hali hazırda olan kodun)
        listView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent()
            intent.putExtra("secilen_zikir", zikirler[position])
            setResult(RESULT_OK, intent)
            finish()
        }

// 2. Kendi Yazdığın Zikri Ekleme Butonu
        btnEkle.setOnClickListener {
            val ozelMetin = editOzelZikir.text.toString()
            if (ozelMetin.isNotEmpty()) {
                val intent = Intent()
                intent.putExtra("secilen_zikir", ozelMetin) // Yazdığın metni gönderir
                setResult(RESULT_OK, intent)
                finish()
            } else {
                editOzelZikir.error = "Lütfen bir zikir yazın"
            }
        }
    }
}