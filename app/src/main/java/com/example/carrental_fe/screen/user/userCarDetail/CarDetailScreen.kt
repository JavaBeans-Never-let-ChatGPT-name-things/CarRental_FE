package com.example.carrental_fe.screen.user.userCarDetail

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventSeat
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.carrental_fe.R
import com.example.carrental_fe.dialog.ErrorDialog
import com.example.carrental_fe.model.Review
import com.example.carrental_fe.model.enums.CarState
import com.example.carrental_fe.screen.component.CustomButton
import com.example.carrental_fe.screen.user.userHomeScreen.TopTitle

@Composable
fun CarDetailScreen(
    onContractNav: (Float, String) -> Unit,
    viewModel: CarDetailViewModel = viewModel(factory = CarDetailViewModel.Factory),
) {
    val car = viewModel.car.collectAsState()
    val reviews = viewModel.reviewList.collectAsState()
    val isLoadingReview = viewModel.isLoadingReview.collectAsState()
    val isQualified = viewModel.isQualified.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9)),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, top = 50.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        TopTitle(title = car.value?.id?:"")
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    AsyncImage(
                        model = car.value?.carImageUrl,
                        contentDescription = "Car Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RatingStars(car.value?.rating ?: 0f)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "${car.value?.rentalPrice?:""}$/day",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Highlight", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(12.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            InfoCard(
                                Icons.Default.LocalGasStation, "Range", "${car.value?.carRange?:""}km",
                                modifier = Modifier.weight(1f)
                            )
                            InfoCard(
                                Icons.Default.Speed, "Speed", "${car.value?.maxSpeed?:""}/kph",
                                modifier = Modifier.weight(1f)
                            )
                            InfoCard(
                                Icons.Default.Tune, "Gear", car.value?.gearType?:"",
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            InfoCard(
                                Icons.Default.Settings, "Engine", car.value?.engineType ?: "",
                                modifier = Modifier.weight(1f)
                            )
                            InfoCard(
                                Icons.Default.EventSeat, "Capacity", "${car.value?.seatsNumber?:""} seat",
                                modifier = Modifier.weight(1f)
                            )
                            InfoCard(
                                Icons.Default.Shield, "Drive", car.value?.drive?:"",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Reviews", style = MaterialTheme.typography.titleMedium)
                }
                if (!isLoadingReview.value)
                {
                    items(reviews.value) { review ->
                        ReviewCard(review)
                    }
                }
                else{
                    item{
                        Box(modifier = Modifier.fillMaxWidth())
                        {
                            CircularProgressIndicator(color = Color(0xFF0D6EFD),
                                modifier = Modifier.align(Alignment.Center))
                        }
                    }
                }
            }
        }

        if (viewModel.role == "User"  && car.value?.state == CarState.AVAILABLE)
        {
            CustomButton(
                backgroundColor = Color(0xFF0D6EFD),
                text = "RENT NOW",
                textColor = 0xFFFFFFFF,
                onClickChange = {
                    viewModel.checkStatus(onContractNav)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
        isQualified.value?.let {
            if (!it)
            {
                ErrorDialog (text = "You must complete your phone number and address") {
                    viewModel.resetQualify()
                }
            }
        }
    }
}

@Composable
fun ReviewCard(review: Review) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar
                if (review.avatarUrl != null) {
                    AsyncImage(
                        model = review.avatarUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Gray, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.male_avatar_svgrepo_com),
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .border(1.dp, Color.Gray, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = review.accountDisplayName,
                        fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
                        fontSize = 18.sp
                    )
                    RatingBar(review.starsNum)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = review.comment,
                fontFamily = FontFamily(Font(R.font.montserrat_regular))
            )
        }
    }
}

@Composable
fun RatingBar(starNums: Int) {
    Row {
        repeat(5) { index ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (index < starNums) Color(0xFFFFC107) else Color.LightGray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun InfoCard(icon: ImageVector, label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                label,
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                color = Color.Black,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                value,
                fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                fontSize = 12.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun RatingStars(rating: Float, starSize: Dp = 24.dp, spaceBetween: Dp = 4.dp) {
    val starCount = 5
    val starPainter = painterResource(id = R.drawable.ic_star)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spaceBetween)
    ) {
        for (i in 0 until starCount) {
            val fillRatio = (rating - i).coerceIn(0f, 1f)
            Box(
                modifier = Modifier
                    .size(starSize)
                    .clip(RectangleShape)
            ) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    with(starPainter) {
                        draw(size = size, colorFilter = ColorFilter.tint(Color.LightGray))
                        if (fillRatio > 0f) {
                            clipRect(right = size.width * fillRatio) {
                                draw(size = size, colorFilter = ColorFilter.tint(Color(0xFFFFC107)))
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "%.1f".format(rating),
            fontFamily = FontFamily(Font(R.font.montserrat_regular)),

            )
    }
}