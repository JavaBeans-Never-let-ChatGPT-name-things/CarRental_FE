package com.example.carrental_fe.screen.user.userContractScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.carrental_fe.R
import com.example.carrental_fe.dialog.ConfirmReportLostDialog
import com.example.carrental_fe.dialog.ReviewDialog
import com.example.carrental_fe.model.Contract
import com.example.carrental_fe.model.enums.ContractStatus
import com.example.carrental_fe.model.enums.PaymentStatus
import com.example.carrental_fe.model.enums.ReturnCarStatus
import com.example.carrental_fe.screen.component.CustomButton
import com.example.carrental_fe.screen.user.userHomeScreen.TopTitle
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ContractScreen(
    vm: ContractViewModel = viewModel(factory = ContractViewModel.Factory),
    onCheckoutNav: (String, String, Long?) -> Unit,
) {
    val contracts = vm.contracts.collectAsState().value
    val showDialog = vm.showReviewDialog.collectAsState()
    val stars = vm.reviewStars.collectAsState()
    val comment = vm.reviewComment.collectAsState()
    val pagerState = rememberPagerState(pageCount = { contracts.size })
    val showConfirmDialog = remember { mutableStateOf(false) }
    val lostContractId = remember { mutableStateOf<Long?>(null) }
    val selectedDaysMap = remember { mutableStateMapOf<Long, Int>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 16.dp)
    ) {
        TopTitle("Contracts")
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 20.dp),
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val contract = contracts[page]
            val selectedDays = selectedDaysMap[contract.id] ?: 0
            ContractCard(
                contract = contract,
                selectedDays = selectedDays,
                onCheckoutNav = {
                    vm.retryContract(contract.id, contract.carId, onCheckoutNav)
                },
                onReview = {
                    vm.onReviewClick(contract.id)
                },
                onExtendContract = {
                    vm.extendContract(contract.id, selectedDays)
                },
                onReportLostClick = {
                    lostContractId.value = contract.id
                    showConfirmDialog.value = true
                },
                onSelectedDaysChange = { newDays ->
                    selectedDaysMap[contract.id] = newDays
                }
            )
        }
    }
    if (showDialog.value) {
        ReviewDialog(
            stars = stars.value,
            comment = comment.value,
            onStarsChange = { vm.reviewStars.value = it },
            onCommentChange = { vm.reviewComment.value = it },
            onDismiss = { vm.dismissReviewDialog() },
            onConfirm = { vm.onReviewSubmit() }
        )
    }
    if (showConfirmDialog.value && lostContractId.value != null) {
        ConfirmReportLostDialog(
            title = "Confirm Report Lost",
            message = "Are you sure you want to report the car as lost?",
            onConfirm = {
                vm.reportLostContract(lostContractId.value!!)
                showConfirmDialog.value = false
            },
            onDismiss = {
                showConfirmDialog.value = false
            }
        )
    }
}
@Composable
fun ContractCard(
    contract: Contract,
    role: String = "User",
    selectedDays: Int = 0,
    onReview: () -> Unit = {},
    onExtendContract: () -> Unit = {},
    onSelectedDaysChange: (Int) -> Unit = {},
    onConfirmPickUp: () -> Unit = {},
    onCheckoutNav: () -> Unit = {},
    onAssignContract: () -> Unit = {},
    onReportLostClick: () -> Unit = {},
    onConfirmPending: () -> Unit = {},
    onRejectPending: () -> Unit = {},
    onConfirmReturn: () -> Unit = {}
) {
    val blue = Color(0xFF0D6EFD)
    val red = Color.Red
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    Card(
        modifier = Modifier
            .wrapContentSize()
            .padding(5.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(Color(0xFFF7F7F9))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = contract.carId.uppercase(),
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))
            if (role != "User")
            {
                ContractRow("Contract's Owner", contract.customerName)
            }

            AsyncImage(
                model = contract.carImageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))

            ContractRow("Contract Date", contract.contractDate.atZone(ZoneId.systemDefault()).toLocalDate().format(dateFormatter))
            ContractRow("Start From", contract.startDate.format(dateFormatter))
            ContractRow("End At", contract.endDate.format(dateFormatter))

            val paymentColor =
                if (contract.paymentStatus == PaymentStatus.SUCCESS) blue else red
            val statusColor =
                if (contract.contractStatus == ContractStatus.EXPIRED || contract.contractStatus == ContractStatus.OVERDUE) red else blue

            ContractRow("Payment Status", contract.paymentStatus.name, paymentColor)
            ContractRow("Contract Status", contract.contractStatus.name, statusColor)

            contract.returnCarStatus?.let {
                val color = when (it) {
                    ReturnCarStatus.INTACT -> blue
                    else -> red
                }
                ContractRow("Return Car Status", it.name, color)
            }
            if (role != "Employee")
            {
                contract.employeeName?.let {
                    ContractRow("Employee Incharge", it)
                }
            }
            ContractRow("Total Price", "$${contract.totalPrice.toInt()}")

            ContractRow("Deposit", "$${contract.deposit.toInt()}")
            Spacer(modifier = Modifier.height(16.dp))

            if (role == "User") {
                when {
                    contract.contractStatus == ContractStatus.COMPLETE && contract.returnCarStatus != ReturnCarStatus.LOST -> {
                        CustomButton(
                            backgroundColor = blue,
                            text = "Review Now",
                            textColor = 0xFFFFFFFF,
                            onClickChange = onReview,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    contract.paymentStatus == PaymentStatus.FAILED -> {
                        CustomButton(
                            backgroundColor = blue,
                            text = "Retry",
                            textColor = 0xFFFFFFFF,
                            onClickChange = onCheckoutNav,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    contract.contractStatus == ContractStatus.PICKED_UP -> {
                        CustomButton(
                            backgroundColor = red,
                            text = "Report Lost",
                            textColor = 0xFFFFFFFF,
                            onClickChange = onReportLostClick,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                val tomorrow = LocalDate.now().plusDays(1)
                if (contract.endDate == tomorrow && contract.contractStatus == ContractStatus.PICKED_UP) {
                    ExtendContractSection(
                        selectedDays = selectedDays,
                        onExtendContract = onExtendContract,
                        onSelectedDaysChange = onSelectedDaysChange
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } else if (role == "Employee") {
                if (contract.pending){
                    Row(modifier = Modifier.fillMaxWidth()) {
                        CustomButton(
                            backgroundColor = blue,
                            text = "Confirm",
                            textColor = 0xFFFFFFFF,
                            onClickChange = onConfirmPending,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 4.dp)
                        )
                        CustomButton(
                            backgroundColor = red,
                            text = "Reject",
                            textColor = 0xFFFFFFFF,
                            onClickChange = onRejectPending,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp)
                        )
                    }
                }
                if (
                    contract.contractStatus == ContractStatus.BOOKED &&
                    contract.paymentStatus == PaymentStatus.SUCCESS &&
                    LocalDate.now().equals(contract.startDate)
                    && !contract.pending
                ) {
                    CustomButton(
                        backgroundColor = blue,
                        text = "Confirm Picked-up",
                        textColor = 0xFFFFFFFF,
                        onClickChange = onConfirmPickUp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (LocalDate.now() >= contract.endDate && contract.contractStatus == ContractStatus.PICKED_UP) {
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomButton(
                        backgroundColor = blue,
                        text = "Confirm Return",
                        textColor = 0xFFFFFFFF,
                        onClickChange = onConfirmReturn,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else if (role == "Admin")
            {
                CustomButton(
                    backgroundColor = blue,
                    text = "Assign Contract",
                    textColor = 0xFFFFFFFF,
                    onClickChange = onAssignContract,
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }
    }
}
@Composable
fun ContractRow(label: String, value: String, valueColor: Color = Color.Black) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(label, fontFamily = FontFamily(Font(R.font.montserrat_semibold)), fontSize = 14.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, color = valueColor,fontFamily = FontFamily(Font(R.font.montserrat_medium)), fontSize = 14.sp)
    }
}
@Composable
fun ExtendContractSection(
    selectedDays: Int,
    onExtendContract: () -> Unit,
    onSelectedDaysChange: (Int) -> Unit
) {
    val daysOptions = (0..3).toList()
    var expanded by remember { mutableStateOf(false) }
    val blue = Color(0xFF0D6EFD)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth().wrapContentHeight()
    ) {
        Box {
            OutlinedButton(onClick = { expanded = true },) {
                Text("Extend: $selectedDays day(s)")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                daysOptions.forEach { day ->
                    DropdownMenuItem(
                        text = { Text("$day day(s)") },
                        onClick = {
                            onSelectedDaysChange(day)
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(10.dp))

        Button(
            onClick = onExtendContract,
            enabled = selectedDays > 0,
            colors = ButtonDefaults.buttonColors(blue),
            modifier = Modifier
                .wrapContentSize()
        ) {
            Text(
                text = "Extend Contract",
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}