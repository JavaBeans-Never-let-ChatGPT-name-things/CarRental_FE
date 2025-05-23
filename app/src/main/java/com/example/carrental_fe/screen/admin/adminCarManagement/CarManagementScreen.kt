package com.example.carrental_fe.screen.admin.adminCarManagement

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.carrental_fe.R
import com.example.carrental_fe.dialog.ErrorDialog
import com.example.carrental_fe.model.CarBrand
import com.example.carrental_fe.screen.component.CustomButton
import com.example.carrental_fe.screen.user.userHomeScreen.BrandLogoCard
import com.example.carrental_fe.screen.user.userHomeScreen.CarCard
import com.example.carrental_fe.screen.user.userHomeScreen.TopTitle
import java.io.File

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CarManagementScreen(
    onNavigateToCarDetail: (String) -> Unit,
    viewModel: CarManagementViewModel = viewModel(factory = CarManagementViewModel.Factory)
) {
    val context = LocalContext.current

    // VM states
    val carBrands    by viewModel.carBrands.collectAsState()
    val cars         by viewModel.carList.collectAsState()
    val selectedBrand by viewModel.selectedBrand.collectAsState()
    val isLoading    by viewModel.isLoading.collectAsState()
    val isLoadingBrand by viewModel.isLoadingBrand.collectAsState()
    val totalPages   by viewModel.totalPages.collectAsState()
    val currentPage  by viewModel.currentPage.collectAsState()
    val errorText    by viewModel.error.collectAsState()

    // local for forms
    var newBrandName by remember { mutableStateOf("") }
    var newBrandLogo by remember { mutableStateOf<Uri?>(null) }

    var newCarId     by remember { mutableStateOf("") }
    var newCarBrand  by remember { mutableStateOf<CarBrand?>(null) }
    var expandedCarBrand by remember { mutableStateOf(false) }
    var newCarImage  by remember { mutableStateOf<Uri?>(null) }
    var newCarSpeed  by remember { mutableStateOf("") }
    var newCarRange  by remember { mutableStateOf("") }
    var newCarSeats  by remember { mutableStateOf("") }
    var newCarPrice  by remember { mutableStateOf("") }
    var newCarEngine by remember { mutableStateOf("") }
    var newCarGear   by remember { mutableStateOf("") }
    var newCarDrive  by remember { mutableStateOf("") }

    val pickBrandLogo = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        newBrandLogo = it
    }
    val pickCarImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        newCarImage = it
    }

    // scroll state for LazyColumn
    val listState = rememberLazyListState()
    LaunchedEffect(true) {
        withFrameNanos { }
        listState.scrollToItem(0)
    }
    // khi chuyển trang, scroll lại cars list về đầu
    LaunchedEffect(currentPage) {
        listState.animateScrollToItem(4)
    }

    // error dialog
    errorText?.let { msg ->
        ErrorDialog(text = msg) {
            viewModel.clearError()
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Title
        item {
            TopTitle("Car Manage")
        }

        // --- Add Brand ---
        item {
            Card(elevation = CardDefaults.cardElevation(4.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Add Car Brand", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = newBrandName,
                        onValueChange = { newBrandName = it },
                        label = { Text("Brand Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    // placeholder image box
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(Color.LightGray, RoundedCornerShape(8.dp))
                            .clickable { pickBrandLogo.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (newBrandLogo != null) {
                            AsyncImage(
                                model = newBrandLogo,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text("Logo", color = Color.DarkGray)
                        }
                    }
                    CustomButton(
                        text = "Add Car Brand",
                        backgroundColor = Color(0xFF0D6EFD),
                        textColor = 0xFFFFFFFF,
                        modifier = Modifier.align(Alignment.End),
                        onClickChange = {
                            if (newBrandName.isBlank() || newBrandLogo == null) {
                                viewModel.setError("Please fill all fields")
                                return@CustomButton
                            }
                            if (carBrands.any { it.name.equals(newBrandName, true) }) {
                                viewModel.setError("Brand exists")
                                return@CustomButton
                            }
                            val file = FileUtils.getFileFromUri(context, newBrandLogo!!)!!
                            viewModel.addBrand(newBrandName, file)
                            newBrandName = ""; newBrandLogo = null
                        })
                }
            }
        }

        // --- Add Car ---
        item {
            Card(elevation = CardDefaults.cardElevation(4.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Add Car", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = newCarId,
                        onValueChange = { newCarId = it },
                        label = { Text("Car ID") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // dropdown for brand
                    Box {
                        OutlinedTextField(
                            value = newCarBrand?.name ?: "",
                            onValueChange = {},
                            label = { Text("Brand") },
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    Modifier.clickable { expandedCarBrand = !expandedCarBrand }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        DropdownMenu(
                            expanded = expandedCarBrand,
                            onDismissRequest = { expandedCarBrand = false }
                        ) {
                            carBrands.forEach { b ->
                                DropdownMenuItem(
                                    text = { Text(b.name) },
                                    onClick = {
                                        newCarBrand = b
                                        Log.d("TAG_BRAND", "Selected: ${b.name}")
                                        expandedCarBrand = false
                                    }
                                )
                            }
                        }
                    }

                    // car image placeholder
                    Box(
                        modifier = Modifier
                            .height(120.dp)
                            .fillMaxWidth()
                            .background(Color.LightGray, RoundedCornerShape(8.dp))
                            .clickable { pickCarImage.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        if (newCarImage != null) {
                            AsyncImage(
                                model = newCarImage,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text("Car Image", color = Color.DarkGray)
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = newCarSpeed,
                            onValueChange = { newCarSpeed = it },
                            label = { Text("Max Speed") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        OutlinedTextField(
                            value = newCarRange,
                            onValueChange = { newCarRange = it },
                            label = { Text("Range") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = newCarSeats,
                            onValueChange = { newCarSeats = it },
                            label = { Text("Seats") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        OutlinedTextField(
                            value = newCarPrice,
                            onValueChange = { newCarPrice = it },
                            label = { Text("Price/day") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = newCarEngine,
                            onValueChange = { newCarEngine = it },
                            label = { Text("Engine") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = newCarGear,
                            onValueChange = { newCarGear = it },
                            label = { Text("Gear") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    OutlinedTextField(
                        value = newCarDrive,
                        onValueChange = { newCarDrive = it },
                        label = { Text("Drive") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    CustomButton(
                        text = "Add Car",
                        backgroundColor = Color(0xFF0D6EFD),
                        textColor = 0xFFFFFFFF,
                        modifier = Modifier.align(Alignment.End),
                        onClickChange = {
                            if (listOf(
                                    newCarId, newCarBrand?.name, newCarSpeed,
                                    newCarRange, newCarSeats, newCarPrice,
                                    newCarEngine, newCarGear, newCarDrive
                                ).any { it.isNullOrBlank() } || newCarImage == null
                            ) {
                                viewModel.setError("Please complete all fields")
                                return@CustomButton
                            }
                            if (cars.any { it.id.equals(newCarId, true) }) {
                                viewModel.setError("Car ID exists")
                                return@CustomButton
                            }
                            val file = FileUtils.getFileFromUri(context, newCarImage!!)!!
                            viewModel.addCar(
                                id = newCarId,
                                brandName = newCarBrand!!.name,
                                maxSpeed = newCarSpeed.toFloat(),
                                carRange = newCarRange.toFloat(),
                                carImage = file,
                                seatsNumber = newCarSeats.toInt(),
                                rentalPrice = newCarPrice.toFloat(),
                                engineType = newCarEngine,
                                gearType = newCarGear,
                                drive = newCarDrive
                            )
                            // reset form
                            newCarId = ""
                            newCarBrand = null
                            newCarImage = null
                            newCarSpeed = ""
                            newCarRange = ""
                            newCarSeats = ""
                            newCarPrice = ""
                            newCarEngine = ""
                            newCarGear = ""
                            newCarDrive = ""
                        })
                }
            }
        }
        item {
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp),
                contentAlignment = Alignment.Center
            ){
                if (isLoadingBrand) {
                    CircularProgressIndicator(color = Color(0xFF0D6EFD))
                }
                else {
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
                }
            }
        }
        // --- Sticky header for Cars List ---
        stickyHeader {
            Text(
                "Cars List",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 8.dp),
                fontWeight = FontWeight.SemiBold
            )
        }
        // --- Car items ---
        if (isLoading) {
            item {
                Box(Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        } else {
            items(cars) { car ->
                CarCard(car = car, onCarCardClick = { onNavigateToCarDetail(car.id) })
            }
        }

        // --- Pagination footer ---
        item {
            if (cars.isNotEmpty()) {
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
}
object FileUtils {
    fun getFileFromUri(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val tempFile = File.createTempFile("temp", null, context.cacheDir)
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}