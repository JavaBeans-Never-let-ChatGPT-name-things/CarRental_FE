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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carrental_fe.R

@Composable
fun InputField(value: String, placeHolder:String, onValueChange:(String) -> Unit = {_ ->},
               editable: Boolean = true, inputType: String = "text") {
    val keyboardOptions = when (inputType.lowercase()) {
        "number" -> KeyboardOptions(keyboardType = KeyboardType.Number)
        else -> KeyboardOptions.Default
    }
    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF7F7F9), shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        placeholder = { Text(placeHolder, color = Color.Gray) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        readOnly = !editable,
        textStyle = TextStyle(color = Color.Gray, fontSize = 16.sp),
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
fun PasswordField(value: String,
                  isPasswordVisible : Boolean,
                  togglePassWordVisibility: () -> Unit,
                  onValueChange: (String) -> Unit,
                  modifier: Modifier = Modifier)
{
    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF7F7F9), shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        placeholder = { Text("Password", color = Color.Gray) },
        singleLine = true,
        textStyle = TextStyle(color = Color.Gray, fontSize = 16.sp),
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val icon = if (isPasswordVisible) R.drawable.eye else R.drawable.eye_slash
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "Toggle Password",
                modifier = Modifier
                    .size(20.dp)
                    .clickable { togglePassWordVisibility() },
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
    textColor: Long,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onClickChange() },
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
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
    iconResId: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
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
fun InputLabel(text: String, @DrawableRes drawable: Int, color: Color = Color.Black) {
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
            fontSize = 14.sp,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}
@Preview()
@Composable
fun InputFieldPreview(){
    InputLabel("Username", R.drawable.user)
}