package com.example.planit_mobile.ui.screens.searchEvent


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planit_mobile.services.models.SearchEventResult
import com.example.planit_mobile.ui.screens.common.BotBar
import com.example.planit_mobile.ui.screens.common.NavigationHandlers
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.window.Dialog

@Composable
fun SearchEventScreen(
    onProfileRequested: () -> Unit,
    onHomeRequested: () -> Unit,
    onEventsRequested: () -> Unit,
    onNearMeRequested: () -> Unit,
    onSearch: (String?) -> Unit,
    events: List<SearchEventResult>,
    categories: List<String>,
    onEventClick: (SearchEventResult) -> Unit,
    searchEventCode: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            BotBar(
                navigation =
                NavigationHandlers(
                    onProfileRequested = onProfileRequested,
                    onHomeRequested = onHomeRequested,
                    onEventsRequested = onEventsRequested
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(bottom = 10.dp),
                onClick = { showDialog = true },
                shape = CircleShape,
                containerColor = Color(0xFF3543C5),
            ){
                Icon(Icons.Default.AddCircle,
                    contentDescription = "Create Event",
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .height(50.dp)
            ) {
                if (!isExpanded) {
                    Text(
                        text = "What are you looking for?",
                        color = Color.White,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                SearchBarAnimation(
                    onSearch = onSearch,
                    isExpanded = isExpanded,
                    setExpanded = { isExpanded = !isExpanded }
                )
                if (!isExpanded) NearMe(onNearMeRequested)
            }
            if (categories.isNotEmpty()) {
                Box {
                    var selectedCategory by remember { mutableStateOf("") }
                    LazyRow {
                        items(categories) { category ->
                            Tab(
                                text = { Text(text = category, color = Color.White) },
                                selected = selectedCategory == category,
                                onClick = { selectedCategory = category }
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                LazyColumn {
                    items(events) { event ->
                        EventCard(event = event, onEventClick = onEventClick)
                    }
                }
            }
            if(showDialog){
                SearchEventDialog(
                    onDismiss = { showDialog = false },
                    searchEventCode = { code -> searchEventCode(code) }
                )
            }
        }
    }
}

@Composable
fun SearchBarAnimation(
    onSearch: (String) -> Unit,
    isExpanded: Boolean,
    setExpanded: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var finalSearchText by remember { mutableStateOf("") }
    Box( modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 35.dp), contentAlignment = Alignment.CenterEnd) {
        AnimatedContent(
            targetState = isExpanded, label = ""
        ) { targetState ->
            if (targetState) {
                val textLimitExceeded = searchText.length > 20
                val limitedText = if (textLimitExceeded) searchText.substring(0, 20) else searchText
                BasicTextField(
                    value = limitedText,
                    onValueChange = { searchText = it },
                    textStyle = TextStyle(color = Color.White),
                    modifier = Modifier
                        .animateContentSize()
                        .background(Color.Transparent)
                        .width(400.dp)
                        .height(50.dp)
                        .border(
                            width = 1.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .padding(vertical = 14.dp, horizontal = 20.dp),
                    singleLine = true,
                    cursorBrush = SolidColor(Color.White),
                    keyboardActions = KeyboardActions(onDone = { finalSearchText = searchText; onSearch(finalSearchText) })
                )
            }
        }
        if (isExpanded) {
            IconButton(onClick = {
                setExpanded()
            }) {
                Icon(
                    Icons.Default.KeyboardArrowUp,
                    contentDescription = "Return",
                    tint = Color.White
                )
            }
        }
        if(!isExpanded) {
            IconButton(onClick = setExpanded) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun NearMe(onNearMeRequested: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(
            onClick = {onNearMeRequested()},
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_dialog_map),
                contentDescription = "Near Me",
                tint = Color.White
            )
        }
    }
}

@Composable
fun SearchEventDialog(
    onDismiss: () -> Unit,
    searchEventCode: (String) -> Unit
) {
    var codeInput by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(39, 62, 73, 255))
            ){
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Enter Event Code",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    TextField(
                        value = codeInput,
                        onValueChange = { codeInput = it },
                        label = { Text("Code") },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            searchEventCode(codeInput)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3543C5))
                    ) {
                        Text("Search")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SearchEventScreenPreview() {
    SearchEventScreen(
        onProfileRequested = { },
        onHomeRequested = { },
        onEventsRequested = { },
        onNearMeRequested = { },
        onSearch = { },
        events = emptyList(),
        categories = emptyList(),
        onEventClick = { },
        searchEventCode = { }
    )
}