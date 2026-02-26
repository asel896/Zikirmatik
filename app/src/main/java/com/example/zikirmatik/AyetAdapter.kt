package com.example.zikirmatik

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AyetAdapter(private val arapcaList: List<AyetDetay>, private val mealList: List<AyetDetay>) :
    RecyclerView.Adapter<AyetAdapter.AyetViewHolder>() {

    class AyetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtArapca = view.findViewById<TextView>(R.id.txtArapcaAyet)
        val txtMeal = view.findViewById<TextView>(R.id.txtMealAyet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AyetViewHolder {
        // BURASI ÖNEMLİ: activity_ayet yerine senin ayet satırı tasarımın (item_ayet_oku gibi) olmalı
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_ayet, parent, false)
        return AyetViewHolder(view)
    }

    override fun onBindViewHolder(holder: AyetViewHolder, position: Int) {
        val arapcaAyet = arapcaList[position]
        val mealAyet = mealList[position]

        holder.txtArapca.text = "${arapcaAyet.text} (${arapcaAyet.numberInSurah})"
        holder.txtMeal.text = mealAyet.text
    }

    override fun getItemCount() = arapcaList.size
}