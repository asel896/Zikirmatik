package com.example.zikirmatik

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class SureAdapter(private val sureList: List<SureBilgi>) :
    RecyclerView.Adapter<SureAdapter.SureViewHolder>() {

    private var filtrelenmisListe: List<SureBilgi> = sureList

    // 114 SURENİN TAM TÜRKÇE LİSTESİ (Sıralı)
    private val turkceSureIsimleri = mapOf(
        1 to "Fatiha", 2 to "Bakara", 3 to "Al-i İmran", 4 to "Nisa", 5 to "Maide",
        6 to "En'am", 7 to "A'raf", 8 to "Enfal", 9 to "Tevbe", 10 to "Yunus",
        11 to "Hud", 12 to "Yusuf", 13 to "Ra'd", 14 to "İbrahim", 15 to "Hicr",
        16 to "Nahl", 17 to "İsra", 18 to "Kehf", 19 to "Meryem", 20 to "Taha",
        21 to "Enbiya", 22 to "Hac", 23 to "Mü'minun", 24 to "Nur", 25 to "Furkan",
        26 to "Şuara", 27 to "Neml", 28 to "Kasas", 29 to "Ankebut", 30 to "Rum",
        31 to "Lokman", 32 to "Secde", 33 to "Ahzab", 34 to "Sebe", 35 to "Fatır",
        36 to "Yasin", 37 to "Saffat", 38 to "Sad", 39 to "Zümer", 40 to "Mü'min (Ğafir)",
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

    class SureViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNo: TextView = view.findViewById(R.id.txtSureNo)
        val txtAd: TextView = view.findViewById(R.id.txtSureAdi)
        val txtMeal: TextView = view.findViewById(R.id.txtSureMeal)
        val txtArapca: TextView = view.findViewById(R.id.txtArapcaAd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sure, parent, false)
        return SureViewHolder(view)
    }

    override fun onBindViewHolder(holder: SureViewHolder, position: Int) {
        val sure = filtrelenmisListe[position]
        holder.txtNo.text = sure.number.toString()

        // Numaradan ismi çekiyoruz, bulamazsa orijinali göster (bulamama şansı yok)
        val turkceAd = turkceSureIsimleri[sure.number] ?: sure.englishName
        holder.txtAd.text = turkceAd

        val tur = if (sure.revelationType == "Meccan") "Mekke" else "Medine"
        holder.txtMeal.text = "$tur • ${sure.numberOfAyahs} Ayet"
        holder.txtArapca.text = sure.name

        // SureAdapter.kt içindeki onClick kısmı
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val sharedPref = context.getSharedPreferences("ZikirmatikPref", android.content.Context.MODE_PRIVATE)

            // Son okunan sureyi kaydet
            with(sharedPref.edit()) {
                putInt("SON_SURE_NO", sure.number)
                putString("SON_SURE_ADI", turkceAd)
                apply()
            }

            val intent = Intent(context, AyetActivity::class.java)
            intent.putExtra("SURE_NO", sure.number)
            intent.putExtra("SURE_ADI", turkceAd)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = filtrelenmisListe.size

    fun filter(query: String) {
        val searchtext = query.lowercase(Locale("tr", "TR"))
        filtrelenmisListe = if (searchtext.isEmpty()) {
            sureList
        } else {
            sureList.filter {
                val turkceIsim = (turkceSureIsimleri[it.number] ?: "").lowercase(Locale("tr", "TR"))
                turkceIsim.contains(searchtext) || it.name.contains(searchtext)
            }
        }
        notifyDataSetChanged()
    }
}