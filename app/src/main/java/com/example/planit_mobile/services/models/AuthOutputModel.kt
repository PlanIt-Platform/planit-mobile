package com.example.planit_mobile.services.models

data class AuthOutputModel(
    val id: Int,
    val accessToken: String,
    val refreshToken: String
)