package com.example.zikirmatik

//import java.util.UUID

data class SadakaKaydi(
    //val id: String = UUID.randomUUID().toString(),
    val niyet: String,
    val tutar: Double,
    val tarih: Long = System.currentTimeMillis()
)


