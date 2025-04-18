package com.example.carrental_fe.screen.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.EventSeat
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.carrental_fe.R
import com.example.carrental_fe.model.Car
import com.example.carrental_fe.model.CarBrand

@Composable
fun CarCard(
    car: Car,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onCarCardClick: ()-> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFF7F7F9))
            .padding(16.dp)
            .width(372.dp)
            .clickable{
                onCarCardClick()
            }
    ) {
        Column {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = car.id,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onFavoriteClick()
                        }
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.LightGray,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            AsyncImage(
                model = car.carImageUrl,
                contentDescription = "Car image",
                modifier = Modifier
                    .width(300.dp)
                    .height(110.dp)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.LocalGasStation, contentDescription = null)
                    Text(
                        text = "${car.carRange}",
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(Icons.Outlined.EventSeat, contentDescription = null)
                    Text(
                        text = "${car.seatsNumber}",
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Text(
                    text = "${car.rentalPrice}/day",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
@Composable
fun BrandLogoCard(
    carBrand: CarBrand,
    onBrandClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) Color(0xFF0D6EFD) else Color(0xFFF7F7F9)
    Card(
        modifier = modifier
            .size(75.dp)
            .clip(RoundedCornerShape(5.dp))
            .clickable{ onBrandClick() },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = carBrand.logoUrl,
                contentDescription = "Car brand logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF7F7F9))
            .clickable { onClick() }
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = {},
            placeholder = { Text("Looking for car", color = Color.Gray) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            },
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                disabledContainerColor = Color(0xFFF7F7F9),
                disabledIndicatorColor = Color.Transparent,
                disabledTextColor = Color.Black,
                disabledPlaceholderColor = Color.Gray,
                disabledLeadingIconColor = Color.Gray
            )
        )
    }
}
@Composable
fun TopTitle(title: String, modifier: Modifier = Modifier )
{
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.highlight),
                contentDescription = null,
                modifier = Modifier.size(20.dp).offset(y = (-8).dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                title,
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                fontSize = 32.sp
            )
        }
    }
}

@Preview()
@Composable
fun Preview() {

}