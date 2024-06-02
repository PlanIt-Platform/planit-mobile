package com.example.planit_mobile.services.models

data class PollModel(
    val id: Int,
    val title: String,
    val created_at: String,
    val duration: Int,
    val options: List<PollOption>
)