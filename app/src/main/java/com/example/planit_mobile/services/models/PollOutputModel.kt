package com.example.planit_mobile.services.models

data class PollOutputModel(
    val id: Int,
    val title: String,
    val created_at: String,
    val duration: Int,
    val options: String
)
