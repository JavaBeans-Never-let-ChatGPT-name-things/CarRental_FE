package com.example.carrental_fe.screen.admin.adminAnalytics

import android.R.attr.bitmap
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.print.PrintAttributes
import android.print.PrintManager
import android.view.PixelCopy
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.toColorInt
import androidx.core.view.doOnLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.carrental_fe.R
import com.example.carrental_fe.dto.response.CarSummaryDTO
import com.example.carrental_fe.dto.response.ContractSummaryDTO
import com.example.carrental_fe.dto.response.MonthlyReportDTO
import com.example.carrental_fe.dto.response.UserSummaryDTO
import com.example.carrental_fe.screen.user.userHomeScreen.TopTitle
import com.example.carrental_fe.model.enums.ContractStatus
import com.example.carrental_fe.screen.admin.adminUserList.InfoRow
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
import com.github.mikephil.charting.utils.ColorTemplate
import java.io.File
import java.io.FileOutputStream
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.min
import android.graphics.Color as AndroidColor
import androidx.core.graphics.createBitmap

//@SuppressLint("ContextCastToActivity")
//@Composable
//fun AnalyticsScreenWithExportPdf(
//    viewModel: AnalyticsViewModel = viewModel(factory = AnalyticsViewModel.Factory)
//) {
//    val context = LocalContext.current
//    val activity = LocalContext.current as? Activity
//    val view = LocalView.current
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        // 1. Scrollable content, không fillMaxSize 100% để còn chừa chỗ cho button
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(bottom = 80.dp) // chừa 80dp cho button
//        ) {
//            AnalyticsScreen(viewModel = viewModel)
//        }
//        ExtendedFloatingActionButton(
//            onClick = {
//                if (activity == null) {
//                    Toast.makeText(context, "Không lấy được Activity", Toast.LENGTH_SHORT).show()
//                    return@ExtendedFloatingActionButton
//                }
//
//                // 1. Tạo software bitmap
//                val width = view.width
//                val height = view.height
//                if (width == 0 || height == 0) {
//                    Toast.makeText(context, "View chưa layout xong", Toast.LENGTH_SHORT).show()
//                    return@ExtendedFloatingActionButton
//                }
//                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//
//                // 2. Dùng PixelCopy để copy từ window
//                val location = IntArray(2).also { view.getLocationInWindow(it) }
//                val srcRect = Rect(
//                    location[0], location[1],
//                    location[0] + width, location[1] + height
//                )
//                PixelCopy.request(
//                    activity.window,
//                    srcRect,
//                    bitmap,
//                    { copyResult ->
//                        if (copyResult == PixelCopy.SUCCESS) {
//                            // 3. Ghi bitmap vào PDF
//                            val document = PdfDocument()
//                            val pageInfo = PdfDocument.PageInfo
//                                .Builder(width, height, 1)
//                                .create()
//                            val page = document.startPage(pageInfo)
//                            page.canvas.drawBitmap(bitmap, 0f, 0f, null)
//                            document.finishPage(page)
//
//                            val docsDir = context
//                                .getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
//                            docsDir?.mkdirs()
//                            val file = File(docsDir, "analytics_report.pdf")
//                            document.writeTo(FileOutputStream(file))
//                            document.close()
//
//                            Toast
//                                .makeText(context, "PDF saved: ${file.absolutePath}", Toast.LENGTH_LONG)
//                                .show()
//                        } else {
//                            Toast
//                                .makeText(context, "PixelCopy thất bại: $copyResult", Toast.LENGTH_LONG)
//                                .show()
//                        }
//                    },
//                    Handler(Looper.getMainLooper())
//                )
//            },
//            icon = { Icon(Icons.Filled.PictureAsPdf, contentDescription = null) },
//            text = { Text("Export PDF") },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//                .align(Alignment.BottomCenter)
//        )
//    }
//}

@SuppressLint("ContextCastToActivity")
@Composable
fun AnalyticsScreenWithExportPdf(
    viewModel: AnalyticsViewModel = viewModel(factory = AnalyticsViewModel.Factory)
) {
    val context = LocalContext.current
    val activity = context as? Activity

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    if (activity != null) exportAnalyticsToPdf(activity, viewModel)
                },
                icon = { Icon(Icons.Filled.PictureAsPdf, contentDescription = null) },
                text = { Text("Export PDF") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            AnalyticsScreen(viewModel)
        }
    }
}


