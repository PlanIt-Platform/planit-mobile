package com.example.planit_mobile.ui.screens.searchEvent


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.planit_mobile.R
import com.example.planit_mobile.ui.screens.common.BotBar
import com.example.planit_mobile.ui.screens.common.NavigationHandlers

@Composable
fun SearchEventScreen(
    onProfileRequested: () -> Unit,
    onHomeRequested: () -> Unit,
    onEventsRequested: () -> Unit,
    onSearch: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
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
    ) {
        Box(modifier = Modifier
            .padding(it)
            .padding(vertical = 20.dp)) {
            if(!isExpanded) {
                Text(
                    text = "What are you looking for?",
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(10.dp)
                )
            }
            SearchBarAnimation(
                onSearch = onSearch,
                isExpanded = isExpanded,
                setExpanded = { isExpanded = !isExpanded }
            )
            if (!isExpanded) FilterSettings()
        }
        Box(modifier = Modifier.padding(vertical = 100.dp)){
            val categories = listOf("All", "Sports and Outdoor", "Entertainment", "Culture", "Education", "Charity")
            var selectedCategory by remember { mutableStateOf(categories[0]) }
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
}

@Composable
fun SearchBarAnimation(
    onSearch: (String) -> Unit,
    isExpanded: Boolean,
    setExpanded: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    Box( modifier = Modifier .fillMaxWidth().padding(horizontal = 35.dp), contentAlignment = Alignment.CenterEnd) {
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
                    cursorBrush = SolidColor(Color.White)
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
fun FilterSettings(){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_filter_alt_24),
                contentDescription = "Filter",
                tint = Color.White
            )
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
        onSearch = { }
    )
}