package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppTab
import com.example.ui.theme.BgColor
import com.example.ui.theme.BorderLine
import com.example.ui.theme.BrandGradient
import com.example.ui.theme.TextColor
import com.example.ui.theme.VioletDim

@Composable
fun AppTopBar(
    currentTab: AppTab,
    productCount: Int,
    salesCount: Int = 0,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(BgColor.copy(alpha = 0.95f))
            .border(width = 1.dp, color = BorderLine)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Mark
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(BrandGradient)
                    .shadow(elevation = 6.dp, shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = "شعار التطبيق",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Brand Text
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "نقطة بيع الأدوات المدرسية",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextColor,
                    lineHeight = 18.sp
                )
                Text(
                    text = when (currentTab) {
                        AppTab.POS -> "بِع بسرعة، تتّبع بدقة"
                        AppTab.PRODUCTS -> "أضف منتجاتك وحدّد أسعارها"
                        AppTab.TRANSACTIONS -> "سجل المبيعات والفواتير السابقة"
                    },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = VioletDim
                )
            }

            // Stat Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(VioletDim.copy(alpha = 0.10f))
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (currentTab) {
                        AppTab.TRANSACTIONS -> "$salesCount معاملة"
                        else -> "$productCount منتج"
                    },
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = VioletDim
                )
            }
        }
    }
}
