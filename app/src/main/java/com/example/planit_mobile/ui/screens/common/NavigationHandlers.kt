package com.example.planit_mobile.ui.screens.common

data class NavigationHandlers(
    val onInfoRequested: (() -> Unit)? = null,
    val onProfileRequested: (() -> Unit)? = null,
    val onHomeRequested: (() -> Unit)? = null,
    val onEventsRequested: (() -> Unit)? = null
)