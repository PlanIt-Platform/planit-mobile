package com.example.planit_mobile.ui.screens.eventDetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.sessionStorage.SessionDataStore
import com.example.planit_mobile.domain.User
import com.example.planit_mobile.services.EventService
import com.example.planit_mobile.services.UserService
import com.example.planit_mobile.services.models.EventModel
import com.example.planit_mobile.services.models.Message
import com.example.planit_mobile.services.models.PollModel
import com.example.planit_mobile.services.models.UsersInEvent
import com.example.planit_mobile.services.utils.launchAndAuthenticateRequest
import com.example.planit_mobile.services.utils.launchAndRequest
import com.example.planit_mobile.ui.screens.common.Error
import com.example.planit_mobile.ui.screens.common.LoadState
import com.example.planit_mobile.ui.screens.common.errorMessage
import com.example.planit_mobile.ui.screens.common.idle
import com.example.planit_mobile.ui.screens.common.loaded
import com.example.planit_mobile.ui.screens.common.loading
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventDetailsViewModel(
    private val service: EventService,
    private val userService: UserService,
    private val sessionStorage: SessionDataStore
) : ViewModel() {

    companion object {
        fun factory(service: EventService, userService: UserService, sessionStorage: SessionDataStore) = viewModelFactory {
            initializer { EventDetailsViewModel(service, userService, sessionStorage) }
        }
    }

    private val loadStateFlow : MutableStateFlow<LoadState<Any?>> = MutableStateFlow(idle())
    private val errorStateFlow: MutableStateFlow<Error> = MutableStateFlow(Error(""))
    private val usersInEventFlow: MutableStateFlow<List<UsersInEvent>?> = MutableStateFlow(null)
    private val eventDetailsFlow: MutableStateFlow<EventModel?> = MutableStateFlow(null)
    private val isUserInEventFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val isUserOrganizerFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val eventDeleted : MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val categoriesFlow: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private val subcategoriesFlow: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    private val userIDFlow: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val pollsFlow: MutableStateFlow<List<PollModel>?> = MutableStateFlow(null)
    private val userInfo: MutableStateFlow<User?> = MutableStateFlow(null)
    private val messagesFlow: MutableStateFlow<List<Message>> = MutableStateFlow(emptyList())

    val loadState: Flow<LoadState<Any?>>
        get() = loadStateFlow.asStateFlow()
    val errorState: Flow<Error>
        get() = errorStateFlow.asStateFlow()
    val eventDetails: Flow<EventModel?>
        get() = eventDetailsFlow.asStateFlow()
    val isUserInEvent: Flow<Boolean>
        get() = isUserInEventFlow.asStateFlow()
    val usersInEvent: Flow<List<UsersInEvent>?>
        get() = usersInEventFlow.asStateFlow()
    val isUserOrganizer: Flow<Boolean>
        get() = isUserOrganizerFlow.asStateFlow()
    val eventDeletedState: Flow<Boolean>
        get() = eventDeleted.asStateFlow()
    val categoriesState: Flow<List<String>>
        get() = categoriesFlow.asStateFlow()
    val subcategoriesState: Flow<List<String>>
        get() = subcategoriesFlow.asStateFlow()
    val userIDState: Flow<Int?>
        get() = userIDFlow.asStateFlow()
    val pollsState: Flow<List<PollModel>?>
        get() = pollsFlow.asStateFlow()
    val messagesState: Flow<List<Message>>
        get() = messagesFlow.asStateFlow()

    fun isUserInEvent(eventID: Int) {
        loadStateFlow.value = loading()
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, _ ->
                userService.getUserEvents(userAccessToken, userRefreshToken)
            },
            onSuccess = {
                if(it.events.any { event -> event.id == eventID }) getUsersInEventAndIsUserInEvent(eventID)
                else {
                    isUserInEventFlow.value = false
                    loadStateFlow.value = loaded(null)
                }
            },
            onFailure = {
                errorStateFlow.value = errorMessage(it.message.toString())
                loadStateFlow.value = idle()
            },
            sessionStorage = sessionStorage
        )
    }

    fun getUsersInEventAndIsUserInEvent(eventID: Int, update: Boolean = false) {
        if (!update){ loadStateFlow.value = loading() }
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, _ ->
                service.getUsersInEvent(eventID, userAccessToken, userRefreshToken)
            },
            onSuccess = {
                usersInEventFlow.value = it.users
                usersInEventFlow.value?.let { e->
                    isUserInEventFlow.value = e.any { user -> user.id == sessionStorage.getUserID() }
                }
                if(!update)loadStateFlow.value = loaded(null)
                isUserOrganizerFlow.value = it.users.any {
                    user -> user.taskName == "Organizer" && user.id == sessionStorage.getUserID()
                }
            },
            onFailure = {
                errorStateFlow.value = errorMessage(it.message.toString())
                loadStateFlow.value = idle()
            },
            sessionStorage = sessionStorage
        )
    }

    fun getEventDetails(eventID: Int) {
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, _ ->
                service.fetchEventInfo(eventID, userAccessToken, userRefreshToken)
            },
            onSuccess = {
                loadStateFlow.value = loaded(null)
                eventDetailsFlow.value = it
            },
            onFailure = {
                errorStateFlow.value = errorMessage(it.message.toString())
                loadStateFlow.value = idle()
                viewModelScope.launch { sessionStorage.clearSession() }
            },
            sessionStorage = sessionStorage
        )
    }

    fun joinEvent(eventID: Int, password: String) {
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, _ ->
                service.joinEvent(userAccessToken, userRefreshToken, eventID, password)
            },
            onSuccess = {
                isUserInEventFlow.value = true
            },
            onFailure = {
                errorStateFlow.value = errorMessage(it.message.toString())
            },
            sessionStorage = sessionStorage
        )
    }

    fun leaveEvent(eventID: Int) {
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, _ ->
                service.leaveEvent(userAccessToken, userRefreshToken, eventID)
            },
            onSuccess = {
                isUserInEventFlow.value = false
                getUsersInEventAndIsUserInEvent(eventID)
            },
            onFailure = {
                errorStateFlow.value = errorMessage(it.message.toString())
            },
            sessionStorage = sessionStorage
        )
    }

    fun deleteEvent(eventID: Int) {
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, _ ->
                service.deleteEvent(userAccessToken, userRefreshToken, eventID)
            },
            onSuccess = {
                eventDeleted.value = true
                val firebaseDB = Firebase.firestore
                firebaseDB.collection("events/${eventID}/messages").get().addOnSuccessListener {
                    for (document in it.documents) {
                        document.reference.delete()
                    }
                }
            },
            onFailure = {
                errorStateFlow.value = errorMessage(it.message.toString())
            },
            sessionStorage = sessionStorage
        )
    }

    fun getCategories() {
        launchAndRequest(
            request = { service.getCategories() },
            onSuccess = {
                categoriesFlow.value = it
            },
            onFailure = { errorStateFlow.value = errorMessage(it.message.toString()) },
        )
    }

    fun getSubcategories(category: String) {
        launchAndRequest(
            request = {
                val validURICategory = category.replace(" ", "-")
                service.getSubcategories(validURICategory)
            },
            onSuccess = {
                subcategoriesFlow.value = it
            },
            onFailure = {
                errorStateFlow.value = errorMessage(it.message.toString())
            },
        )
    }

    fun editEvent(
        eventID: Int,
        name: String,
        description: String?,
        category: String,
        subCategory: String?,
        location: String?,
        visibility: String,
        date: String,
        endDate: String?,
        price: String,
        password: String
    ) {
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, _ ->
                service.editEvent(
                    userAccessToken,
                    userRefreshToken,
                    eventID,
                    name,
                    description,
                    category,
                    subCategory,
                    location,
                    visibility,
                    date,
                    endDate,
                    price,
                    password
                )
            },
            onSuccess = {
                getEventDetails(eventID)
            },
            onFailure = {
                errorStateFlow.value = errorMessage(it.message.toString())
            },
            sessionStorage = sessionStorage
        )
    }

    fun removeUserTask(
        eventID: Int,
        userID: Int,
        taskId: Int
    ) {
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, _ ->
                service.removeUserTask(userAccessToken, userRefreshToken, userID, eventID, taskId)
            },
            onSuccess = {
                getUsersInEventAndIsUserInEvent(eventID, true)
            },
            onFailure = {
                errorStateFlow.value = errorMessage(it.message.toString())
            },
            sessionStorage = sessionStorage
        )
    }

    fun assignUserTask(
        eventID: Int,
        userID: Int,
        taskName: String
    ) {
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, _ ->
                service.assignUserTask(userAccessToken, userRefreshToken, userID, eventID, taskName)
            },
            onSuccess = {
                getUsersInEventAndIsUserInEvent(eventID, true)
            },
            onFailure = {
                errorStateFlow.value = errorMessage(it.message.toString())
            },
            sessionStorage = sessionStorage
        )
    }

    fun kickUserFromEvent(
        eventID: Int,
        userID: Int
    ) {
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, _ ->
                service.kickUserFromEvent(userAccessToken, userRefreshToken, userID, eventID)
            },
            onSuccess = {
                getUsersInEventAndIsUserInEvent(eventID, true)
            },
            onFailure = {
                errorStateFlow.value = errorMessage(it.message.toString())
            },
            sessionStorage = sessionStorage
        )
    }

    fun getPolls(eventID: Int) {
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, _ ->
                service.getPolls(userAccessToken, userRefreshToken, eventID)
            },
            onSuccess = {
                Log.d("EVENT polls", it.toString())
                pollsFlow.value = it
            },
            onFailure = {
                errorStateFlow.value = errorMessage(it.message.toString())
            },
            sessionStorage = sessionStorage
        )
    }

    fun createPoll(eventID: Int, title: String, duration: String, options: List<String>) {
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, _ ->
                service.createPoll(userAccessToken, userRefreshToken, eventID, title, duration, options)
            },
            onSuccess = {
                getPolls(eventID)
            },
            onFailure = {
                errorStateFlow.value = errorMessage(it.message.toString())
            },
            sessionStorage = sessionStorage
        )
    }

    fun deletePoll(eventID: Int, pollID: Int) {
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, _ ->
                service.deletePoll(userAccessToken, userRefreshToken, eventID, pollID)
            },
            onSuccess = {
                getPolls(eventID)
            },
            onFailure = {
                errorStateFlow.value = errorMessage(it.message.toString())
            },
            sessionStorage = sessionStorage
        )
    }

    fun voteOnPoll(eventID: Int, pollID: Int, optionID: Int){
        launchAndAuthenticateRequest(
            request = { userAccessToken, userRefreshToken, _ ->
                service.voteOnPoll(userAccessToken, userRefreshToken, eventID, pollID, optionID)
            },
            onSuccess = {
                getPolls(eventID)
            },
            onFailure = {
                errorStateFlow.value = errorMessage(it.message.toString())
            },
            sessionStorage = sessionStorage
        )
    }

    suspend fun getUser(){
        val userID = sessionStorage.getUserID()
        if (userID != null){
            launchAndAuthenticateRequest(
                request = { userAccessToken, userRefreshToken, _ ->
                    userService.fetchUserInfo(userID, userAccessToken, userRefreshToken)
                },
                onSuccess = {
                    userInfo.value = it
                },
                onFailure = {
                    errorStateFlow.value = errorMessage(it.message.toString())
                },
                sessionStorage = sessionStorage
            )
        }
    }

    suspend fun sendMessage (
        eventID: Int,
        message: String
    ) {
        val firebaseDB = Firebase.firestore
        if (userInfo.value != null){
            firebaseDB.collection("events/${eventID}/messages").add(
                hashMapOf(
                    "text" to message,
                    "createdAt" to FieldValue.serverTimestamp(),
                    "uId" to sessionStorage.getUserID(),
                    "name" to userInfo.value!!.name
                )
            )
        }
    }

    fun listenForMessages(eventID: Int) {
        val firebaseDB = Firebase.firestore
        val messagesCollection = firebaseDB.collection("events/${eventID}/messages")

        messagesCollection
            .orderBy("createdAt")
            .limit(100)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.w("TAG", "Listen failed.", error)
                    return@addSnapshotListener
                }

                val messages = value?.documents?.map { doc ->
                    Message(
                        id = doc.id,
                        text = doc.getString("text") ?: "",
                        createdAt = doc.getDate("createdAt"),
                        uId = doc.get("uId").toString(),
                        name = doc.getString("name")
                    )
                } ?: emptyList()

                messagesFlow.value = messages
            }
    }

    suspend fun getUserID() {
        userIDFlow.value = sessionStorage.getUserID()
    }

    fun dismissError() {
        errorStateFlow.value = Error("")
    }

}