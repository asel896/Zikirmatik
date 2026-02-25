package com.example.zikirmatik

data class AyetCevap(val data: AyetData)
data class AyetData(val ayahs: List<AyetBilgi>, val name: String)
data class AyetBilgi(
    val numberInSurah: Int,
    val text: String // Arapça metin
)