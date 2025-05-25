package com.example.carrental_fe.screen.admin.adminAnalytics

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.carrental_fe.R
import com.example.carrental_fe.dto.response.CarSummaryDTO
import com.example.carrental_fe.dto.response.ContractSummaryDTO
import com.example.carrental_fe.dto.response.MonthlyReportDTO
import com.example.carrental_fe.dto.response.UserSummaryDTO
import com.example.carrental_fe.screen.user.userHomeScreen.TopTitle
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import android.graphics.Color as AndroidColor

@Composable
fun AnalyticsScreen(viewModel: AnalyticsViewModel = viewModel(factory = AnalyticsViewModel.Factory)) {
    val scrollState = rememberScrollState()
    val years = listOf("2024","2025")
    val selectedYear = viewModel.selectedYear.collectAsState().value
    val carFilters = listOf("Rental Count","Rating")
    val selectedFilter = viewModel.carFilterOption.collectAsState().value
    var expandedYear by remember { mutableStateOf(false) }
    var expandedFilter by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        TopTitle("Analytics")
        // Year Dropdown
        DropdownMenuSelector(
            expanded = expandedYear,
            label="Select Year",
            options=years,
            selectedOption=selectedYear.toString(),
            onOptionSelected={ viewModel.updateYear(it.toInt()) },
            onChange={ expandedYear = !expandedYear },
            onChangeExpanded = { expandedYear = false }
        )

        Spacer(Modifier.height(16.dp))

        // BarChart (Revenue & Penalty)
        DualBarChart(
            revenueData = viewModel.monthlyRevenue.collectAsState().value,
            penaltyData = viewModel.monthlyPenalty.collectAsState().value,
            revenueColor = Color(0xFF0D6EFD),
            penaltyColor = Color(0xFFF40707)
        )

        Spacer(Modifier.height(16.dp))

        // Date Range Picker with Reset
        DateRangePicker(
            startDate = viewModel.startDate.collectAsState().value,
            endDate = viewModel.endDate.collectAsState().value,
            onDateSelected = { start, end -> viewModel.updateDateRange(start, end) },
            onReset = { viewModel.resetDateRange() }
        )

        Spacer(Modifier.height(16.dp))

        // Revenue & Penalty Summary
        SummaryCard(title = "Total Revenue", value = viewModel.totalRevenue.collectAsState().value)
        SummaryCard(title = "Total Penalty", value = viewModel.totalPenalty.collectAsState().value)

        Spacer(Modifier.height(16.dp))

        // Contract Summary + PieChart
        ContractSummarySection(
            contractSummary = viewModel.contractSummary.collectAsState().value,
        )

        Spacer(Modifier.height(16.dp))

        // Return Car Status PieChart
        PieChartSection(
            title = "Return Car Status",
            data = viewModel.returnStatus.collectAsState().value.map { it.returnCarStatus.name to it.value.toFloat() }
        )

        Spacer(Modifier.height(16.dp))

        // Top 3 Cars Filter
        DropdownMenuSelector(
            expanded = expandedFilter,
            label="Top 3 Cars By",
            options=carFilters,
            selectedOption=selectedFilter,
            onOptionSelected={ viewModel.updateCarFilter(it) },
            onChange={ expandedFilter = !expandedFilter },
            onChangeExpanded = { expandedFilter = false }
        )

        Spacer(Modifier.height(8.dp))

        Top3CarSection(selectedOption = selectedFilter, cars = viewModel.top3Cars.collectAsState().value)

        Spacer(Modifier.height(16.dp))

        // Top 3 Best Users
        Top3UserSection(title = "Top 3 Best Users", users = viewModel.top3BestUsers.collectAsState().value)

        Spacer(Modifier.height(16.dp))

        // Top 3 Worst Users
        Top3UserSection(title = "Top 3 Worst Users", users = viewModel.top3WorstUsers.collectAsState().value)
    }
}
@Composable
fun DropdownMenuSelector(
    expanded: Boolean,
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    onChange: () -> Unit,
    onChangeExpanded:()->Unit
) {

    Column {
        Text(label, fontWeight = FontWeight.Medium)
        Box {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth(),
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, null, modifier = Modifier.clickable{ onChange() }) }
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { onChangeExpanded() }) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            onChangeExpanded()
                        }
                    )
                }
            }
        }
    }
}
@Composable
fun DualBarChart(
    revenueData: List<MonthlyReportDTO>,
    penaltyData: List<MonthlyReportDTO>,
    revenueColor: Color,
    penaltyColor: Color
) {
    AndroidView(
        factory = { ctx ->
            BarChart(ctx).apply {
                description.isEnabled = false
                setDrawGridBackground(false)
                axisRight.isEnabled = false
                legend.isEnabled = true

                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setCenterAxisLabels(true)
                xAxis.granularity = 1f
                xAxis.isGranularityEnabled = true
                xAxis.setDrawLabels(true)
                xAxis.isAvoidFirstLastClippingEnabled

                axisLeft.axisMinimum = 0f
            }
        },
        update = { chart ->
            val months = (revenueData.map { it.month } + penaltyData.map { it.month })
                .distinct()
                .sorted()

            val revMap = revenueData.associate { it.month to it.value.toFloat() }
            val penMap = penaltyData.associate { it.month to it.value.toFloat() }

            val revEntries = months.mapIndexed { idx, month ->
                BarEntry(idx.toFloat(), revMap[month] ?: 0f)
            }
            val penEntries = months.mapIndexed { idx, month ->
                BarEntry(idx.toFloat(), penMap[month] ?: 0f)
            }

            val revSet = BarDataSet(revEntries, "Revenue").apply { color = revenueColor.toArgb() }
            val penSet = BarDataSet(penEntries, "Penalty").apply { color = penaltyColor.toArgb() }
            chart.post {
                val barWidth  = 0.4f
                val barSpace  = 0.05f
                val groupSpace = 1f - (barWidth + barSpace) * 2

                val data = BarData(revSet, penSet).apply {
                    this.barWidth = barWidth
                }

                chart.setFitBars(false)
                val groupCount  = months.size
                val startX      = 0f
                val groupWidth  = data.getGroupWidth(groupSpace, barSpace)

                chart.xAxis.apply {
                    axisMinimum               = startX
                    axisMaximum               = startX + groupCount * groupWidth
                    setLabelCount(groupCount, false)
                    granularity               = 1f
                    isGranularityEnabled      = true
                    setDrawLabels(true)
                    valueFormatter            = IndexAxisValueFormatter(months.map { it.toString() })
                    setCenterAxisLabels(true)
                    isAvoidFirstLastClippingEnabled
                }

                data.groupBars(startX, groupSpace, barSpace)
                chart.data = data
                chart.invalidate()

            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

@Composable
fun DateRangePicker(
    startDate: LocalDate?,
    endDate: LocalDate?,
    onDateSelected: (LocalDate?, LocalDate?) -> Unit,
    onReset: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            LabeledDatePicker(
                label = "Start Date",
                selectedDate = startDate,
                onDateSelected = { onDateSelected(it, endDate) }
            )
            LabeledDatePicker(
                label = "End Date",
                selectedDate = endDate,
                onDateSelected = { onDateSelected(startDate, it) }
            )
        }
        Spacer(Modifier.height(4.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = onReset) {
                Text("Reset Date Range")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabeledDatePicker(
    label: String,
    selectedDate: LocalDate?,
    modifier: Modifier = Modifier,
    onDateSelected: (LocalDate?) -> Unit
) {
    val fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    var show by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(label, fontWeight = FontWeight.Medium)
        Row(
            modifier = Modifier
                .wrapContentSize()
                .clickable { show = true },
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(selectedDate?.format(fmt) ?: "-Select Date-", color = Color.Gray)
            Icon(Icons.Default.CalendarToday, null)
        }
    }

    if (show) {
        val state = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
                ?.atStartOfDay(ZoneId.systemDefault())
                ?.toInstant()?.toEpochMilli()
        )
        DatePickerDialog(
            onDismissRequest = { show = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let {
                        onDateSelected(
                            Instant.ofEpochMilli(it)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        )
                    }
                    show = false
                }) { Text("OK") }
            }
        ) {
            DatePicker(state = state)
        }
    }
}

@Composable
fun SummaryCard(title: String, value: Double) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold)
            Text("$value", style = MaterialTheme.typography.headlineSmall)
        }
    }
}

