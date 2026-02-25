package com.example.zikirmatik

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zikirmatik.R

class SureAdapter(private val sureList: List<SureBilgi>) :
    RecyclerView.Adapter<SureAdapter.SureViewHolder>() {

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
        val sure = sureList[position]
        holder.txtNo.text = sure.number.toString()

        // 1. Önce baştaki Al-, An- gibi ekleri silelim
        var temizAd = sure.englishName.replace(Regex("^A.-"), "")

        // 2. Şimdi hatalı İngilizce harf gruplarını Türkçeye çevirelim
        temizAd = temizAd
            .replace("aa", "a")  // Faatiha -> Fatiha
            .replace("q", "k")   // Baqara -> Bakara
            .replace("Q", "K")   // Quraish -> Kuraish
            .replace("ee", "i")  // Örn: Yaaseen -> Yasin
            .replace("oo", "u")
            .replace("'", "")    // An'aam -> Anaam
            .replace("-", " ")   // Tireleri boşluk yap

        holder.txtAd.text = temizAd

        // Mekke/Medine Türkçeleştirmesi
        val tur = if (sure.revelationType == "Meccan") "Mekke" else "Medine"
        holder.txtMeal.text = "$tur • ${sure.numberOfAyahs} Ayet"

        holder.txtArapca.text = sure.name

        // Tıklama olayı
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, AyetActivity::class.java)
            intent.putExtra("SURE_NO", sure.number)
            intent.putExtra("SURE_ADI", temizAd)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = sureList.size

}