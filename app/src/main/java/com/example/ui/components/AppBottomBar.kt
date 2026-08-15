package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppTab
import com.example.ui.theme.BorderLine
import com.example.ui.theme.BrandGradient
import com.example.ui.theme.SurfaceSolid
import com.example.ui.theme.TextMuted
import com.example.ui.theme.VioletPrimary

@Composable
fun AppBottomBar(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 12.dp)
            .background(SurfaceSolid.copy(alpha = 0.95f))
            .border(width = 1.dp, color = BorderLine)
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tab 1: POS
            val posActive = currentTab == AppTab.POS
            val posColor by animateColorAsState(if (posActive) VioletPrimary else TextMuted, label = "posColor")

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onTabSelected(AppTab.POS) }
                    .padding(vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (posActive) {
                    Box(
                        modifier = Modifier
                            .width(28.dp)
                            .height(3.dp)
                            .clip(RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp))
                            .background(BrandGradient)
                    )
                } else {
                    Spacer(modifier = Modifier.height(3.dp))
                }
                Spacer(modifier = Modifier.height(4.dp))
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "نقطة البيع",
                    tint = posColor,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "نقطة البيع",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = posColor
                )
            }

            // Tab 2: Products
            val prodActive = currentTab == AppTab.PRODUCTS
            val prodColor by animateColorAsState(if (prodActive) VioletPrimary else TextMuted, label = "prodColor")

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onTabSelected(AppTab.PRODUCTS) }
                    .padding(vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (prodActive) {
                    Box(
                        modifier = Modifier
                            .width(28.dp)
                            .height(3.dp)
                            .clip(RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp))
                            .background(BrandGradient)
                    )
                } else {
                    Spacer(modifier = Modifier.height(3.dp))
                }
                Spacer(modifier = Modifier.height(4.dp))
                Icon(
                    imageVector = Icons.Default.Inventory2,
                    contentDescription = "المنتجات",
                    tint = prodColor,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "المنتجات",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = prodColor
                )
            }

            // Tab 3: Transactions / المعاملات
            val transActive = currentTab == AppTab.TRANSACTIONS
            val transColor by animateColorAsState(if (transActive) VioletPrimary else TextMuted, label = "transColor")

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onTabSelected(AppTab.TRANSACTIONS) }
                    .padding(vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (transActive) {
                    Box(
                        modifier = Modifier
                            .width(28.dp)
                            .height(3.dp)
                            .clip(RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp))
                            .background(BrandGradient)
                    )
                } else {
                    Spacer(modifier = Modifier.height(3.dp))
                }
                Spacer(modifier = Modifier.height(4.dp))
                Icon(
                    imageVector = Icons.Default.ReceiptLong,
                    contentDescription = "المعاملات",
                    tint = transColor,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "المعاملات",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = transColor
                )
            }
        }
    }
}
