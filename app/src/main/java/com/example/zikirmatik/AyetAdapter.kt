package com.example.zikirmatik

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class AyetAdapter(private val arapcaList: List<AyetBilgi>, private val mealList: List<AyetBilgi>) :
    RecyclerView.Adapter<AyetAdapter.AyetViewHolder>() {

    class AyetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtArapca = view.findViewById<TextView>(R.id.txtArapcaAyet)
        val txtMeal = view.findViewById<TextView>(R.id.txtMealAyet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AyetViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_ayet, parent, false))

    override fun onBindViewHolder(holder: AyetViewHolder, position: Int) {
        holder.txtArapca.text = "${arapcaList[position].text} (${arapcaList[position].numberInSurah})"
        holder.txtMeal.text = mealList[position].text
    }



    override fun getItemCount() = arapcaList.size
}