package com.example.planit_mobile.services.models

data class UserEventsResult(
    val userId: Int,
    val username: String,
    val events: List<SearchEventResult>,
)
