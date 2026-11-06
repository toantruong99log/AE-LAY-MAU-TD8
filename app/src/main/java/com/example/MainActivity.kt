package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.Amber500
import com.example.ui.theme.Amber600
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToLong

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ConcreteSampleCalculatorApp()
            }
        }
    }
}

/**
 * 5 loại bê tông / vữa theo yêu cầu
 */
enum class ConcreteType(
    val id: Int,
    val title: String
) {
    VUA(1, "Vữa"),
    BE_TONG(2, "Bê tông"),
    BE_TONG_CO_THAM(3, "Bê tông - có thấm"),
    BE_TONG_CO_R7(4, "Bê tông - có R7"),
    BE_TONG_CO_R7_CO_THAM(5, "Bê tông - có R7, có thấm")
}

/**
 * Kết quả tính số tổ mẫu
 */
data class SampleResult(
    val r7: Int,
    val r28: Int,
    val luu: Int,
    val tham: Int
)

/**
 * Hàm tính số tổ mẫu:
 * - Làm tròn P về tối đa 2 chữ số thập phân trước khi chia
 * - Nếu P để trống hoặc <= 0 thì tất cả kết quả = 0
 */
fun calculateSampleResult(volumeInput: String, type: ConcreteType): SampleResult {
    val sanitized = volumeInput.trim().replace(',', '.')
    if (sanitized.isEmpty()) {
        return SampleResult(r7 = 0, r28 = 0, luu = 0, tham = 0)
    }

    val pDouble = sanitized.toDoubleOrNull()
    if (pDouble == null || pDouble <= 0.0) {
        return SampleResult(r7 = 0, r28 = 0, luu = 0, tham = 0)
    }

    // Làm tròn P về tối đa 2 chữ số thập phân trước khi chia
    val pBd = try {
        BigDecimal(sanitized).setScale(2, RoundingMode.HALF_UP)
    } catch (_: Exception) {
        BigDecimal.valueOf((pDouble * 100.0).roundToLong()).divide(BigDecimal.valueOf(100))
    }

    if (pBd <= BigDecimal.ZERO) {
        return SampleResult(r7 = 0, r28 = 0, luu = 0, tham = 0)
    }

    fun ceilDiv(value: BigDecimal, divisor: Long): Int {
        return value.divide(BigDecimal.valueOf(divisor), 0, RoundingMode.CEILING).toInt()
    }

    return when (type) {
        ConcreteType.VUA -> {
            val r28 = ceilDiv(pBd, 500)
            SampleResult(
                r7 = 0,
                r28 = r28,
                luu = r28,
                tham = 0
            )
        }
        ConcreteType.BE_TONG -> {
            val r28 = ceilDiv(pBd, 20)
            val luu = Math.ceil(r28 / 3.0).toInt() * 2
            SampleResult(
                r7 = 0,
                r28 = r28,
                luu = luu,
                tham = 0
            )
        }
        ConcreteType.BE_TONG_CO_THAM -> {
            val r28 = ceilDiv(pBd, 20)
            val luu = Math.ceil(r28 / 3.0).toInt() * 2
            SampleResult(
                r7 = 0,
                r28 = r28,
                luu = luu,
                tham = 1
            )
        }
        ConcreteType.BE_TONG_CO_R7 -> {
            val count = ceilDiv(pBd, 20)
            SampleResult(
                r7 = count,
                r28 = count,
                luu = count,
                tham = 0
            )
        }
        ConcreteType.BE_TONG_CO_R7_CO_THAM -> {
            val count = ceilDiv(pBd, 20)
            SampleResult(
                r7 = count,
                r28 = count,
                luu = count,
                tham = 1
            )
        }
    }
}

@Composable
fun ConcreteSampleCalculatorApp() {
    var volumeInput by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(ConcreteType.BE_TONG) }

    val result = calculateSampleResult(volumeInput, selectedType)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF8FAFC)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 480.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Header: Title chính "AE Lấy mẫu mãi đỉnh", Title phụ "tính tổ mẫu bê đê"
                AppHeader()

                // Mục 1: Khối lượng bê tông (Gọn gàng)
                VolumeInputCard(
                    value = volumeInput,
                    onValueChange = { newValue ->
                        val filtered = newValue.filter { char ->
                            char.isDigit() || char == '.' || char == ','
                        }
                        volumeInput = filtered
                    },
                    onClear = { volumeInput = "" }
                )

                // Mục 2: Loại bê tông / vữa (Bố cục gọn gàng dễ bấm không chiếm nhiều chiều cao)
                ConcreteTypeCard(
                    selectedType = selectedType,
                    onTypeSelected = { selectedType = it }
                )

                // Mục 3: Kết quả số tổ mẫu (Thiết kế dạng lưới 2x2 siêu gọn, hiển thị trọn vẹn không cần cuộn)
                ResultCard(result = result)

                // Chân trang
                Text(
                    text = "Liên hệ 113 để biết thêm chi tiết",
                    fontSize = 12.sp,
                    color = Slate400,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp, bottom = 6.dp)
                        .testTag("footer_text")
                )
            }
        }
    }
}

