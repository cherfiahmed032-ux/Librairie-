package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CategoryConstants
import com.example.data.Sale
import com.example.ui.theme.BorderLine
import com.example.ui.theme.MintAccent
import com.example.ui.theme.MintDim
import com.example.ui.theme.SurfaceSolid
import com.example.ui.theme.TextColor
import com.example.ui.theme.TextMuted
import com.example.ui.theme.VioletDim
import com.example.ui.theme.VioletPrimary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TransactionsScreen(
    sales: List<Sale>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSaleClick: (Sale) -> Unit,
    modifier: Modifier = Modifier
) {
    val filteredSales = sales.filter { sale ->
        val receiptNumber = if (sale.id.length >= 6) sale.id.takeLast(6).uppercase() else sale.id.uppercase()
        receiptNumber.contains(searchQuery, ignoreCase = true) ||
            sale.payMethod.contains(searchQuery, ignoreCase = true) ||
            sale.getItems().any { it.name.contains(searchQuery, ignoreCase = true) }
    }

    val totalRevenue = sales.sumOf { it.total }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Revenue Summary Banner Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceSolid),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = BorderLine, shape = RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "إجمالي المبيعات المحققة",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = CategoryConstants.formatPrice(totalRevenue),
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = VioletDim
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MintAccent.copy(alpha = 0.12f))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${sales.size} فاتورة",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = MintDim
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("بحث برقم الفاتورة أو اسم المنتج...", fontSize = 13.5.sp, color = TextMuted) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "بحث",
                        tint = TextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(13.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = VioletPrimary,
                    unfocusedBorderColor = BorderLine,
                    focusedContainerColor = SurfaceSolid,
                    unfocusedContainerColor = SurfaceSolid
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Transactions List
            if (filteredSales.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = null,
                            tint = TextMuted.copy(alpha = 0.5f),
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (sales.isEmpty()) "لا توجد معاملات بعد — قم بعمليات بيع لتظهر هنا" else "لا توجد معاملات مطابقة للبحث",
                            fontSize = 13.5.sp,
                            color = TextMuted
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(filteredSales, key = { it.id }) { sale ->
                        TransactionCard(
                            sale = sale,
                            onClick = { onSaleClick(sale) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionCard(
    sale: Sale,
    onClick: () -> Unit
) {
    val items = sale.getItems()
    val receiptNumber = if (sale.id.length >= 6) sale.id.takeLast(6).uppercase() else sale.id.uppercase()
    val totalItemsCount = items.sumOf { it.qty }
    val dateFormat = SimpleDateFormat("yyyy/MM/dd - HH:mm", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(sale.ts))
    val isCash = sale.payMethod == "نقدي"

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceSolid),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = BorderLine, shape = RoundedCornerShape(14.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header Row: Receipt # & Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Brush.linearGradient(listOf(VioletPrimary, Color(0xFF6B4EE6)))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Receipt,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "فاتورة #$receiptNumber",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = TextColor
                        )
                        Text(
                            text = formattedDate,
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }

                // Payment Method Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isCash) MintAccent.copy(alpha = 0.12f) else VioletPrimary.copy(alpha = 0.10f))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isCash) Icons.Default.Payments else Icons.Default.CreditCard,
                            contentDescription = null,
                            tint = if (isCash) MintDim else VioletDim,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = sale.payMethod,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCash) MintDim else VioletDim
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Items preview summary
            val itemsPreview = items.joinToString("، ") { "${it.name} (${it.qty})" }
            Text(
                text = itemsPreview,
                fontSize = 12.sp,
                color = TextColor.copy(alpha = 0.85f),
                maxLines = 2,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Footer Row: Items Count & Total & View Action
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = BorderLine.copy(alpha = 0.5f))
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$totalItemsCount قطعة",
                    fontSize = 11.5.sp,
                    color = TextMuted
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = CategoryConstants.formatPrice(sale.total),
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = VioletDim
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(VioletPrimary.copy(alpha = 0.08f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = "عرض الفاتورة",
                            tint = VioletDim,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        }
    }
}
