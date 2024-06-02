package com.example.planit_mobile.ui.screens.eventDetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.planit_mobile.PlanItDependencyProvider
import com.example.planit_mobile.ui.screens.common.Error
import com.example.planit_mobile.ui.screens.common.ErrorPopup
import com.example.planit_mobile.ui.screens.common.LoadingScreen
import com.example.planit_mobile.ui.screens.common.idle
import com.example.planit_mobile.ui.screens.common.loading
import com.example.planit_mobile.ui.theme.PlanitMobileTheme
import kotlinx.coroutines.launch

class EventDetailsActivity : ComponentActivity() {

    private val dependencies by lazy { application as PlanItDependencyProvider }

    private val viewModel by viewModels<EventDetailsViewModel> {
        EventDetailsViewModel.factory(dependencies.eventService, dependencies.userService, dependencies.sessionStorage)
    }

    companion object {
        private const val EXTRA_EVENT_ID = "extra_event_id"
        private const val EXTRA_VISIBILITY = "extra_visibility"

        fun navigateTo(origin: Activity, eventID: Int, visibility: String?) {
            val intent = Intent(origin, EventDetailsActivity::class.java).apply {
                putExtra(EXTRA_EVENT_ID, eventID)
                putExtra(EXTRA_VISIBILITY, visibility)
            }
            origin.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val eventId = intent.getIntExtra(EXTRA_EVENT_ID, 0)
        val visibility = intent.getStringExtra(EXTRA_VISIBILITY)

        // If the visibility is not known, make a request to find it out
        if (visibility == null) {
            // TODO: Make a request to find out the visibility
        }

        viewModel.listenForMessages(eventId)

        lifecycleScope.launch {
            viewModel.eventDeletedState.collect { eventDeleted ->
                if (eventDeleted) {
                    Log.d("EventDetailsActivity", "Event deleted, finishing activity")
                    finish()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.getUserID()
            viewModel.getUser()
        }

        viewModel.isUserInEvent(eventId)

            setContent {
            PlanitMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    viewModel.getCategories()
                    val loadState = viewModel.loadState.collectAsState(initial = idle()).value
                    val isUserInEvent = viewModel.isUserInEvent.collectAsState(initial = false).value
                    val userErrorMessage =
                        viewModel.errorState.collectAsState(initial = Error("")).value.message
                    val usersInEvent =
                        viewModel.usersInEvent.collectAsState(initial = emptyList()).value
                    val isUserOrganizer = viewModel.isUserOrganizer.collectAsState(initial = false).value
                    val categories = viewModel.categoriesState.collectAsState(initial = emptyList()).value
                    val subCategories = viewModel.subcategoriesState.collectAsState(initial = emptyList()).value
                    val userIDState = viewModel.userIDState.collectAsState(initial = null).value
                    val polls = viewModel.pollsState.collectAsState(initial = emptyList()).value
                    val messages = viewModel.messagesState.collectAsState(initial = emptyList()).value

                    if(loadState == loading()){
                        LoadingScreen(
                            onBackRequested = { finish() }
                        )
                    } else {
                        if (!isUserInEvent && visibility == "Private") {
                            UserNotInPrivateEventScreen(joinEvent =
                            { password ->
                                viewModel.joinEvent(eventId, password)
                            },
                                onBackRequested = { finish() }
                            )
                        } else {
                            viewModel.getEventDetails(eventId)
                            if(loadState == loading()){
                                LoadingScreen(
                                    onBackRequested = { finish() }
                                )
                            } else {
                                val eventDetails =
                                    viewModel.eventDetails.collectAsState(initial = null).value
                                EventDetailsScreen(
                                    onBackRequested = { finish() },
                                    eventDetails = eventDetails,
                                    isUserInEvent = isUserInEvent,
                                    joinEvent = { password ->
                                        viewModel.joinEvent(eventId, password)
                                    },
                                    usersInEvent = usersInEvent,
                                    isUserOrganizer = isUserOrganizer,
                                    leaveEvent = {
                                        viewModel.leaveEvent(eventId)
                                    },
                                    editEvent = { name, description, category, subCategory, location, visibility, date, endDate, price, password ->
                                        viewModel.editEvent(eventId, name, description, category, subCategory, location, visibility, date, endDate, price, password)
                                    },
                                    deleteEvent = {
                                        viewModel.deleteEvent(eventId)
                                    },
                                    categories = categories,
                                    onCategorySelected = { category ->
                                        viewModel.getSubcategories(category)
                                    },
                                    subCategories = subCategories,
                                    updateUsersInEvent = {
                                        viewModel.getUsersInEventAndIsUserInEvent(eventId, true)
                                    },
                                    removeUserTask = { userID, taskID ->
                                        viewModel.removeUserTask(eventId, userID, taskID)
                                    },
                                    assignUserTask = { userID, taskName ->
                                        viewModel.assignUserTask(eventId, userID, taskName)
                                    },
                                    kickUser = { userID ->
                                        viewModel.kickUserFromEvent(eventId, userID)
                                    },
                                    userID = userIDState,
                                    getPolls = {
                                        viewModel.getPolls(eventId)
                                    },
                                    polls = polls,
                                    createPoll = {pollTitle, duration, options ->
                                        viewModel.createPoll(eventId, pollTitle, duration, options)
                                    },
                                    deletePoll = {pollID ->
                                        viewModel.deletePoll(eventId, pollID)
                                    },
                                    voteOnPoll = {pollID, optionID ->
                                        viewModel.voteOnPoll(eventId, pollID, optionID)
                                    },
                                    sendMessage = { message ->
                                        lifecycleScope.launch {
                                            viewModel.sendMessage(eventId, message)
                                        }
                                    },
                                    messages = messages
                                )
                            }
                        }
                        ErrorPopup(
                            showDialog = userErrorMessage != "",
                            errorMessage = userErrorMessage
                        ) {
                            viewModel.dismissError()
                        }
                    }
                }
            }
        }
    }

}