package com.example.planit_mobile.services.models

data class EventModel(
    val id: Int,
    val title: String,
    val description: String?,
    val category: String,
    val locationType: String?,
    val location: String?,
    val latitude: String?,
    val longitude: String?,
    val visibility: String,
    val date: String,
    val priceAmount: Double?,
    val endDate: String?,
    val priceCurrency: String?,
    val code: String
)
