package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CategoryConstants
import com.example.data.Product
import com.example.ui.theme.BorderLine
import com.example.ui.theme.BrandGradient
import com.example.ui.theme.BrandGradient120
import com.example.ui.theme.DangerColor
import com.example.ui.theme.SurfaceSolid
import com.example.ui.theme.TextColor
import com.example.ui.theme.TextMuted
import com.example.ui.theme.VioletDim
import com.example.ui.theme.VioletPrimary

@Composable
fun PosScreen(
    products: List<Product>,
    searchQuery: String,
    activeCategory: String,
    cartTotalCount: Int,
    cartTotalPrice: Double,
    onSearchQueryChange: (String) -> Unit,
    onCategorySelect: (String) -> Unit,
    onProductClick: (Product) -> Unit,
    onOpenCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("الكل") + CategoryConstants.CATEGORIES.map { it.name }
    val filteredProducts = products.filter { product ->
        val matchesQuery = product.name.contains(searchQuery, ignoreCase = true)
        val matchesCategory = activeCategory == "الكل" || product.category == activeCategory
        matchesQuery && matchesCategory
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("ابحث عن منتج...", fontSize = 13.5.sp, color = TextMuted) },
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

            Spacer(modifier = Modifier.height(12.dp))

            // Category Chips (Horizontal Scrollable)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { cat ->
                    val isActive = cat == activeCategory
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isActive) BrandGradient else androidx.compose.ui.graphics.Brush.linearGradient(listOf(SurfaceSolid, SurfaceSolid)))
                            .border(
                                width = 1.dp,
                                color = if (isActive) Color.Transparent else BorderLine,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { onCategorySelect(cat) }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = cat,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isActive) Color.White else TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Product Grid
            if (filteredProducts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = TextMuted.copy(alpha = 0.5f),
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "لا توجد منتجات مطابقة",
                            fontSize = 13.5.sp,
                            color = TextMuted
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 148.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = if (cartTotalCount > 0) 80.dp else 16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(filteredProducts, key = { it.id }) { product ->
                        ProductTile(
                            product = product,
                            onClick = { onProductClick(product) }
                        )
                    }
                }
            }
        }

        // Floating Cart Bar (cart-fab)
        if (cartTotalCount > 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 16.dp, shape = RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(BrandGradient120)
                        .clickable { onOpenCart() }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icon + Badge
                    Box(contentAlignment = Alignment.TopStart) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White.copy(alpha = 0.22f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "السلة",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Badge
                        Box(
                            modifier = Modifier
                                .size(18.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$cartTotalCount",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = VioletDim
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Text & Total
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "عرض السلة",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                        Text(
                            text = CategoryConstants.formatPrice(cartTotalPrice),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = Color.White
                        )
                    }

                    // Arrow
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "فتح السلة",
                        tint = Color.White.copy(alpha = 0.85f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ProductTile(
    product: Product,
    onClick: () -> Unit
) {
    val catInfo = CategoryConstants.catInfo(product.category)
    val isOutOfStock = product.stock <= 0
    val isLowStock = product.stock in 1..3

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceSolid),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(126.dp)
            .border(width = 1.dp, color = BorderLine, shape = RoundedCornerShape(14.dp))
            .alpha(if (isOutOfStock) 0.55f else 1f)
            .clickable(enabled = !isOutOfStock) { onClick() }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Category Accent Line on Start/Right
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxSize()
                    .background(catInfo.gradient)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 14.dp, top = 12.dp, end = 12.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top: Category tag & Name
                Column {
                    Text(
                        text = product.category,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = catInfo.color,
                        letterSpacing = 0.2.sp
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = product.name,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextColor,
                        maxLines = 2,
                        lineHeight = 16.sp
                    )
                }

                // Bottom: Price & Stock
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = CategoryConstants.formatPrice(product.price),
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = VioletDim
                    )
                    Text(
                        text = when {
                            isOutOfStock -> "نفدت"
                            isLowStock -> "متوفر ${product.stock}"
                            else -> "متوفر ${product.stock}"
                        },
                        fontSize = 10.sp,
                        fontWeight = if (isLowStock || isOutOfStock) FontWeight.Bold else FontWeight.Normal,
                        color = if (isLowStock || isOutOfStock) DangerColor else TextMuted
                    )
                }
            }
        }
    }
}
