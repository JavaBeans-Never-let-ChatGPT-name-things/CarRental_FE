package com.example.carrental_fe.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReviewDialog(
    stars: Int,
    comment: String,
    onStarsChange: (Int) -> Unit,
    onCommentChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Submit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Leave a Review") },
        text = {
            Column {
                Text("Rating")
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 1..5) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star $i",
                            tint = if (i <= stars) Color(0xFFFFC107) else Color.LightGray,
                            modifier = Modifier
                                .size(32.dp)
                                .clickable { onStarsChange(i) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Comment")
                TextField(
                    value = comment,
                    onValueChange = onCommentChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    placeholder = { Text("Write your feedback here") },
                    maxLines = 4,
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                )
            }
        },
        shape = RoundedCornerShape(16.dp),
    )
}

@Composable
@Preview
fun ReviewDialogPreview() {
    ReviewDialog(
        stars = 3,
        comment = "",
        onStarsChange = {},
        onCommentChange = {},
        onDismiss = {},
        onConfirm = {}
    )
}