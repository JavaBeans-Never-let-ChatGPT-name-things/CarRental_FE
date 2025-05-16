package com.example.carrental_fe.screen.admin.adminContractList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carrental_fe.model.Contract
import com.example.carrental_fe.screen.user.userContractScreen.ContractCard
import com.example.carrental_fe.screen.user.userHomeScreen.TopTitle

@Composable
fun PendingContractScreen(vm: PendingContractViewModel = viewModel(factory = PendingContractViewModel.Factory)){
    val contracts = vm.pendingContracts.collectAsState().value
    val pagerState = rememberPagerState(pageCount = { contracts.size })

    var showAssignDialog by remember { mutableStateOf(false) }
    var selectedContract by remember { mutableStateOf<Contract?>(null) }
    var selectedEmployee by remember { mutableStateOf<String?>(null) }
    val employees = vm.employees.collectAsState().value
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 16.dp)
    ) {
        TopTitle("Pending Contracts")
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
                    role = "Admin",
                    onAssignContract = {
                        selectedContract = contract
                        vm.fetchAvailableEmployees(contract.id)
                        showAssignDialog = true
                    }
                )
            }
        }
        if (showAssignDialog) {
            AlertDialog(
                onDismissRequest = { showAssignDialog = false },
                confirmButton = {
                    Button(
                        onClick = {
                            selectedEmployee?.let {
                                vm.assignContract(selectedContract!!.id, it)
                            }
                            showAssignDialog = false
                        },
                        enabled = selectedEmployee != null
                    ) {
                        Text("Assign")
                    }
                },
                dismissButton = {
                    Button(onClick = { showAssignDialog = false }) {
                        Text("Cancel")
                    }
                },
                title = { Text("Choose Employee to Manage Contract") },
                text = {
                    Column {
                        Text("Employee Name:")
                        Spacer(modifier = Modifier.height(8.dp))
                        var expanded by remember { mutableStateOf(false) }
                        Box {
                            OutlinedTextField(
                                value = selectedEmployee ?: "",
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Select Employee") },
                                trailingIcon = {
                                    Icon(
                                        Icons.Default.ArrowDropDown,
                                        contentDescription = null,
                                        Modifier.clickable { expanded = !expanded }
                                    )
                                }
                            )
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                employees.forEach { employee ->
                                    DropdownMenuItem(
                                        text = { Text(employee) },
                                        onClick = {
                                            selectedEmployee = employee
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}