package com.example.carrental_fe.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ConfirmReportLostDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontSize = 18.sp) },
        text = {
            Text(
                text = message,
                fontSize = 14.sp
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Confirm", color = Color(0xFF0D6EFD))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Red)
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Preview
@Composable
fun ConfirmReportLostDialogPreview() {
    ConfirmReportLostDialog(
        title = "Confirm Report Lost",
        message = "Are you sure you want to report the car as lost?",
        onConfirm = {},
        onDismiss = {}
    )
}