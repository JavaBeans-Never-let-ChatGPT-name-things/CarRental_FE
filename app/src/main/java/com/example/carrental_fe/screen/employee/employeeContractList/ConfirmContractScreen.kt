package com.example.carrental_fe.screen.employee.employeeContractList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.carrental_fe.screen.user.userHomeScreen.TopTitle

@Composable
fun ConfirmContractScreen(vm: ConfirmContractViewModel = viewModel(factory = ConfirmContractViewModel.Factory)){
    val contracts = vm.contracts.collectAsState().value
    val pagerState = rememberPagerState(pageCount = { contracts.size })
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 16.dp)
    ){
        TopTitle("Confirm Contracts")
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
            }
        }
    }
}