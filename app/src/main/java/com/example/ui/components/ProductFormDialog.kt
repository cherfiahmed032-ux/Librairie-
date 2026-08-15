package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.CategoryConstants
import com.example.ui.ProductFormData
import com.example.ui.theme.BorderLine
import com.example.ui.theme.SurfaceSolid
import com.example.ui.theme.TextColor
import com.example.ui.theme.TextMuted
import com.example.ui.theme.VioletDim
import com.example.ui.theme.VioletPrimary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductFormDialog(
    formData: ProductFormData?,
    onNameChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onStockChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    if (formData == null) return

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceSolid),
            elevation = CardDefaults.cardElevation(defaultElevation = 24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .border(width = 1.dp, color = BorderLine, shape = RoundedCornerShape(22.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (formData.isEdit) "تعديل منتج" else "إضافة منتج",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = VioletDim
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق",
                            tint = TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Name Input
                Text(
                    text = "اسم المنتج",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = VioletDim
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = formData.name,
                    onValueChange = onNameChange,
                    placeholder = { Text("مثال: علبة أقلام تلوين", fontSize = 13.sp, color = TextMuted) },
                    singleLine = true,
                    shape = RoundedCornerShape(11.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VioletPrimary,
                        unfocusedBorderColor = BorderLine,
                        focusedContainerColor = SurfaceSolid,
                        unfocusedContainerColor = SurfaceSolid
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Price Input
                Text(
                    text = "السعر (دج)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = VioletDim
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = formData.price,
                    onValueChange = onPriceChange,
                    placeholder = { Text("0.00", fontSize = 13.sp, color = TextMuted) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(11.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VioletPrimary,
                        unfocusedBorderColor = BorderLine,
                        focusedContainerColor = SurfaceSolid,
                        unfocusedContainerColor = SurfaceSolid
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Stock Input
                Text(
                    text = "الكمية المتوفرة",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = VioletDim
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = formData.stock,
                    onValueChange = onStockChange,
                    placeholder = { Text("0", fontSize = 13.sp, color = TextMuted) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(11.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = VioletPrimary,
                        unfocusedBorderColor = BorderLine,
                        focusedContainerColor = SurfaceSolid,
                        unfocusedContainerColor = SurfaceSolid
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Category Picker
                Text(
                    text = "الفئة",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = VioletDim
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CategoryConstants.CATEGORIES.forEach { cat ->
                        val isSelected = formData.category == cat.name
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) cat.gradient else androidx.compose.ui.graphics.Brush.linearGradient(listOf(SurfaceSolid, SurfaceSolid)))
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) Color.Transparent else BorderLine,
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .clickable { onCategoryChange(cat.name) }
                                .padding(horizontal = 14.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = cat.name,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else TextMuted
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Submit button
                Button(
                    onClick = onSave,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VioletPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = "حفظ المنتج",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
