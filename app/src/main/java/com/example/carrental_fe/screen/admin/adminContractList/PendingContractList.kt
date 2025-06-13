package com.example.carrental_fe.screen.admin.adminContractList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carrental_fe.R
import com.example.carrental_fe.dialog.SuccessDialog
import com.example.carrental_fe.model.Contract
import com.example.carrental_fe.screen.user.userContractScreen.ContractCard
import com.example.carrental_fe.screen.user.userHomeScreen.TopTitle

@Composable
fun PendingContractScreen(vm: PendingContractViewModel = viewModel(factory = PendingContractViewModel.Factory)){
    val contracts = vm.filteredContracts.collectAsState().value
    val pagerState = rememberPagerState(pageCount = { contracts.size })

    var showAssignDialog by remember { mutableStateOf(false) }
    var selectedContract by remember { mutableStateOf<Contract?>(null) }
    var selectedEmployee by remember { mutableStateOf<String?>(null) }
    val employees = vm.employees.collectAsState().value

    val filterOptions = vm.filterOptions.collectAsState().value
    val selectedFilter = vm.selectedFilter.collectAsState()
    val selectedSort = vm.selectedSort.collectAsState()
    val message = vm.message.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
            .padding(top = 16.dp)
    ) {
        TopTitle("Pending Contracts")
        Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    var expandedFilter by remember { mutableStateOf(false) }
                    Text("Filter By:", fontFamily = FontFamily(Font(R.font.montserrat_semibold)), fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Box{
                        Text(
                            text = selectedFilter.value,
                            modifier = Modifier
                                .clickable { expandedFilter = true }
                                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                        DropdownMenu(
                            expanded = expandedFilter,
                            onDismissRequest = { expandedFilter = false }
                        ) {
                            filterOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        vm.setFilter(option)
                                        expandedFilter = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    var expandedSort by remember { mutableStateOf(false) }
                    Text("Sort By:", fontFamily = FontFamily(Font(R.font.montserrat_semibold)), fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Box {
                        Text(
                            text = selectedSort.value,
                            modifier = Modifier
                                .clickable { expandedSort = true }
                                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                        DropdownMenu(
                            expanded = expandedSort,
                            onDismissRequest = { expandedSort = false }
                        ) {
                            listOf("StartDate ASC", "StartDate DESC").forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        vm.setSort(option)
                                        expandedSort = false
                                    }
                                )
                            }
                        }
                    }
                }
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 20.dp),
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val contract = contracts[page]
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
        message?.let{
            SuccessDialog(text = message) {
                vm.clearMessage()
            }
        }
    }
}