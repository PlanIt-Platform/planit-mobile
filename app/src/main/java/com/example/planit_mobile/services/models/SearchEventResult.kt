package com.example.planit_mobile.services.models

data class SearchEventResult(
    val id: Int,
    val title: String,
    val description: String?,
    val category: String?,
    val location: String?,
    val visibility: String,
    val date: String?,
)
