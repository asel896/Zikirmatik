package com.example.zikirmatik

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CuzAdapter(
    private val liste: List<String>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<CuzAdapter.CuzViewHolder>() {

    class CuzViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNo: TextView = view.findViewById(R.id.txtSureNo)
        val txtAd: TextView = view.findViewById(R.id.txtSureAdi)
        val txtBilgi: TextView = view.findViewById(R.id.txtSureMeal)
        val txtArapca: TextView = view.findViewById(R.id.txtArapcaAd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuzViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_sure, parent, false)
        return CuzViewHolder(v)
    }

    override fun onBindViewHolder(holder: CuzViewHolder, position: Int) {
        val cuzNo = position + 1
        holder.txtNo.text = cuzNo.toString()
        holder.txtAd.text = "$cuzNo. Cüz"
        holder.txtBilgi.text = "Kur'an-ı Kerim"
        holder.txtArapca.text = "الجزء"

        holder.itemView.setOnClickListener { onItemClick(cuzNo) }
    }

    override fun getItemCount(): Int = liste.size
}