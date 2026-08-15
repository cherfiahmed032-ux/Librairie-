package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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

@Composable
fun ReceiptDialog(
    sale: Sale?,
    onClose: () -> Unit
) {
    if (sale == null) return

    val items = sale.getItems()
    val receiptNumber = if (sale.id.length >= 6) sale.id.takeLast(6).uppercase() else sale.id.uppercase()

    Dialog(onDismissRequest = onClose) {
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceSolid),
            elevation = CardDefaults.cardElevation(defaultElevation = 24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .border(width = 1.dp, color = BorderLine, shape = RoundedCornerShape(22.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Success Circle
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(MintAccent, Color(0xFF22C1A0))))
                        .shadow(elevation = 12.dp, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "نجاح",
                        tint = Color.White,
                        modifier = Modifier.size(34.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "تم البيع بنجاح",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextColor
                )

                Text(
                    text = "#$receiptNumber",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Receipt Paper Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(SurfaceSolid)
                        .border(width = 1.dp, color = BorderLine, shape = RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        items.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = item.name,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextColor
                                )
                                Text(
                                    text = "${item.qty} × ${CategoryConstants.formatPrice(item.price)}",
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = TextMuted
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Pay method line
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "طريقة الدفع",
                                fontSize = 12.5.sp,
                                color = TextMuted
                            )
                            Text(
                                text = sale.payMethod,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextColor
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Total line
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(width = 1.dp, color = BorderLine.copy(alpha = 0.5f))
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "الإجمالي",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextColor
                            )
                            Text(
                                text = CategoryConstants.formatPrice(sale.total),
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = VioletDim
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Done button
                Button(
                    onClick = onClose,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = "حسناً، فاتورة جديدة",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
