package com.example.carrental_fe.screen.user.userSearchScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carrental_fe.R
import com.example.carrental_fe.screen.user.userHomeScreen.CarCard
import com.example.carrental_fe.screen.user.userHomeScreen.TopTitle

@Composable
fun SearchScreen(
    onNavigateToCarDetail: (String) -> Unit,
    viewModel: SearchScreenViewModel = viewModel(factory = SearchScreenViewModel.Factory) )
{
    LaunchedEffect(Unit) {
        viewModel.resetSearchScreen()
    }
    val listState = rememberLazyListState()
    var showContent by remember { mutableStateOf(false) }
    val cars by viewModel.carList.collectAsState()
    val query by viewModel.query.collectAsState()
    val currentPage by viewModel.currentPage.collectAsState()
    val favourtiteCars by viewModel.favouriteCars.collectAsState()
    val totalPage by viewModel.totalPages.collectAsState()
    LaunchedEffect(Unit) {
        showContent = true
    }
    LaunchedEffect(currentPage) {
        listState.animateScrollToItem(0)
    }
    LaunchedEffect(query){
        viewModel.setCurrentPage()
    }
    AnimatedVisibility(
        visible = showContent,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
                .padding(top = 43.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                TopTitle("Search Now")
            }
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = query,
                onValueChange = {
                    viewModel.setSearchQuery(it)
                    viewModel.getCars()
                },
                placeholder = { Text("Looking for car", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF7F7F9),
                    focusedContainerColor = Color(0xFFF7F7F9),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(Modifier.height(16.dp))

            Text(
                "Cars List",
                fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 8.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    state = listState
                ) {
                    items(cars) { car ->
                        CarCard(
                            car = car,
                            isFavorite = favourtiteCars.any { it.id == car.id },
                            onFavoriteClick = { viewModel.toggleFavourite(car.id) },
                            onCarCardClick = {
                                onNavigateToCarDetail(car.id)
                            }
                        )
                    }
                }
                if (cars.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { viewModel.prevSearchPage() }, enabled = currentPage > 1) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous")
                        }

                        (1..totalPage).forEach { page ->
                            Text(
                                text = page.toString(),
                                modifier = Modifier
                                    .padding(horizontal = 6.dp)
                                    .clickable { viewModel.gotoSearchPage(page) },
                                color = if (page == currentPage) Color(0xFF0D6EFD) else Color.Gray,
                                fontWeight = if (page == currentPage) FontWeight.Bold else FontWeight.Normal,
                            )
                        }

                        IconButton(onClick = { viewModel.nextSearchPage() }, enabled = currentPage < totalPage) {
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
                        }
                    }
                }
            }
        }
    }
}