@Composable
fun ContractSummarySection(contractSummary: List<ContractSummaryDTO>) {
    LazyRow {
        items(contractSummary) { item ->
            Card(modifier = Modifier.padding(8.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(item.contractStatus.name, fontWeight = FontWeight.Bold)
                    Text("${item.value}")
                }
            }
        }
    }
    Spacer(Modifier.height(8.dp))
    PieChartSection(title = "Contract Summary", data = contractSummary.map { it.contractStatus.name to it.value.toFloat() })
}

@Composable
fun PieChartSection(title: String, data: List<Pair<String, Float>>) {
    Column {
        Text(title, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        AndroidView(
            factory = { ctx ->
                PieChart(ctx).apply {
                    description.isEnabled = false
                    isDrawHoleEnabled = true
                    setEntryLabelColor(AndroidColor.BLACK)
                    legend.isEnabled = true
                }
            },
            update = { chart ->
                val entries = data.map { PieEntry(it.second, it.first) }
                val set = PieDataSet(entries, "").apply {
                    // ví dụ dùng 4 màu, customize theo ý
                    colors = listOf(
                        "#0D6EFD".toColorInt(),
                        "#F40707".toColorInt(),
                        "#2ECC71".toColorInt(),
                        "#F1C40F".toColorInt()
                    )
                }
                chart.data = PieData(set)
                chart.invalidate()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )
    }
}

@Composable
fun Top3CarSection(selectedOption: String, cars: List<CarSummaryDTO>) {
    Column {
        cars.forEach {
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
                    AsyncImage(model = it.imageUrl, contentDescription = null, modifier = Modifier.size(64.dp))
                    Spacer(Modifier.width(16.dp))
                    Column {
                        if (selectedOption == "Rental Count") Text("Rental: ${it.rentalCount}") else Text("Rating: ${it.rating}")
                    }
                }
            }
        }
    }
}

@Composable
fun Top3UserSection(title: String, users: List<UserSummaryDTO>) {
    Text(title, fontWeight = FontWeight.Bold)
    Column {
        users.forEach {
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
                    AsyncImage(model = it.avatarUrl,
                        placeholder = painterResource(id = if (it.gender == 1) R.drawable.male else R.drawable.female),
                        error = painterResource(id = if (it.gender == 1) R.drawable.male else R.drawable.female),
                        contentDescription = null,
                        modifier = Modifier.size(64.dp))
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(it.displayName)
                        Text(it.email)
                        Text(
                            "Credit: ${it.creditPoint}",
                            color = if (it.creditPoint >= 0) Color(0xFF0D6EFD) else Color(0xFFF40707)
                        )
                    }
                }
            }
        }
    }
}