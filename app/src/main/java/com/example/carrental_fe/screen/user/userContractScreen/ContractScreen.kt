package com.example.carrental_fe.screen.user.userContractScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.carrental_fe.R
import com.example.carrental_fe.dialog.ReviewDialog
import com.example.carrental_fe.model.enums.ContractStatus
import com.example.carrental_fe.model.enums.PaymentStatus
import com.example.carrental_fe.screen.component.CustomButton
import com.example.carrental_fe.screen.user.userHomeScreen.TopTitle
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ContractScreen(
    vm: ContractViewModel = viewModel(factory = ContractViewModel.Factory),
    onCheckoutNav: (String, String, Long?) -> Unit,
) {
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val contracts = vm.contracts.collectAsState().value
    val showDialog = vm.showReviewDialog.collectAsState()
    val stars = vm.reviewStars.collectAsState()
    val comment = vm.reviewComment.collectAsState()
    val blue = Color(0xFF0D6EFD)
    val red = Color.Red
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(top = 16.dp)
    )
    {
        TopTitle("Contracts")
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(contracts) { contract ->
                Card(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(12.dp),
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

                        ContractRow("Total Price", "$${contract.totalPrice.toInt()}")

                        ContractRow("Deposit", "$${contract.deposit.toInt()}")
                        Spacer(modifier = Modifier.height(16.dp))

                        if (contract.contractStatus == ContractStatus.COMPLETE) {
                            CustomButton(
                                backgroundColor = blue,
                                text = "Review Now",
                                textColor = 0xFFFFFFFF,
                                onClickChange = { vm.onReviewClick(contract.id) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else if (contract.paymentStatus == PaymentStatus.FAILED) {
                            CustomButton(
                                backgroundColor = blue,
                                text = "Retry",
                                textColor = 0xFFFFFFFF,
                                onClickChange = { vm.retryContract(contract.id, contract.carId, onCheckoutNav) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
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
}
@Composable
fun ContractRow(label: String, value: String, valueColor: Color = Color.Black) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(label, fontFamily = FontFamily(Font(R.font.montserrat_semibold)), fontSize = 14.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, color = valueColor,fontFamily = FontFamily(Font(R.font.montserrat_medium)), fontSize = 14.sp)
    }
}