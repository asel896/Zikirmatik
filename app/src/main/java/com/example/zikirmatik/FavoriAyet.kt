package com.example.zikirmatik

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class FavoriAyet(val arapca: String, val meal: String, val bilgi: String)

class FavoriAyetAdapter(private val liste: List<FavoriAyet>) :
    RecyclerView.Adapter<FavoriAyetAdapter.FavViewHolder>() {

    class FavViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val arapca = view.findViewById<TextView>(R.id.txtFavArapca)
        val meal = view.findViewById<TextView>(R.id.txtFavMeal)
        val bilgi = view.findViewById<TextView>(R.id.txtFavSureNo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FavViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_favori_ayet, parent, false))

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val ayet = liste[position]

        // Altta Meal (Daha küçük ve gri tonlarda)
        holder.meal.text = ayet.meal


        // Üstte Arapça (Büyük ve Sağda)
        holder.arapca.text = ayet.arapca


        // En altta Sure Bilgisi
        holder.bilgi.text = ayet.bilgi
    }

    override fun getItemCount() = liste.size
}