package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AppBottomBar
import com.example.ui.components.AppToast
import com.example.ui.components.AppTopBar
import com.example.ui.components.CartDialog
import com.example.ui.components.ConfirmDialog
import com.example.ui.components.ProductFormDialog
import com.example.ui.components.QuantityDialog
import com.example.ui.components.ReceiptDialog
import com.example.ui.screens.PosScreen
import com.example.ui.screens.ProductsScreen
import com.example.ui.screens.TransactionsScreen
import com.example.ui.theme.BgColor
import kotlinx.coroutines.delay

@Composable
fun MainScreen(
    viewModel: PosViewModel,
    modifier: Modifier = Modifier
) {
    // Force RTL for Arabic POS Application
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        val products by viewModel.allProducts.collectAsStateWithLifecycle()
        val sales by viewModel.allSales.collectAsStateWithLifecycle()
        val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
        val posSearchQuery by viewModel.posSearchQuery.collectAsStateWithLifecycle()
        val productsSearchQuery by viewModel.productsSearchQuery.collectAsStateWithLifecycle()
        val transactionsSearchQuery by viewModel.transactionsSearchQuery.collectAsStateWithLifecycle()
        val activeCategory by viewModel.activeCategory.collectAsStateWithLifecycle()

        val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
        val cartTotalCount by viewModel.cartTotalCount.collectAsStateWithLifecycle()
        val cartTotalPrice by viewModel.cartTotalPrice.collectAsStateWithLifecycle()

        val qtyModalProduct by viewModel.qtyModalProduct.collectAsStateWithLifecycle()
        val qtyModalValue by viewModel.qtyModalValue.collectAsStateWithLifecycle()

        val cartModalOpen by viewModel.cartModalOpen.collectAsStateWithLifecycle()
        val selectedPayMethod by viewModel.selectedPayMethod.collectAsStateWithLifecycle()

        val receiptModalSale by viewModel.receiptModalSale.collectAsStateWithLifecycle()
        val productFormData by viewModel.productFormData.collectAsStateWithLifecycle()
        val confirmDeleteProduct by viewModel.confirmDeleteProduct.collectAsStateWithLifecycle()
        val confirmClearCart by viewModel.confirmClearCart.collectAsStateWithLifecycle()

        var currentToast by remember { mutableStateOf<String?>(null) }

        LaunchedEffect(Unit) {
            viewModel.toastEvent.collect { msg ->
                currentToast = msg
                delay(2200)
                if (currentToast == msg) {
                    currentToast = null
                }
            }
        }

        Scaffold(
            topBar = {
                AppTopBar(
                    currentTab = currentTab,
                    productCount = products.size,
                    salesCount = sales.size,
                    modifier = Modifier.statusBarsPadding()
                )
            },
            bottomBar = {
                AppBottomBar(
                    currentTab = currentTab,
                    onTabSelected = { viewModel.switchTab(it) }
                )
            },
            containerColor = BgColor,
            modifier = modifier.fillMaxSize()
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(BgColor)
            ) {
                when (currentTab) {
                    AppTab.POS -> {
                        PosScreen(
                            products = products,
                            searchQuery = posSearchQuery,
                            activeCategory = activeCategory,
                            cartTotalCount = cartTotalCount,
                            cartTotalPrice = cartTotalPrice,
                            onSearchQueryChange = { viewModel.setPosSearchQuery(it) },
                            onCategorySelect = { viewModel.setActiveCategory(it) },
                            onProductClick = { viewModel.openQtyModal(it) },
                            onOpenCart = { viewModel.openCartModal() }
                        )
                    }
                    AppTab.PRODUCTS -> {
                        ProductsScreen(
                            products = products,
                            searchQuery = productsSearchQuery,
                            onSearchQueryChange = { viewModel.setProductsSearchQuery(it) },
                            onEditProduct = { viewModel.openEditProductModal(it) },
                            onDeleteProduct = { viewModel.requestDeleteProduct(it) },
                            onAddProduct = { viewModel.openAddProductModal() }
                        )
                    }
                    AppTab.TRANSACTIONS -> {
                        TransactionsScreen(
                            sales = sales,
                            searchQuery = transactionsSearchQuery,
                            onSearchQueryChange = { viewModel.setTransactionsSearchQuery(it) },
                            onSaleClick = { viewModel.viewSaleReceipt(it) }
                        )
                    }
                }

                // Toast
                AppToast(
                    message = currentToast,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp)
                )
            }
        }

        // Modals
        QuantityDialog(
            product = qtyModalProduct,
            quantity = qtyModalValue,
            onIncrement = { viewModel.incrementQtyModal() },
            onDecrement = { viewModel.decrementQtyModal() },
            onConfirm = { viewModel.confirmQtyAdd() },
            onDismiss = { viewModel.closeQtyModal() }
        )

        CartDialog(
            isOpen = cartModalOpen,
            cartItems = cartItems,
            totalPrice = cartTotalPrice,
            selectedPayMethod = selectedPayMethod,
            onPayMethodChange = { viewModel.setPayMethod(it) },
            onQuantityChange = { prodId, delta -> viewModel.changeCartItemQty(prodId, delta) },
            onRemoveItem = { viewModel.removeCartItem(it) },
            onCheckout = { viewModel.checkout() },
            onRequestClearCart = { viewModel.requestClearCart() },
            onDismiss = { viewModel.closeCartModal() }
        )

        ReceiptDialog(
            sale = receiptModalSale,
            onClose = { viewModel.closeReceiptModal() }
        )

        ProductFormDialog(
            formData = productFormData,
            onNameChange = { viewModel.updateProductFormField(name = it) },
            onPriceChange = { viewModel.updateProductFormField(price = it) },
            onStockChange = { viewModel.updateProductFormField(stock = it) },
            onCategoryChange = { viewModel.updateProductFormField(category = it) },
            onSave = { viewModel.saveProductForm() },
            onDismiss = { viewModel.closeProductFormModal() }
        )

        // Confirm Delete Dialog
        confirmDeleteProduct?.let { prod ->
            ConfirmDialog(
                isOpen = true,
                title = "تأكيد الحذف",
                message = "سيتم حذف \"${prod.name}\" نهائياً، ولا يمكن التراجع عن هذا الإجراء.",
                confirmText = "حذف",
                isDanger = true,
                onConfirm = { viewModel.confirmDeleteProductAction() },
                onCancel = { viewModel.cancelDeleteProduct() }
            )
        }

        // Confirm Clear Cart Dialog
        ConfirmDialog(
            isOpen = confirmClearCart,
            title = "تفريغ السلة؟",
            message = "سيتم حذف جميع عناصر السلة الحالية.",
            confirmText = "تفريغ",
            isDanger = true,
            onConfirm = { viewModel.confirmClearCartAction() },
            onCancel = { viewModel.cancelClearCart() }
        )
    }
}
