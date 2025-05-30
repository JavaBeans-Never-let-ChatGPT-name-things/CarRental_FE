package com.example.carrental_fe.screen.user.userContractDetail

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carrental_fe.R
import com.example.carrental_fe.dialog.ErrorDialog
import com.example.carrental_fe.screen.component.CustomButton
import com.example.carrental_fe.screen.user.userHomeScreen.TopTitle
import com.example.carrental_fe.dialog.SuccessDialog
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ContractDetailsScreen(
    onCheckoutNav: (String, String, Long) -> Unit,
    vm: ContractDetailViewModel = viewModel(factory = ContractDetailViewModel.Factory),
    onCheckoutComplete: () -> Unit) {
    val startDate = vm.startDate.collectAsState()
    val endDate = vm.endDate.collectAsState()
    val dateDiff = vm.dateDiff.collectAsState()
    val account = vm.account.collectAsState()
    val totalPrice = vm.totalPrice.collectAsState()
    val showError = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }
    vm.paymentStatus?.let {
        if (it == "success"){
            SuccessDialog(text = "Successfully Booked ${vm.carId}", onDismiss = { onCheckoutComplete() })
        }
        else
        {
            ErrorDialog(text = "Payment Failed", onDismiss = { onCheckoutComplete() })
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .padding(top = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        TopTitle("Contract Details")

        Spacer(modifier = Modifier.height(30.dp))

        Card(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Contact Information",
                    fontFamily = FontFamily(Font(R.font.montserrat_medium)))

                Spacer(modifier = Modifier.height(12.dp))

                InfoItem(Icons.Default.Email, account.value?.email?:"", "Email")
                InfoItem(Icons.Default.Phone, account.value?.phoneNumber?:"", "Phone")

                Spacer(modifier = Modifier.height(16.dp))
                Text("Address", fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                Text( account.value?.address?:"", color = Color.Gray)

                Spacer(modifier = Modifier.height(16.dp))
                Text("Payment Method", fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                InfoItem(Icons.Default.QrCode, "PayOs", null)

                Spacer(modifier = Modifier.height(16.dp))
                LabeledDatePicker(
                    label = "Start Date",
                    selectedDate = startDate.value,
                    onDateSelected = { picked ->
                        when {
                            picked < LocalDate.now() -> {
                                errorMessage.value = "Start date cannot be in the past"
                                showError.value = true
                            }
                            picked > endDate.value -> {
                                errorMessage.value = "Start date cannot be after end date"
                                showError.value = true
                            }
                            else -> vm.setStartDate(picked)
                        }
                    }
                )
                LabeledDatePicker(
                    label = "End Date",
                    selectedDate = endDate.value,
                    onDateSelected = { picked ->
                        when {
                            picked < LocalDate.now() -> {
                                errorMessage.value = "End date cannot be in the past"
                                showError.value = true
                            }
                            picked < startDate.value -> {
                                errorMessage.value = "End date cannot be before start date"
                                showError.value = true
                            }
                            else -> vm.setEndDate(picked)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text("Total Price", fontWeight = FontWeight.Medium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${vm.pricePreday} * ${dateDiff.value} (days)", fontWeight = FontWeight.Light, color = Color.Gray)
                    Text("${totalPrice.value} $", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Card(
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Deposit fee", color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.montserrat_regular)))
                    Text("$100", fontWeight = FontWeight.Bold)
                }

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    color = Color.Gray.copy(alpha = 0.5f),
                    thickness = 1.dp
                )

                Text(
                    "Please come and pick up the car before the start date",
                    color = Color.Gray,
                    fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomButton(
                    backgroundColor = Color(0xFF0D6EFD),
                    text = "Checkout",
                    textColor = 0xFFFFFFFF,
                    onClickChange = {
                        vm.createContract(onCheckoutNav)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
    if (showError.value) {
        ErrorDialog(
            text = errorMessage.value,
            onDismiss = { showError.value = false }
        )
    }
}
@Composable
fun InfoItem(icon: ImageVector, title: String, subtitle: String?) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null)
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(title, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            if (subtitle != null) {
                Text(subtitle, color = Color.Gray)
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabeledDatePicker(label: String, selectedDate: LocalDate, onDateSelected: (LocalDate) -> Unit, minDate: LocalDate = LocalDate.MIN) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    )
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        DatePickerDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            val picked = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                            if (picked >= minDate) {
                                onDateSelected(picked)
                            }
                        }
                        showDialog.value = false
                    }
                ) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontWeight = FontWeight.Medium)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(formatter.format(selectedDate), color = Color.Gray)
            Icon(Icons.Default.CalendarToday,
                contentDescription = null,
                modifier = Modifier.clickable { showDialog.value = true })
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}