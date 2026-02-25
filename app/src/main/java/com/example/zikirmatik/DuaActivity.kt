package com.example.zikirmatik // Eğer paket adın farklıysa burayı düzelt

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray

class DuaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dua)

        val duaText = findViewById<TextView>(R.id.textViewDuaIcerik)
        val kaynakText = findViewById<TextView>(R.id.textDuaKaynak)
        val yeniDuaBtn = findViewById<Button>(R.id.btnYeniDua)

        // Sayfa ilk açıldığında bir dua göster
        duayiGetir(duaText, kaynakText)

        // Butona basıldığında yeni bir rastgele dua getir
        yeniDuaBtn.setOnClickListener {
            duayiGetir(duaText, kaynakText)
        }
    }

    private fun duayiGetir(duaView: TextView, kaynakView: TextView) {
        try {
            // Assets klasöründeki dualar.json dosyasını oku
            val jsonString = assets.open("dualar.json").bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)

            // Rastgele bir sayı seç
            val rastgeleIndeks = (0 until jsonArray.length()).random()
            val secilenObje = jsonArray.getJSONObject(rastgeleIndeks)

            // Ekrana yazdır
            duaView.text = secilenObje.getString("dua")
            kaynakView.text = "- ${secilenObje.getString("kaynak")}"

        } catch (e: Exception) {
            duaView.text = "Dualar yüklenirken bir hata oluştu."
            e.printStackTrace()
        }
    }
}