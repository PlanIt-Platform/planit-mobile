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
    const val USER_EVENTS_URL = "${PLANIT_API_URL}/user/events"

    /**
     * Event paths
     */
    const val CREATE_EVENT = "${PLANIT_API_URL}/event"
    private const val GET_EVENT = "${PLANIT_API_URL}/event/{id}"
    private const val USERS_IN_EVENT = "${PLANIT_API_URL}/event/{id}/users"
    const val GET_CATEGORIES = "${PLANIT_API_URL}/event/categories"
    private const val GET_SUBCATEGORIES = "${PLANIT_API_URL}/event/categories/{category}/subcategories"
    private const val JOIN_EVENT = "${PLANIT_API_URL}/event/{id}/join"
    private const val LEAVE_EVENT = "${PLANIT_API_URL}/event/{id}/leave"
    private const val DELETE_EVENT = "${PLANIT_API_URL}/event/{id}"
    private const val EDIT_EVENT = "${PLANIT_API_URL}/event/{id}/edit"
    private const val REMOVE_USER_TASK = "${PLANIT_API_URL}/user/{userId}/event/{eventId}/task/{taskId}"
    private const val ASSIGN_USER_TASK = "${PLANIT_API_URL}/user/{userId}/event/{eventId}/task"
    private const val KICK_USER_FROM_EVENT = "${PLANIT_API_URL}/event/{eventId}/kick/{userId}"
    private const val GET_POLLS = "${PLANIT_API_URL}/event/{id}/polls"
    private const val CREATE_POLL = "${PLANIT_API_URL}/event/{id}/poll"
    private const val POLL = "${PLANIT_API_URL}/event/{eventId}/poll/{pollId}"
    private const val VOTE_POLL = "${PLANIT_API_URL}/event/{eventId}/poll/{pollId}/vote/{optionId}"

    fun getEventPath(id: Int): String = GET_EVENT.replace("{id}", "$id")

    fun searchEventPath(query: String?, limit: Int?, offset: Int?): String {
        val input = if (query != null) "${PLANIT_API_URL}/events?searchInput=${query}"
        else "${PLANIT_API_URL}/events?"
        val limitInput =
            if(query != null) "&" else "" + if (limit != null) "limit=${limit}" else ""
        val offsetInput =
            if(limit != null || query != null) "&" else ""  + if (offset != null) "offset=${offset}" else ""
        return input + limitInput + offsetInput
    }


    fun getSubcategoriesPath(category: String): String =
        GET_SUBCATEGORIES.replace("{category}", category)

    fun getJoinEventPath(id: Int): String = JOIN_EVENT.replace("{id}", "$id")

    fun getUsersInEventPath(id: Int): String = USERS_IN_EVENT.replace("{id}", "$id")

    fun getLeaveEventPath(id: Int): String = LEAVE_EVENT.replace("{id}", "$id")

    fun getDeleteEventPath(id: Int): String = DELETE_EVENT.replace("{id}", "$id")

    fun getEditEventPath(id: Int): String = EDIT_EVENT.replace("{id}", "$id")

    fun getRemoveUserTaskPath(userId: Int, eventId: Int, taskId: Int): String =
        REMOVE_USER_TASK.replace("{userId}", "$userId")
            .replace("{eventId}", "$eventId")
            .replace("{taskId}", "$taskId")

    fun getAssignUserTaskPath(userId: Int, eventId: Int): String =
        ASSIGN_USER_TASK.replace("{userId}", "$userId")
            .replace("{eventId}", "$eventId")

    fun getKickUserFromEventPath(eventId: Int, userId: Int): String =
        KICK_USER_FROM_EVENT.replace("{eventId}", "$eventId")
            .replace("{userId}", "$userId")

    fun getPollsPath(eventId: Int): String = GET_POLLS.replace("{id}", "$eventId")

    fun getCreatePollPath(eventId: Int): String =
        CREATE_POLL.replace("{id}", "$eventId")

    fun getPollPath(eventId: Int, pollId: Int): String =
        POLL.replace("{eventId}", "$eventId")
            .replace("{pollId}", "$pollId")

    fun getVotePollPath(eventId: Int, pollId: Int, optionId: Int): String =
        VOTE_POLL.replace("{eventId}", "$eventId")
            .replace("{pollId}", "$pollId")
            .replace("{optionId}", "$optionId")

}
