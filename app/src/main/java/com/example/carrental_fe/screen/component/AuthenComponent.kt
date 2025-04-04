package com.example.carrental_fe.screen.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carrental_fe.R

@Composable
fun InputField(placeHolder:String, onValueChange:() -> Unit, modifier: Modifier = Modifier){
    TextField(
        value = "",
        onValueChange = { onValueChange() },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF7F7F9), shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        placeholder = { Text(placeHolder, color = Color.Gray) },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFF7F7F9),
            focusedContainerColor = Color(0xFFF7F7F9),
            disabledContainerColor = Color(0xFFF7F7F9),
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun PasswordField(togglePassWordVisibility: () -> Unit, onValueChange:() -> Unit, modifier: Modifier = Modifier)
{
    val isPasswordVisible = false
    TextField(
        value = "",
        onValueChange = { onValueChange() },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF7F7F9), shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        placeholder = { Text("Password", color = Color.Gray) },
        singleLine = true,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.eye_slash), // Thay bằng ID ảnh của bạn
                contentDescription = "Toggle Password",
                modifier = Modifier
                    .size(20.dp)
                    .clickable { togglePassWordVisibility },
                tint = Color.Gray
            )
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFF7F7F9),
            focusedContainerColor = Color(0xFFF7F7F9),
            disabledContainerColor = Color(0xFFF7F7F9),
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        )
    )
}
@Composable
fun CustomButton(
    onClickChange: () -> Unit = {},
    backgroundColor: Color,
    text: String,
    @DrawableRes imageResId: Int? = null,
    textColor: Long
) {
    Button(
        onClick = { onClickChange },
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            imageResId?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp)
                )
            }
            Text(
                text = text,
                fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(textColor)
            )
        }
    }
}

@Composable
fun BackButton(
    onClick: () -> Unit,
    iconResId: Int
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(Color(0xFFF7F7F9))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = "Back"
        )
    }
}
@Composable
fun InputLabel(text: String, @DrawableRes drawable: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(drawable),
            modifier = Modifier.size(15.dp),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            fontFamily = FontFamily(Font(R.font.montserrat_medium)),
            text = text,
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium
        )
    }
}
@Preview()
@Composable
fun InputFieldPreview(){
    InputLabel("Username", R.drawable.user)
}