@Composable
private fun AppHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "AE LẤY MẪU TĐ8 MÃI ĐỈNH",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Slate900,
            textAlign = TextAlign.Center,
            lineHeight = 26.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "tính tổ mẫu bê đê",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Slate600,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun VolumeInputCard(
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("volume_card"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.5.dp, Slate200),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = "1. Tổng khối lượng bê tông (m³)",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Slate900
            )
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("volume_input"),
                textStyle = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                ),
                placeholder = {
                    Text(
                        text = "Ví dụ: 12.5 hoặc 60",
                        fontSize = 16.sp,
                        color = Slate400
                    )
                },
                trailingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 6.dp)
                    ) {
                        if (value.isNotEmpty()) {
                            IconButton(
                                onClick = onClear,
                                modifier = Modifier
                                    .size(32.dp)
                                    .testTag("clear_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Xóa nội dung",
                                    tint = Slate400,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Slate100
                        ) {
                            Text(
                                text = "m³",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Slate700,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Slate900,
                    unfocusedBorderColor = Slate300,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
        }
    }
}

@Composable
private fun ConcreteTypeCard(
    selectedType: ConcreteType,
    onTypeSelected: (ConcreteType) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("type_card"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.5.dp, Slate200),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Text(
                text = "2. Loại bê tông / vữa",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Slate900
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Bố cục gọn: Hàng 1 (Vữa, Bê tông), Hàng 2 (Bê tông - có thấm, Bê tông - có R7), Hàng 3 (Bê tông - có R7, có thấm)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Hàng 1
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CompactTypeButton(
                        type = ConcreteType.VUA,
                        isSelected = selectedType == ConcreteType.VUA,
                        onClick = { onTypeSelected(ConcreteType.VUA) },
                        modifier = Modifier.weight(1f)
                    )
                    CompactTypeButton(
                        type = ConcreteType.BE_TONG,
                        isSelected = selectedType == ConcreteType.BE_TONG,
                        onClick = { onTypeSelected(ConcreteType.BE_TONG) },
                        modifier = Modifier.weight(1.2f)
                    )
                }

                // Hàng 2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CompactTypeButton(
                        type = ConcreteType.BE_TONG_CO_THAM,
                        isSelected = selectedType == ConcreteType.BE_TONG_CO_THAM,
                        onClick = { onTypeSelected(ConcreteType.BE_TONG_CO_THAM) },
                        modifier = Modifier.weight(1f)
                    )
                    CompactTypeButton(
                        type = ConcreteType.BE_TONG_CO_R7,
                        isSelected = selectedType == ConcreteType.BE_TONG_CO_R7,
                        onClick = { onTypeSelected(ConcreteType.BE_TONG_CO_R7) },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Hàng 3
                CompactTypeButton(
                    type = ConcreteType.BE_TONG_CO_R7_CO_THAM,
                    isSelected = selectedType == ConcreteType.BE_TONG_CO_R7_CO_THAM,
                    onClick = { onTypeSelected(ConcreteType.BE_TONG_CO_R7_CO_THAM) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun CompactTypeButton(
    type: ConcreteType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .heightIn(min = 42.dp)
            .clickable(onClick = onClick)
            .testTag("type_option_${type.id}"),
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) Slate900 else Color.White,
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) Slate900 else Slate300
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (isSelected) Amber500 else Slate400,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = type.title,
                fontSize = 13.5.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else Slate800,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun ResultCard(result: SampleResult) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("result_container"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Slate900),
        border = BorderStroke(1.5.dp, Amber600),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "3. Số tổ mẫu",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Amber600
                ) {
                    Text(
                        text = "KẾT QUẢ",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Hiển thị 4 kết quả trong lưới 2x2 cực kỳ gọn, số to đậm, nhìn trọn không cần kéo xuống
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ResultGridItem(
                        count = result.r7,
                        label = "tổ nén R7",
                        tag = "result_r7",
                        modifier = Modifier.weight(1f)
                    )
                    ResultGridItem(
                        count = result.r28,
                        label = "tổ nén R28",
                        tag = "result_r28",
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ResultGridItem(
                        count = result.luu,
                        label = "tổ lưu",
                        tag = "result_luu",
                        modifier = Modifier.weight(1f)
                    )
                    ResultGridItem(
                        count = result.tham,
                        label = "tổ thấm",
                        tag = "result_tham",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun ResultGridItem(
    count: Int,
    label: String,
    tag: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF1E293B))
            .border(1.dp, Color(0xFF334155), RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .testTag(tag)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = count.toString(),
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Amber500,
                modifier = Modifier.widthIn(min = 28.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                lineHeight = 18.sp
            )
        }
    }
}
