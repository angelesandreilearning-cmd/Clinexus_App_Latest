package com.example.clinexusapp.model

data class Region(
    val code: String,
    val name: String,
    val regionName: String
)

data class Province(
    val code: String,
    val name: String,
    val regionCode: String
)

data class City(
    val code: String,
    val name: String,
    val provinceCode: String? = null,
    val regionCode: String? = null
)

data class Barangay(
    val code: String,
    val name: String,
    val cityCode: String? = null,
    val municipalityCode: String? = null
)
