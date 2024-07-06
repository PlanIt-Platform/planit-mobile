package com.example.planit_mobile.services.models

data class NearbyEventModel (
    val id: Int,
    val title: String,
    val location: String,
    val latitude: Double,
    val longitude: Double
)