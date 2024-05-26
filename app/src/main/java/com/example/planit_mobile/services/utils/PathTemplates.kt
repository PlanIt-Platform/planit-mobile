package com.example.planit_mobile.services.utils

object PathTemplates {
    const val PLANIT_API_URL = "http://10.0.2.2:1904/api-planit"

    /**
     * User paths
     */
    const val REGISTER_URL = "${PLANIT_API_URL}/register"
    const val LOGIN_URL = "${PLANIT_API_URL}/login"
    const val LOGOUT_URL = "${PLANIT_API_URL}/logout"
    const val USER_URL = "${PLANIT_API_URL}/user"

    /**
     * Event paths
     */
    const val CREATE_EVENT = "${PLANIT_API_URL}/event"
    private const val GET_EVENT = "${PLANIT_API_URL}/event/{id}"
    const val USERS_IN_EVENT = "${PLANIT_API_URL}/event/{id}/users"
    const val GET_CATEGORIES = "${PLANIT_API_URL}/event/categories"
    private const val GET_SUBCATEGORIES = "${PLANIT_API_URL}/event/categories/{category}/subcategories"

    fun getEventPath(id: String): String {
        return GET_EVENT.replace("{id}", id)
    }

    fun getSubcategoriesPath(category: String): String {
        return GET_SUBCATEGORIES.replace("{category}", category)
    }
}
