package com.example.planit_mobile.services.models

data class EventModel(
    val id: Int,
    val title: String,
    val description: String?,
    val category: String,
    val subcategory: String?,
    val location: String?,
    val visibility: String,
    val date: String,
    val priceAmount: Double?,
    val endDate: String?,
    val priceCurrency: String?
)
