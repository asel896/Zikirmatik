package com.example.zikirmatik

data class SureCevap(
    val data: List<SureBilgi>
)

data class SureBilgi(
    val number: Int,
    val name: String,
    val englishName: String,
    val englishNameTranslation: String,
    val numberOfAyahs: Int,
    val revelationType: String
)