fun exportAnalyticsToPdf(activity: Activity, viewModel: AnalyticsViewModel) {
    val context = activity
    val decor = activity.window.decorView as ViewGroup

    // Tạo ComposeView off-screen hiển thị AnalyticsPrintableScreen
    val composeView = ComposeView(context).apply {
        alpha = 0f
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        setContent { AnalyticsPrintableScreen(viewModel) }
    }
    decor.addView(
        composeView,
        FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    )

    composeView.doOnLayout {
        val width = composeView.width
        val height = composeView.height

        if (width == 0 || height == 0) {
            Toast.makeText(context, "Layout chưa sẵn sàng", Toast.LENGTH_SHORT).show()
            decor.removeView(composeView)
            return@doOnLayout
        }

        val bitmap = createBitmap(width, height)
        val loc = IntArray(2).also(composeView::getLocationInWindow)
        val rect = Rect(loc[0], loc[1], loc[0] + width, loc[1] + height)

        PixelCopy.request(
            activity.window,
            rect,
            bitmap,
            { result ->
                if (result == PixelCopy.SUCCESS) {
                    val pdf = PdfDocument()
                    val sliceHeight = context.resources.displayMetrics.heightPixels
                    var offsetY = 0
                    var page = 1

                    while (offsetY < bitmap.height) {
                        val h = minOf(sliceHeight, bitmap.height - offsetY)
                        val pageInfo = PdfDocument.PageInfo.Builder(width, h, page++).create()
                        val p = pdf.startPage(pageInfo)
                        val src = Rect(0, offsetY, width, offsetY + h)
                        val dst = Rect(0, 0, width, h)
                        p.canvas.drawBitmap(bitmap, src, dst, null)
                        pdf.finishPage(p)
                        offsetY += h
                    }

                    val file = File(
                        context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                        "analytics_report.pdf"
                    )
                    pdf.writeTo(FileOutputStream(file))
                    pdf.close()
                    Toast.makeText(context, "PDF đã lưu tại:\n${file.absolutePath}", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, "PixelCopy thất bại: $result", Toast.LENGTH_LONG).show()
                }
                decor.removeView(composeView)
            },
            Handler(Looper.getMainLooper())
        )
    }
}



