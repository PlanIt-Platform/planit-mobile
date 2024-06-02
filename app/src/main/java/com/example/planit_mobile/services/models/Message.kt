package com.example.planit_mobile.services.models

import java.util.Date

data class Message(
    val id: String,
    val text: String,
    val createdAt: Date?,
    val uId: String?,
    val name: String?
)
