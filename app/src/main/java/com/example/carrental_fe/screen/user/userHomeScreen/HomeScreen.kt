package com.example.carrental_fe.screen.user.userHomeScreen

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carrental_fe.R
import com.example.carrental_fe.screen.user.BrandLogoCard
import com.example.carrental_fe.screen.user.CarCard
import com.example.carrental_fe.screen.user.SearchBar
import com.example.carrental_fe.screen.user.TopTitle
import kotlin.compareTo
import kotlin.text.compareTo

@Composable
fun HomeScreen(
    onNavigateToSearchScreen: () -> Unit,
    onNavigateToCarDetail: (String) -> Unit,
    viewModel: UserHomeScreenViewModel = viewModel(factory = UserHomeScreenViewModel.Factory),
) {

    val listState = rememberLazyListState()
    val isAtBottom by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            val totalItemsCount = listState.layoutInfo.totalItemsCount
            lastVisibleItem?.index == totalItemsCount - 1
        }
    }
    val selectedBrand by viewModel.selectedBrand.collectAsState()
    val carBrands by viewModel.carBrands.collectAsState()
    val cars by viewModel.carList.collectAsState()
    val totalPages by viewModel.totalPages.collectAsState()
    val currentPage by viewModel.currentPage.collectAsState()
    val favourtiteCars by viewModel.favouriteCars.collectAsState()
    LaunchedEffect(currentPage) {
        listState.animateScrollToItem(0)
    }
    LaunchedEffect(selectedBrand) {
        listState.animateScrollToItem(0)
    }
    LaunchedEffect(Unit) {
        viewModel.resetPage()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        )
        {
            TopTitle("Explore")
        }
        Spacer(Modifier.height(12.dp))

        SearchBar(query = "", onClick = { onNavigateToSearchScreen() })

        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Select Car Brand",
                fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
            Text(
                text = "See all",
                color = Color(0xFF0D6EFD),
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clickable {
                        if (selectedBrand != null) {
                            viewModel.resetPage()
                        }
                    }
            )
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(carBrands) { brand ->
                BrandLogoCard(
                    carBrand = brand,
                    isSelected = selectedBrand?.id == brand.id,
                    onBrandClick = {
                        viewModel.loadCarsByBrand(brand)
                    }
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        Text("Cars List", fontFamily = FontFamily(Font(R.font.montserrat_medium)),
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp)

        Box(modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize(),
                state = listState
            ) {
                items(cars) { car ->
                    CarCard(
                        car = car,
                        isFavorite = favourtiteCars.any { it.id == car.id },
                        onFavoriteClick = {
                            viewModel.toggleFavourite(car.id) },
                        onCarCardClick = {
                            onNavigateToCarDetail(car.id)
                        }
                    )
                }
            }
        }
        if (isAtBottom && cars.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.prevPage() }, enabled = currentPage > 1) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous")
                }

                (1..totalPages).forEach { page ->
                    Text(
                        text = page.toString(),
                        modifier = Modifier
                            .padding(horizontal = 6.dp)
                            .clickable { viewModel.goToPage(page) },
                        color = if (page == currentPage) Color(0xFF0D6EFD) else Color.Gray,
                        fontWeight = if (page == currentPage) FontWeight.Bold else FontWeight.Normal,
                    )
                }

                IconButton(onClick = { viewModel.nextPage() }, enabled = currentPage < totalPages) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
                }
            }
        }
    }
}