@Composable
fun AnalyticsPrintableScreen(viewModel: AnalyticsViewModel) {
    val selectedFilter = viewModel.carFilterOption.collectAsState().value
    val contractSummary = viewModel.contractSummary.collectAsState().value
    val returnStatus = viewModel.returnStatus.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TopTitle("Analytics")
        Spacer(Modifier.height(16.dp))
        DualBarChart(
            revenueData = viewModel.monthlyRevenue.collectAsState().value,
            penaltyData = viewModel.monthlyPenalty.collectAsState().value,
            revenueColor = Color(0xFF0D6EFD),
            penaltyColor = Color(0xFFF40707)
        )

        Spacer(Modifier.height(16.dp))
        SummaryCard("Total Revenue", viewModel.totalRevenue.collectAsState().value)
        SummaryCard("Total Penalty", viewModel.totalPenalty.collectAsState().value)

        Spacer(Modifier.height(16.dp))
        Text("Contract Summary", fontSize = 18.sp)
        AndroidView(
            factory = {
                generatePieChartView(
                    it,
                    contractSummary.map { it.contractStatus.name to it.value.toFloat() }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(Modifier.height(16.dp))
        Text("Return Car Status", fontSize = 18.sp)
        AndroidView(
            factory = {
                generatePieChartView(
                    it,
                    returnStatus.map { it.returnCarStatus.name to it.value.toFloat() }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(Modifier.height(16.dp))
        Top3CarSection(selectedFilter, viewModel.top3Cars.collectAsState().value)
        Top3UserSection("Top 3 Best Users", viewModel.top3BestUsers.collectAsState().value)
        Top3UserSection("Top 3 Worst Users", viewModel.top3WorstUsers.collectAsState().value)
    }
}

fun generatePieChartView(context: Context, data: List<Pair<String, Float>>): PieChart {
    return PieChart(context).apply {
        layoutParams = ViewGroup.LayoutParams(600, 600)
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        description.isEnabled = false
        isDrawHoleEnabled = true
        legend.isEnabled = true

        val entries = data.map { PieEntry(it.second, it.first) }
        val dataSet = PieDataSet(entries, "").apply {
            colors = ColorTemplate.MATERIAL_COLORS.toList()
            valueTextSize = 14f
        }

        this.data = PieData(dataSet)
        measure(
            View.MeasureSpec.makeMeasureSpec(600, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(600, View.MeasureSpec.EXACTLY)
        )
        layout(0, 0, 600, 600)
    }
}
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

        Spacer(Modifier.height(20.dp))

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
        Text(text = label,
                fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
            fontSize = 18.sp
        )
        Spacer(Modifier.height(4.dp))
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
                Text("Reset Date Range", color = Color(0xFF0D6EFD))
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
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween)
    {
        Text(
            text = title,
            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
            color = if (title == "Total Revenue") Color(0xFF0D6EFD) else Color(0xFFF40707),
            fontSize = 18.sp,
            )
        Text(
            text = "$value $",
            fontFamily = FontFamily(Font(R.font.montserrat_bold)),
            color = Color.Black,
            fontSize = 16.sp,
            )
    }
}

@Composable
fun ContractSummarySection(contractSummary: List<ContractSummaryDTO>) {
    LazyRow {
        items(contractSummary) { item ->
            Card(modifier = Modifier.padding(8.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(Color(0xFFF7F7F9))) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Image(
                        painter = painterResource(if (item.contractStatus == ContractStatus.PICKED_UP ||
                            item.contractStatus == ContractStatus.REVIEWED ||
                            item.contractStatus == ContractStatus.COMPLETE ||
                            item.contractStatus == ContractStatus.BOOKED) R.drawable.good else R.drawable.bad),
                        contentDescription = null,
                    )
                    Text(item.contractStatus.name, fontFamily = FontFamily(Font(R.font.montserrat_medium)), fontWeight = FontWeight.Bold)
                    Text("${item.value}", fontFamily = FontFamily(Font(R.font.montserrat_regular)))
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
        Text(text = title,
            fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
            fontSize = 18.sp
        )
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
            CarCard(car = it, selectedOption = selectedOption)
        }
    }
}
@Composable
fun CarCard(car: CarSummaryDTO, selectedOption: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                model = car.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .weight(1.5f)
                    .aspectRatio(1.6f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = car.id.uppercase(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (selectedOption != "Rental Count")
                    {
                        Text(
                            text = "RATING:",
                            fontSize = 14.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = car.rating.toString(),
                            fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                            fontSize = 14.sp
                        )
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier
                                .size(18.dp)
                                .padding(start = 4.dp)
                        )
                    }
                    else{
                        Text(
                            text = "RENTAL COUNT:",
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat_semibold))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = car.rentalCount.toString(),
                            fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Top3UserSection(title: String, users: List<UserSummaryDTO>) {
    Text(text = title,
        fontFamily = FontFamily(Font(R.font.montserrat_semibold)),
        fontSize = 18.sp
    )
    Column (
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        users.forEach {
            Card(
                    modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(5.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F9))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AsyncImage(
                            model = it.avatarUrl,
                            contentDescription = "Profile Image",
                            placeholder = painterResource(id = if (it.gender == 1) R.drawable.male else R.drawable.female),
                            error = painterResource(id = if (it.gender == 1) R.drawable.male else R.drawable.female),
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        InfoRow(
                            icon = Icons.Default.Person,
                            label = "Display Name:",
                            value = it.displayName
                        )
                        Spacer(Modifier.height(8.dp))
                        InfoRow(
                            icon = Icons.Default.Phone,
                            label = "Phone Number:",
                            value = it.phoneNumber ?: ""
                        )
                        Spacer(Modifier.height(8.dp))
                        InfoRow(icon = Icons.Default.Email, label = "Email:", value = it.email)
                        Spacer(Modifier.height(8.dp))
                        InfoRow(
                            icon = Icons.Default.Receipt,
                            label = "Credit Point",
                            value = "${it.creditPoint}",
                            isGoodCredit = (if (it.creditPoint>0) true else false)
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
fun AnalyticsScreenPreview() {
    AnalyticsScreen()
}