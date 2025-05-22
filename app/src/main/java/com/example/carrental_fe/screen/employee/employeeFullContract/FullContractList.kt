package com.example.carrental_fe.screen.employee.emplooyeeFullContract

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carrental_fe.model.enums.ReturnCarStatus
import com.example.carrental_fe.screen.user.userContractScreen.ContractCard
import com.example.carrental_fe.screen.user.userHomeScreen.TopTitle

@Composable
fun FullContractScreen(vm: FullContractViewModel = viewModel(factory = FullContractViewModel.Factory)){
    val contracts = vm.contracts.collectAsState().value
    val pagerState = rememberPagerState(pageCount = { contracts.size })

    var showReturnDialog by remember { mutableStateOf(false) }
    var selectedContractId by remember { mutableStateOf<Long?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 16.dp)
    ){
        TopTitle("Contracts Incharge")
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 20.dp),
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val contract = contracts[page]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 16.dp)
            ) {
                ContractCard(
                    contract = contract,
                    role = "Employee",
                    onConfirmPickUp = { vm.confirmPickup(contract.id)},
                    onConfirmReturn = {
                        selectedContractId = contract.id
                        showReturnDialog = true
                    }
                )
            }
        }
    }
    if (showReturnDialog && selectedContractId != null) {
        ConfirmReturnDialog(
            onConfirm = { returnCarStatus ->
                vm.confirmReturn(selectedContractId!!, returnCarStatus)
                showReturnDialog = false
                selectedContractId = null
            },
            onDismiss = {
                showReturnDialog = false
                selectedContractId = null
            }
        )
    }
}
@Composable
fun ConfirmReturnDialog(
    onConfirm: (ReturnCarStatus) -> Unit,
    onDismiss: () -> Unit
) {
    val options = listOf(ReturnCarStatus.INTACT, ReturnCarStatus.DAMAGED)
    var selectedOption by remember { mutableStateOf<ReturnCarStatus?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirm Return") },
        text = {
            Column {
                options.forEach { status ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedOption = status }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedOption == status,
                            onClick = { selectedOption = status }
                        )
                        Text(status.name)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedOption?.let { onConfirm(it) }
                },
                enabled = selectedOption != null
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}