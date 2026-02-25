package com.example.zikirmatik

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// onItemClick parametresini ekledik
class SadakaAdapter(
    private val liste: List<SadakaKaydi>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<SadakaAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val niyet: TextView = view.findViewById(R.id.txtListeNiyet)
        val tutar: TextView = view.findViewById(R.id.txtListeTutar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sadaka, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = liste[position]
        holder.niyet.text = item.niyet
        holder.tutar.text = "${item.tutar} TL"

        // Kartın kendisine tıklandığında Activity'deki fonksiyonu tetikler
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount() = liste.size
}